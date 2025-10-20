package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    UserRepository userRepository;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));


        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
