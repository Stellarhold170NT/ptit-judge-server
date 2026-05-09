package vn.ptit.judge.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ptit.judge.model.Session;
import vn.ptit.judge.model.User;
import vn.ptit.judge.repository.SessionRepository;
import vn.ptit.judge.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password, String studentCode, String displayName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByStudentCode(studentCode)) {
            throw new RuntimeException("Student code already registered");
        }

        String passwordHash = passwordEncoder.encode(password);
        User user = new User(studentCode, email, passwordHash, displayName, LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = UUID.randomUUID().toString();
        Session session = new Session(token, user.getStudentCode(), LocalDateTime.now(), LocalDateTime.now().plusHours(24));
        sessionRepository.save(session);
        return token;
    }

    public void logout(String token) {
        sessionRepository.deleteByToken(token);
    }

    public Optional<User> getUserFromToken(String token) {
        Optional<Session> sessionOpt = sessionRepository.findByToken(token);
        if (sessionOpt.isEmpty()) {
            return Optional.empty();
        }

        Session session = sessionOpt.get();
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        return userRepository.findByStudentCode(session.getStudentCode());
    }

    public boolean isTokenValid(String token) {
        Optional<Session> sessionOpt = sessionRepository.findByToken(token);
        if (sessionOpt.isEmpty()) {
            return false;
        }
        return sessionOpt.get().getExpiresAt().isAfter(LocalDateTime.now());
    }
}