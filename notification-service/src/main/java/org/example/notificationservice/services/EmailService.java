package org.example.notificationservice.services;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.dtos.CourseCreatedDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAnnouncementEmails(List<String> recipientEmails, CourseCreatedDTO course) {
        for (String email : recipientEmails) {
            sendEmail(
                    email,
                    "New course from your teacher!",
                    buildEmailBody(course)
            );
        }
    }

    public void sendPaymentConfirmationEmail(String recipientEmail) {
        String subject = "Payment Confirmation - E-Learning Platform";
        String body = """
                Hello,
                
                Your payment has been successfully processed.
                
                Thank you for your purchase! You can now access your content on our platform.
                
                Best regards,
                The E-Learning Team
                """;

        sendEmail(recipientEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    private String buildEmailBody(CourseCreatedDTO course) {
        return String.format(
                "Hello!\n\n" +
                        "Your teacher %s has just uploaded a new course titled \"%s\".\n\n" +
                        "Course Description:\n%s\n\n" +
                        "Price: $%.2f\n\n" +
                        "Check it out now on the platform!\n\n" +
                        "Best regards,\n" +
                        "The E-Learning Team",
                course.getTeacherName(),
                course.getCourseName(),
                course.getDescription(),
                course.getPrice()
        );
    }
}
