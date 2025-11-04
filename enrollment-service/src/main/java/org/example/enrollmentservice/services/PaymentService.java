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
import org.example.enrollmentservice.requestBodies.CourseInfo;
import org.example.enrollmentservice.requestBodies.EnrollmentRequestBody;
import org.example.enrollmentservice.services.helper.DiscountPublisher;
import org.example.enrollmentservice.services.helper.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

        List<Enrollment> enrollmen = enrollmentRepository.findAllByStudentId(request.getStudentId());
        HashSet<Long> st = new HashSet<>();
        for(Enrollment enrollment : enrollmen) {
            st.add(enrollment.getCourseId());
        }
        List<CourseInfo> courseIDs = request.getCourseIDs();
        for (var item : courseIDs) {
            if(st.contains(item.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("555555 a7a7a isbnoakbdoas");
            }
            long discountNumberOfMembers = redisService.atomicDecrementMembers(item.getId());
            if (discountNumberOfMembers == -2) {
                return ResponseEntity.badRequest().body("Payment failed, please try again");
            }

            DiscountCacheDTO discountCacheDTO = redisService.getDiscount(item.getId()).orElse(null);
            discountPublisher.publishDiscount(item.getId(), discountCacheDTO);
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
        List<CourseInfo> courseIds = request.getCourseIDs();
        List<Enrollment> enrollments = new ArrayList<>();
        List<OrderItem> orderItemsIds = new ArrayList<>();
        for (var item : courseIds) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCourseId(item.getId());
            orderItem.setPrice(item.getPrice());
            orderItem.setOrder(foundOrder);
            orderItemsIds.add(orderItem);

            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(request.getStudentId());
            enrollment.setCourseId(item.getId());
            enrollment.setPrice(item.getPrice());
            enrollment.setPaymentDate(LocalDate.now());
            enrollment.setOrder(foundOrder);
            enrollments.add(enrollment);
        }
        orderItemsRepository.saveAll(orderItemsIds);
        enrollmentRepository.saveAll(enrollments);
        return ResponseEntity.ok().body(response);
    }

}
