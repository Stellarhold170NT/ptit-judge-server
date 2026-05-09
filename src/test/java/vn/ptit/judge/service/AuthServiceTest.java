package vn.ptit.judge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import vn.ptit.judge.model.Session;
import vn.ptit.judge.model.User;
import vn.ptit.judge.repository.SessionRepository;
import vn.ptit.judge.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @TempDir
    Path tempDir;

    private ObjectMapper objectMapper;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir);

        userRepository = new UserRepository(objectMapper, dataDir.toString());
        sessionRepository = new SessionRepository(objectMapper, dataDir.toString());

        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

        authService = new AuthService(userRepository, sessionRepository, passwordEncoder);
    }

    @Test
    void registerCreatesUserWithHashedPassword() {
        User user = authService.register("test@example.com", "password123", "B19DCD001", "Test User");

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("B19DCD001", user.getStudentCode());
        assertEquals("Test User", user.getDisplayName());
        assertNotNull(user.getPasswordHash());
        assertNotEquals("password123", user.getPasswordHash());
        assertTrue(user.getPasswordHash().startsWith("$2"));
    }

    @Test
    void registerThrowsExceptionWhenEmailExists() {
        authService.register("test@example.com", "password123", "B19DCD001", "Test User");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.register("test@example.com", "password456", "B19DCD002", "Another User"));

        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    void registerThrowsExceptionWhenStudentCodeExists() {
        authService.register("test@example.com", "password123", "B19DCD001", "Test User");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.register("test2@example.com", "password456", "B19DCD001", "Another User"));

        assertEquals("Student code already registered", exception.getMessage());
    }

    @Test
    void loginWithCorrectCredentialsReturnsToken() {
        authService.register("test@example.com", "password123", "B19DCD001", "Test User");

        String token = authService.login("test@example.com", "password123");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void loginWithWrongPasswordThrowsException() {
        authService.register("test@example.com", "password123", "B19DCD001", "Test User");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.login("test@example.com", "wrongpassword"));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void loginWithNonexistentUserThrowsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.login("nonexistent@example.com", "password123"));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void getUserFromTokenReturnsUserForValidToken() {
        User registered = authService.register("test@example.com", "password123", "B19DCD001", "Test User");
        String token = authService.login("test@example.com", "password123");

        Optional<User> userOpt = authService.getUserFromToken(token);

        assertTrue(userOpt.isPresent());
        assertEquals(registered.getEmail(), userOpt.get().getEmail());
        assertEquals(registered.getStudentCode(), userOpt.get().getStudentCode());
    }

    @Test
    void getUserFromTokenReturnsEmptyForInvalidToken() {
        Optional<User> userOpt = authService.getUserFromToken("invalid-token");

        assertTrue(userOpt.isEmpty());
    }

    @Test
    void logoutDeletesSession() {
        authService.register("test@example.com", "password123", "B19DCD001", "Test User");
        String token = authService.login("test@example.com", "password123");

        assertTrue(authService.isTokenValid(token));

        authService.logout(token);

        assertFalse(authService.isTokenValid(token));
    }

    @Test
    void isTokenValidReturnsFalseForExpiredToken() {
        // Create a session manually that is already expired
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        authService = new AuthService(userRepository, sessionRepository, passwordEncoder);

        User user = new User("B19DCD001", "test@example.com",
                passwordEncoder.encode("password123"), "Test User", LocalDateTime.now());
        userRepository.save(user);

        Session expiredSession = new Session(
                "expired-token",
                "B19DCD001",
                LocalDateTime.now().minusHours(25),
                LocalDateTime.now().minusHours(1)
        );
        sessionRepository.save(expiredSession);

        assertFalse(authService.isTokenValid("expired-token"));
    }

    @Test
    void isTokenValidReturnsTrueForValidToken() {
        authService.register("test@example.com", "password123", "B19DCD001", "Test User");
        String token = authService.login("test@example.com", "password123");

        assertTrue(authService.isTokenValid(token));
    }
}