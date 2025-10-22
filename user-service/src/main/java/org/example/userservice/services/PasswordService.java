package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.ChangePasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final RedisService redisService;
    private final  EmailService emailService;
    public String changePassword(@RequestBody ChangePasswordRequest request) throws BadRequestException {
        Users user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

         if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
              throw new BadRequestException("Old password does not match");
         }
         String newPassword = request.getNewPassword();
         user.setPassword(passwordEncoder.encode(newPassword));
         userRepository.save(user);
         return "Password changed";
    }

    public String forgetPassword(@RequestBody String email) {
        //generate otp
        //cache otp and user
        //send otp in email
        System.out.println(email);
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String otp = otpService.generateOtp();
        redisService.redisSaveForgetPasswordCode(user.getEmail(), otp);
       emailService.sendForgetPasswordCode("omarmamdouh753@gmail.com","change ur password",email,otp);
       return "Email sent successfully";
    }
}
