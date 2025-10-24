package org.example.userservice.services.helper.helperServices;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final JavaMailSenderImpl mailSender;

    public String sendForgetPasswordCode(String from,String subject,String to, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);

        message.setText("Reset password code : " + otp + "\n" +
                "It is valid for 5 minutes");

        javaMailSender.send(message);
        return otp;
    }


}