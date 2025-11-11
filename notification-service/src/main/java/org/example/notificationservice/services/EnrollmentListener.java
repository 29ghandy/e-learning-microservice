package org.example.notificationservice.services;


import lombok.RequiredArgsConstructor;
import org.example.notificationservice.feign.UserClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentListener {
    private final UserClient userClient;
    private final EmailService emailService;

    @RabbitListener(queues = "email.payment.queue")
    public void enrollmentListener(EmailMessage message) {
        List<Long> studentId = new ArrayList<>();
        studentId.add(message.studentId);
        List<String> emails = userClient.getEmailsByIds(studentId);

        emailService.sendPaymentConfirmationEmail(emails.get(0));
    }

    public record EmailMessage(Long studentId, List<Long> courses) implements Serializable {}
}
