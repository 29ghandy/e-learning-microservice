package org.example.courseservice.services.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.dtos.DiscountCacheDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> discountCache;
    private final RedisTemplate<String, Object> paymentCache;
    private  final ObjectMapper mapper = new ObjectMapper();
    private static final String DISCOUNT_COURSE = "discount-course:";
    private static final String PAYMENT_PREFIX = "payment:";
    public void saveDiscount(Long courseId, DiscountCacheDTO discount, Duration ttl) {
        String key = DISCOUNT_COURSE + courseId;
        discountCache.opsForValue().set(key, discount, ttl);
    }
    public void savePayment(Long studentId, List<Long> courses) {
        String key = PAYMENT_PREFIX + studentId;
        List<Long> coursesIds = mapper.convertValue(paymentCache.opsForValue().get(key),List.class);
        if (coursesIds ==  null)  coursesIds = new ArrayList<>();
        coursesIds.addAll(courses);
        paymentCache.opsForValue().set(key, coursesIds);
    }
    public Optional<DiscountCacheDTO> getDiscount(Long courseId) {
        String key = DISCOUNT_COURSE + courseId;
        DiscountCacheDTO discount = mapper.convertValue(discountCache.opsForValue().get(key), DiscountCacheDTO.class);
        return Optional.ofNullable(discount);
    }

    public void deleteDiscount(Long courseId) {
        discountCache.delete(DISCOUNT_COURSE + courseId);
    }

    public boolean courseExists(Long courseId) {
        return Boolean.TRUE.equals(discountCache.hasKey(DISCOUNT_COURSE + courseId));
    }
    public boolean paymentExists(Long studentId, Long courseId) {
        String key = PAYMENT_PREFIX + studentId;
        if(paymentCache.hasKey(key))
        {
            try {

                String json = paymentCache.opsForValue().get(key, 0, -1);

                List<Long> courses = mapper.readValue(json, new TypeReference<List<Long>>() {});

                return courses.contains(courseId);
            } catch (Exception e) {
                e.printStackTrace(); // optional: log it properly
                return false;
            }
        }
        return false;
    }
}
