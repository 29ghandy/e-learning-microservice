package org.example.userservice.authtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.HashPasswordRequest;
import org.example.userservice.requestBodies.LoginRequest;
import org.example.userservice.requestBodies.SignUpRequest;
import org.example.userservice.services.AuthService;
import org.example.userservice.controllers.AuthController;
import org.example.userservice.services.helper.helperServices.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- /signup ----------

    @Test
    void signup_shouldReturn200_whenValidRequest() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("new@example.com");
        request.setPassword("password");
        request.setRole("STUDENT");
        request.setName("Ali Gadallah");

        when(authService.signUp(any(SignUpRequest.class)))
                .thenReturn("User registered successfully!");

        mockMvc.perform(multipart("/api/user/signup")
                        .param("name", "Ali Gadallah")
                        .param("email", "new@example.com")
                        .param("password", "password")
                        .param("role", "STUDENT"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully!")));

        verify(authService).signUp(any(SignUpRequest.class));
    }

    @Test
    void signup_shouldReturn400_whenServiceThrowsException() throws Exception {
        when(authService.signUp(any(SignUpRequest.class)))
                .thenThrow(new Exception("Email already registered"));

        mockMvc.perform(multipart("/api/user/signup")
                        .param("name", "Ali Gadallah")
                        .param("email", "existing@example.com")
                        .param("password", "password")
                        .param("role", "STUDENT"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already registered")));
    }

    // ---------- /login ----------

    @Test
    void login_shouldReturn200AndSetCookies_whenValidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        when(authService.login(any(LoginRequest.class), any()))
                .thenReturn("Logged in Successfully");

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged in Successfully")));

        verify(authService).login(any(LoginRequest.class), any());
    }

    @Test
    void login_shouldReturn400_whenServiceThrowsException() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("wrongpass");

        when(authService.login(any(LoginRequest.class), any()))
                .thenThrow(new Exception("Invalid login credentials"));

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid login credentials")));
    }

    // ---------- /refresh-token ----------

    @Test
    void refreshToken_shouldReturn200_whenServiceReturnsOk() throws Exception {
        // arrange
        ResponseEntity<?> responseEntity = ResponseEntity.ok("Token refreshed");

        // IMPORTANT: use Mockito.doReturn(...) here
        doReturn(responseEntity)
                .when(authService)
                .refreshToken(any(), any());

        // act + assert
        mockMvc.perform(get("/api/user/refresh-token"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Token refreshed")));

        verify(authService).refreshToken(any(), any());
    }

    @Test
    void refreshToken_shouldReturn400_whenServiceThrowsException() throws Exception {
        when(authService.refreshToken(any(), any()))
                .thenThrow(new Exception("Invalid refresh token"));

        mockMvc.perform(get("/api/user/refresh-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid refresh token")));
    }

    // ---------- /logout ----------

    @Test
    void logout_shouldReturn200() throws Exception {
        when(authService.logout(any(), any()))
                .thenReturn(ResponseEntity.ok("Logged out Successfully"));

        mockMvc.perform(post("/api/user/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out Successfully")));

        verify(authService).logout(any(), any());
    }

    // ---------- /hash-password ----------

    @Test
    void hash_shouldReturnHashedPassword() throws Exception {
        HashPasswordRequest request = new HashPasswordRequest();
        request.setPassword("plain");

        when(authService.hash(any(HashPasswordRequest.class)))
                .thenReturn("encoded");

        mockMvc.perform(get("/api/user/hash-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("encoded"));

        verify(authService).hash(any(HashPasswordRequest.class));
    }

    // ---------- /{id} ----------

    @Test
    void getUserById_shouldReturnEmail_whenUserExists() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("user@example.com"));

        verify(userRepository).findById(1L);
    }

    // ---------- /users/emails ----------

    @Test
    void getEmailsByIds_shouldReturnEmailList() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<String> emails = List.of("u1@example.com", "u2@example.com");

        when(userRepository.findEmailsByIdIn(ids))
                .thenReturn(emails);

        mockMvc.perform(post("/api/user/users/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("u1@example.com"))
                .andExpect(jsonPath("$[1]").value("u2@example.com"));

        verify(userRepository).findEmailsByIdIn(ids);
    }
}
