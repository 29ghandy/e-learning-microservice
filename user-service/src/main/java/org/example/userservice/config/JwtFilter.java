package org.example.userservice.config;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.userservice.services.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
         final String authorizationHeader = request.getHeader("Authorization");
         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
             final String token = authorizationHeader.substring(7);
             final String username = JwtService.extractUserName(token);
         }
         filterChain.doFilter(request, response);
    }
}

