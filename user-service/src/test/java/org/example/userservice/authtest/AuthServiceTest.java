package org.example.userservice.authtest;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.userservice.models.Users;
import org.example.userservice.models.Role;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.HashPasswordRequest;
import org.example.userservice.requestBodies.LoginRequest;
import org.example.userservice.requestBodies.SignUpRequest;
import org.example.userservice.services.helper.designPatterns.userFinder.UserFinderFactory;
import org.example.userservice.services.helper.designPatterns.userFinder.UserFinderStrategy;
import org.example.userservice.services.helper.designPatterns.userSaving.SignUpFactory;
import org.example.userservice.services.helper.designPatterns.userSaving.UserSave;
import org.example.userservice.services.helper.helperServices.JwtService;
import org.example.userservice.services.helper.helperServices.UserDetailsService;
import org.example.userservice.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private SignUpFactory signUpFactory;
    @Mock
    private UserFinderFactory userFinderFactory;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void init() {
        // set jwt expiry fields so tests don't break
        ReflectionTestUtils.setField(authService, "accessTokenExpires", 3600L);
        ReflectionTestUtils.setField(authService, "refreshTokenExpires", 86400L);
    }

    // ---------- signUp ----------

    @Test
    void signUp_shouldThrowException_whenEmailAlreadyExists() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        request.setRole("STUDENT");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new Users()));

        Exception ex = assertThrows(Exception.class, () -> authService.signUp(request));
        assertThat(ex.getMessage()).contains("Email already registered");

        verify(userRepository).findByEmail("test@example.com");
        verifyNoMoreInteractions(signUpFactory);
    }

    @Test
    void signUp_shouldCallStrategy_whenEmailNotRegistered() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("new@example.com");
        request.setRole("STUDENT");

        when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.empty());

        UserSave userSave = mock(UserSave.class);
        when(signUpFactory.getStrategy("STUDENT"))
                .thenReturn(userSave);

        String result = authService.signUp(request);

        assertThat(result).isEqualTo("User registered successfully!");
        verify(userRepository).findByEmail("new@example.com");
        verify(signUpFactory).getStrategy("STUDENT");
        verify(userSave).signUp(request);
    }

    // ---------- login ----------

    @Test
    void login_shouldAuthenticateAndSetCookies_forStudentOrTeacher() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("student@example.com");
        request.setPassword("password");

        HttpServletResponse response = mock(HttpServletResponse.class);

        // mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // mock user
        Users user = new Users();
        user.setId(1L);
        user.setBanned(false);

        Role role = new Role();
        role.setName("STUDENT");
        user.setRole(role);

        when(userRepository.findByEmail("student@example.com"))
                .thenReturn(Optional.of(user));

        // userFinder for STUDENT ROLE
        UserFinderStrategy userFinderStrategy = mock(UserFinderStrategy.class);
        when(userFinderFactory.getStrategy("STUDENT ROLE"))
                .thenReturn(userFinderStrategy);
        when(userFinderStrategy.findRoleID(1L))
                .thenReturn(10L); // e.g. studentId

        // jwt generation
        when(jwtService.generateToken(anyMap(), eq(user), anyLong()))
                .thenReturn("access-token-value")
                .thenReturn("refresh-token-value");

        String result = authService.login(request, response);

        assertThat(result).isEqualTo("Logged in Successfully");

        verify(authenticationManager).authenticate(
                argThat(token ->
                        token.getPrincipal().equals("student@example.com")
                                && token.getCredentials().equals("password")
                )
        );

        // verify cookies were added
        verify(response, times(2)).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    void login_shouldThrowException_whenUserIsBanned() {
        LoginRequest request = new LoginRequest();
        request.setEmail("banned@example.com");
        request.setPassword("password");

        HttpServletResponse response = mock(HttpServletResponse.class);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Users user = new Users();
        user.setId(1L);
        user.setBanned(true);
        Role role = new Role();
        role.setName("STUDENT");
        user.setRole(role);

        when(userRepository.findByEmail("banned@example.com"))
                .thenReturn(Optional.of(user));

        Exception ex = assertThrows(Exception.class, () -> authService.login(request, response));
        assertThat(ex.getMessage()).contains("Invalid login credentials");

        verify(response, never()).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    void login_shouldThrowException_whenUnknownRole() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        HttpServletResponse response = mock(HttpServletResponse.class);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Users user = new Users();
        user.setId(1L);
        user.setBanned(false);
        Role role = new Role();
        role.setName("UNKNOWN_ROLE");
        user.setRole(role);

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(user));

        // userFinderFactory will return null by default (not stubbed)

        Exception ex = assertThrows(Exception.class, () -> authService.login(request, response));
        assertThat(ex.getMessage()).contains("Invalid login credentials");

        verify(response, never()).addHeader(eq("Set-Cookie"), anyString());
    }

    // ---------- refreshToken ----------

    @Test
    void refreshToken_shouldGenerateNewAccessToken_whenRefreshTokenValid() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("refresh-token", "refresh-token-value");

        when(jwtService.extractTokenFromCookies(request)).thenReturn(tokenMap);
        when(jwtService.extractUserName("refresh-token-value"))
                .thenReturn("user@example.com");

        // userDetails
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com"))
                .thenReturn(userDetails);

        // claims extracted from refresh token
        Claims claims = mock(Claims.class);
        when(jwtService.extractClaims("refresh-token-value"))
                .thenReturn(claims);

        when(jwtService.generateToken(eq(claims), eq(userDetails), anyLong()))
                .thenReturn("new-access-token");

        ResponseEntity<?> responseEntity = authService.refreshToken(request, response);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo("Token refreshed");

        verify(response).addHeader(eq("Set-Cookie"), contains("access_token"));
    }

    @Test
    void refreshToken_shouldThrowException_whenRefreshTokenInvalid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("refresh-token", "bad-token");
        when(jwtService.extractTokenFromCookies(request))
                .thenReturn(tokenMap);
        when(jwtService.extractUserName("bad-token"))
                .thenReturn(null);

        Exception ex = assertThrows(Exception.class,
                () -> authService.refreshToken(request, response));

        assertThat(ex.getMessage()).contains("Invalid refresh token");
    }

    // ---------- logout ----------

    @Test
    void logout_shouldClearCookies() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<String> result = authService.logout(request, response);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("Logged out Successfully");

        // 2 cookies deleted
        verify(response, times(2)).addHeader(eq("Set-Cookie"), anyString());
    }

    // ---------- hash ----------

    @Test
    void hash_shouldUsePasswordEncoder() {
        HashPasswordRequest request = new HashPasswordRequest();
        request.setPassword("plain");

        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        String result = authService.hash(request);

        assertThat(result).isEqualTo("encoded");
        verify(passwordEncoder).encode("plain");
    }
}
