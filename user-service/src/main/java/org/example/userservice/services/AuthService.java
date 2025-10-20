package org.example.userservice.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.userservice.config.SecurityConfig.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public String signUp(SignUpRequest requestBody) throws Exception {

        if (userRepository.findByEmail(requestBody.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }


        String imageUrl = saveImage(requestBody.getImage());


        Users user = new Users();
        user.setName(requestBody.getName());
        user.setEmail(requestBody.getEmail());
        user.setPassword(encoder.encode(requestBody.getPassword()));
        user.setImageUrl(imageUrl);
        userRepository.save(user);

        return "User registered successfully!";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) return null;

        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String filePath = uploadDir + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.copy(imageFile.getInputStream(), path);

        return filePath;
    }
}
