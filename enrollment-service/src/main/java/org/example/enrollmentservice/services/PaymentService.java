package org.example.enrollmentservice.services;


import ch.qos.logback.core.joran.sanity.Pair;
import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.dtos.DiscountCacheDTO;
import org.example.enrollmentservice.enums.Status;
import org.example.enrollmentservice.models.Enrollment;
import org.example.enrollmentservice.models.Order;
import org.example.enrollmentservice.models.OrderItem;
import org.example.enrollmentservice.repostories.EnrollmentRepository;
import org.example.enrollmentservice.repostories.OrderItemsRepository;
import org.example.enrollmentservice.repostories.OrderRepository;
import org.example.enrollmentservice.requestBodies.EnrollmentRequestBody;
import org.example.enrollmentservice.services.helper.DiscountPublisher;
import org.example.enrollmentservice.services.helper.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.key}")
    private String stripeKey;
    private final EnrollmentRepository enrollmentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final RedisService redisService;
    private final DiscountPublisher discountPublisher;

    @Transactional
    public ResponseEntity<?> payCourses(EnrollmentRequestBody request) throws StripeException {
        // loop through list of courseIds
        // atomic decrement discount members in cache
        // if decrement successful proceed payment

        List<Pair<Long,Double>> courseIDs = request.getCourseIDs();
        for (var item : courseIDs) {
            long discountNumberOfMembers = redisService.atomicDecrementMembers(item.first);
            if (discountNumberOfMembers == -2) {
                return ResponseEntity.badRequest().body("Payment failed, please try again");
            }

            DiscountCacheDTO discountCacheDTO = redisService.getDiscount(item.first).orElse(null);
            discountPublisher.publishDiscount(item.first, discountCacheDTO);
        }

        Stripe.apiKey = stripeKey;
        Map<String, Object> params = new HashMap<>();
        long amountInCents = Math.round(request.getTotalPrice() * 100);
        params.put("amount", amountInCents);
        params.put("currency", "usd");
        params.put("automatic_payment_methods", Map.of("enabled", true));
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());

        Order order = new Order();
        order.setTotalPrice(request.getTotalPrice());
        order.setOrderStatus(Status.valueOf("PAYED"));
        order.setStudentId(request.getStudentId());
        orderRepository.save(order);
        Order foundOrder = orderRepository.findByStudentId(request.getStudentId());
        List<Pair<Long, Double>> courseIds = request.getCourseIDs();
        List<Enrollment> enrollments = new ArrayList<>();
        List<OrderItem> orderItemsIds = new ArrayList<>();
        for (var item : courseIds) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCourseId(item.first);
            orderItem.setPrice(item.second);
            orderItem.setOrder(foundOrder);
            orderItemsIds.add(orderItem);

            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(request.getStudentId());
            enrollment.setCourseId(item.first);
            enrollment.setPrice(item.second);
            enrollment.setPaymentDate(LocalDate.now());
        }
        orderItemsRepository.saveAll(orderItemsIds);
        enrollmentRepository.saveAll(enrollments);
        return ResponseEntity.ok().body(response);
    }

}
