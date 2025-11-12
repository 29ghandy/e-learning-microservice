package org.example.notificationservice.services;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.config.FeignAuthInterceptor;
import org.example.notificationservice.dtos.CourseCreatedDTO;
import org.example.notificationservice.feign.CourseClient;
import org.example.notificationservice.feign.EnrollmentClient;
import org.example.notificationservice.feign.UserClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCreatedListener {
    private final EnrollmentClient enrollmentClient;
    private final UserClient userClient;
    private final CourseClient courseClient;
    private final EmailService emailService;

    @RabbitListener(queues = "course.created.update.queue")
    public void CourseListener(CourseMessage message) {
        CourseCreatedDTO course = message.course;

        try {
            FeignAuthInterceptor.setToken(course.getJwtToken());

            if (course.getTeacherId() == null) {
                System.out.println("null ya khwl");
                return;
            }
            Long teacherId = course.getTeacherId();
            List<Long> courseIds = courseClient.getTeacherCourses(teacherId);
            System.out.println(courseIds.size());
            //List<Long> studentIds = enrollmentClient.getStudentsByTeacher(teacherId, courseIds);
            List<Long> studentIds = new ArrayList<>();
            studentIds.add(1L);
            List<String> emails = userClient.getEmailsByIds(studentIds);

            //emailService.sendAnnouncementEmails(emails, course);
        } finally {
            FeignAuthInterceptor.clear();
        }
    }

    public record CourseMessage(CourseCreatedDTO course) {}
}
