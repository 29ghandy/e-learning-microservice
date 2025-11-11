package org.example.notificationservice.services;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.dtos.CourseCreatedDTO;
import org.example.notificationservice.feign.CourseClient;
import org.example.notificationservice.feign.EnrollmentClient;
import org.example.notificationservice.feign.UserClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCreatedListener {
    private final EnrollmentClient enrollmentClient;
    private final UserClient userClient;
    private final CourseClient courseClient;
    private final EmailService emailService;

    @RabbitListener(queues = "course.created.update.queue")
    public void CourseListener(CourseCreatedDTO course) {
        Long teacherId = course.getTeacherId();

        List<Long> courseIds = courseClient.getTeacherCourses(teacherId);

        //students who bought teacher’s previous courses
        List<Long> studentIds = enrollmentClient.getStudentsByTeacher(teacherId, courseIds);

//        List<String> emails = userClient.getEmailsByIds(studentIds);
//
//        emailService.sendAnnouncementEmails(emails, course);
    }
}
