package vn.ptit.judge.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import vn.ptit.judge.model.Submission;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubmissionRepository extends JsonFileRepository<Submission> {

    @Autowired
    public SubmissionRepository(ObjectMapper objectMapper, @Value("${judge.data-dir}") String dataDir) {
        super(objectMapper, dataDir + File.separator + "submissions.json", Submission.class);
    }

    public List<Submission> findByStudentCode(String studentCode) {
        return findAll().stream()
                .filter(s -> studentCode.equals(s.getStudentCode()))
                .collect(Collectors.toList());
    }

    public List<Submission> findByStudentCodeAndQCode(String studentCode, String qCode) {
        return findAll().stream()
                .filter(s -> studentCode.equals(s.getStudentCode()) && qCode.equals(s.getQCode()))
                .collect(Collectors.toList());
    }
}
