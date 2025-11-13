package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.userservice.models.Interest;
import org.example.userservice.models.SocialLink;
import org.example.userservice.models.Student;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.InterestRepository;
import org.example.userservice.repositories.SocialLinkRepository;
import org.example.userservice.repositories.StudentRepository;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.AddInterestsRequest;
import org.example.userservice.requestBodies.AddSocialLinksRequest;
import org.example.userservice.requestBodies.ChangePasswordRequest;
import org.example.userservice.requestBodies.ResetPasswordRequest;
import org.example.userservice.services.helper.helperServices.EmailService;
import org.example.userservice.services.helper.helperServices.OTPService;
import org.example.userservice.services.helper.helperServices.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final RedisService redisService;
    private final EmailService emailService;
    private final InterestRepository interestRepository;
    private final SocialLinkRepository socialLinkRepository;
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

    public String resetPassword(@RequestBody  ResetPasswordRequest resetPasswordRequest) throws Exception {
        Users connectedUser = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + resetPasswordRequest.getEmail() + " is not found"));


        redisService.validateOtp(resetPasswordRequest.getEmail(),resetPasswordRequest.getCode());
        connectedUser.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(connectedUser);
        return "Password reset successfully";
    }
    public ResponseEntity<?>addSocialLinks(AddSocialLinksRequest request)
    {
        List<SocialLink> socialUrls = new ArrayList<>();
        for(var url : request.getUrls()) {
            SocialLink socialLink = new SocialLink();
            socialLink.setId(request.getTeacherId());
            socialLink.setUrl(url);
            socialUrls.add(socialLink);
        }
        socialLinkRepository.saveAll(socialUrls);
        return ResponseEntity.status(HttpStatus.CREATED).body("social links added successfully");
    }
    public ResponseEntity<?>addInterests(AddInterestsRequest request)
    {
        List<Interest> interests = new ArrayList<>();
        for(var interestName : request.getInterests()) {
            Interest interest = new Interest();
            interest.setId(request.getStudentId());
            interest.setName(interestName);
            interests.add(interest);
        }
        interestRepository.saveAll(interests);
        return ResponseEntity.status(HttpStatus.CREATED).body("Interests added successfully");
    }
}
