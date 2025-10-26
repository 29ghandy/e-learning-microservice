package org.example.userservice.services;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.HashPasswordRequest;
import org.example.userservice.requestBodies.LoginRequest;
import org.example.userservice.requestBodies.SignUpRequest;
import org.example.userservice.services.helper.helperServices.JwtService;
import org.example.userservice.services.helper.helperServices.UserDetailsService;
import org.example.userservice.services.helper.designPatterns.userFinder.UserFinderFactory;
import org.example.userservice.services.helper.designPatterns.userFinder.UserFinderStrategy;
import org.example.userservice.services.helper.designPatterns.userSaving.SignUpFactory;
import org.example.userservice.services.helper.designPatterns.userSaving.UserSave;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private  final UserFinderFactory userFinderFactory;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Value(("${jwt.access.expire}"))
    private long accessTokenExpires;
    @Value("${jwt.refresh.expire}")
    private long refreshTokenExpires;


    public String signUp(SignUpRequest requestBody) throws Exception {

        if (userRepository.findByEmail(requestBody.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }

        UserSave strategy = signupFactory.getStrategy(requestBody.getRole());
        strategy.signUp(requestBody);
        return "User registered successfully!";
    }


    public String login(@RequestBody @Valid LoginRequest requestBody, HttpServletResponse response) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestBody.getEmail(),
                            requestBody.getPassword()
                    )
            );


            Users user = userRepository.findByEmail(requestBody.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + requestBody.getEmail()));

            UserFinderStrategy userFinderStrategy = userFinderFactory.getStrategy(user.getRole().getName() + " ROLE");
            if (userFinderStrategy == null && !user.getRole().getName().equals("ADMIN") &&  !user.getRole().getName().equals("SUPER ADMIN")) {
                throw new IllegalArgumentException("Unknown role: " + user.getRole().getName());
            }
            long id = (user.getRole().getName().equals( "TEACHER") || user.getRole().getName().equals( "STUDENT"))? userFinderStrategy.findRoleID(user.getId()): user.getId();

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().getName());
            claims.put("id", id);


            String accessToken = jwtService.generateToken(claims, user,accessTokenExpires);
            String refreshToken = jwtService.generateToken(claims, user,refreshTokenExpires);

            System.out.println(accessToken);
            ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(accessTokenExpires)
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(refreshTokenExpires)
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            return "Logged in Successfully";
        } catch (Exception e) {
            throw new Exception("Invalid login credentials: " + e.getMessage());
        }

    }
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String refreshToken = jwtService.extractTokenFromCookies(request).get("refresh-token");
        String username = jwtService.extractUserName(refreshToken);
        if (username == null) {
            throw new Exception("Invalid refresh token");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Claims claims = jwtService.extractClaims(refreshToken);

        String accessToken = jwtService.generateToken(claims, userDetails, accessTokenExpires);
        System.out.println(accessToken);
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(accessTokenExpires)
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity.ok("Token refreshed");
    }
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ResponseCookie deleteAccess = ResponseCookie.from("access_token", "")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefresh = ResponseCookie.from("refresh_token", "")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteAccess.toString());
        response.addHeader("Set-Cookie", deleteRefresh.toString());

        return ResponseEntity.ok("Logged out Successfully");
    }

    public String hash(HashPasswordRequest request) {
        return passwordEncoder.encode(request.getPassword());
    }
}
