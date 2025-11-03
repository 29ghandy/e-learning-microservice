package org.example.courseservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.repositories.CourseRepository;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisExpirationListener implements MessageListener {

    private final CourseRepository courseRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // e.g. "discount:42"
        if (!expiredKey.startsWith("discount:")) return;

        Long courseId = Long.valueOf(expiredKey.replace("discount:", ""));
        courseRepository.findById(courseId).ifPresent(course -> {
            course.setDiscountPercentage(0.0);
            course.setDiscountNumberOfMembers(0L);
            course.setDiscountStartDate(null);
            course.setDiscountEndDate(null);
            courseRepository.save(course);
        });
        System.out.println("Discount expired for courseId=" + courseId);
    }
}
