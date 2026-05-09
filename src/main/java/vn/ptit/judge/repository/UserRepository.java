package vn.ptit.judge.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import vn.ptit.judge.model.User;

import java.io.File;
import java.util.Optional;

@Repository
public class UserRepository extends JsonFileRepository<User> {

    @Autowired
    public UserRepository(ObjectMapper objectMapper, @Value("${judge.data-dir}") String dataDir) {
        super(objectMapper, dataDir + File.separator + "users.json", User.class);
    }

    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst();
    }

    public Optional<User> findByStudentCode(String studentCode) {
        return findAll().stream()
                .filter(u -> studentCode.equals(u.getStudentCode()))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean existsByStudentCode(String studentCode) {
        return findByStudentCode(studentCode).isPresent();
    }
}
