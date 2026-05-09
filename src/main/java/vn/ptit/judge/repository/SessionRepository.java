package vn.ptit.judge.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import vn.ptit.judge.model.Session;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SessionRepository extends JsonFileRepository<Session> {

    @Autowired
    public SessionRepository(ObjectMapper objectMapper, @Value("${judge.data-dir}") String dataDir) {
        super(objectMapper, dataDir + File.separator + "sessions.json", Session.class);
    }

    public Optional<Session> findByToken(String token) {
        return findAll().stream()
                .filter(s -> token.equals(s.getToken()))
                .findFirst();
    }

    public List<Session> findByStudentCode(String studentCode) {
        return findAll().stream()
                .filter(s -> studentCode.equals(s.getStudentCode()))
                .collect(Collectors.toList());
    }

    public void deleteByToken(String token) {
        lock.lock();
        try {
            List<Session> sessions = findAll().stream()
                    .filter(s -> !token.equals(s.getToken()))
                    .collect(Collectors.toList());
            saveAll(sessions);
        } finally {
            lock.unlock();
        }
    }

    public void deleteExpired() {
        lock.lock();
        try {
            List<Session> sessions = findAll().stream()
                    .filter(s -> s.getExpiresAt().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            saveAll(sessions);
        } finally {
            lock.unlock();
        }
    }
}