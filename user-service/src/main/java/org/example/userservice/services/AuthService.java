package org.example.userservice.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.LoginRequest;
import org.example.userservice.requestBodies.SignUpRequest;
import org.example.userservice.services.helper.roleIDFinder.RoleFinderFactory;
import org.example.userservice.services.helper.roleIDFinder.RoleFinderStrategy;
import org.example.userservice.services.helper.userSaving.SignUpFactory;
import org.example.userservice.services.helper.userSaving.UserSave;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SignUpFactory signupFactory;
    private  final RoleFinderFactory roleFinderFactory;
    @Value(("${jwt.access.expire}"))
    private long accessTokenExpires;
    @Value("${jwt.access.expire}")
    private long refreshTokenExpires;


    public String signUp(SignUpRequest requestBody) throws Exception {

        if (userRepository.findByEmail(requestBody.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }

        UserSave strategy = signupFactory.getStrategy(requestBody.getRole());
        strategy.signUp(requestBody);
        return "User registered successfully!";
    }


    public Map<String, String> login(@RequestBody @Valid LoginRequest requestBody) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestBody.getEmail(),
                            requestBody.getPassword()
                    )
            );


            Users user = userRepository.findByEmail(requestBody.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + requestBody.getEmail()));

            RoleFinderStrategy roleFinderStrategy = roleFinderFactory.getStrategy(user.getRole().getName() + " ROLE");
            long id = roleFinderStrategy.findRoleID(user.getId());

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().getName());
            claims.put("email", user.getEmail());
            claims.put("id", id);


            String token = jwtService.generateToken(claims, user);

            return Map.of("token", token);
        } catch (Exception e) {
            throw new Exception("Invalid login credentials: " + e.getMessage());
        }
    }


}
