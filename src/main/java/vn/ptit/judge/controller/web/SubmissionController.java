package vn.ptit.judge.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ptit.judge.model.Submission;
import vn.ptit.judge.model.User;
import vn.ptit.judge.repository.SubmissionRepository;
import vn.ptit.judge.service.AuthService;

import java.util.*;

@Controller
public class SubmissionController {

    private static final Map<String, String> EXERCISE_NAMES = new LinkedHashMap<>();

    static {
        EXERCISE_NAMES.put("4siaIVgn", "Tính tổng dãy số nguyên");
        EXERCISE_NAMES.put("oPW4mpqm", "Sắp xếp từ alphabet");
        EXERCISE_NAMES.put("2ucfcULf", "Tính giá cuối sản phẩm");
        EXERCISE_NAMES.put("kpFm4QvE", "Tính tổng (gRPC)");
        EXERCISE_NAMES.put("0VdN4dph", "Sắp xếp từ (gRPC)");
        EXERCISE_NAMES.put("1zog4SNL", "Giá cuối (gRPC)");
        EXERCISE_NAMES.put("yyhHZUpt", "Tính tổng (SOAP)");
        EXERCISE_NAMES.put("qvCGMEhW", "Đảo ngược chuỗi (SOAP)");
        EXERCISE_NAMES.put("7u07dNqg", "Giá cuối (SOAP)");
    }

    private final AuthService authService;
    private final SubmissionRepository submissionRepository;

    public SubmissionController(AuthService authService, SubmissionRepository submissionRepository) {
        this.authService = authService;
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/submissions")
    public String submissions(HttpServletRequest request, Model model) {
        String token = WebAuthController.getAuthToken(request);
        if (token == null || !authService.isTokenValid(token)) {
            return "redirect:/login";
        }

        Optional<User> userOpt = authService.getUserFromToken(token);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        String studentCode = userOpt.get().getStudentCode();
        String displayName = userOpt.get().getDisplayName();

        List<Submission> submissions = submissionRepository.findByStudentCode(studentCode);
        submissions.sort((a, b) -> {
            if (a.getTimestamp() == null || b.getTimestamp() == null) return 0;
            return b.getTimestamp().compareTo(a.getTimestamp());
        });

        model.addAttribute("loggedIn", true);
        model.addAttribute("displayName", displayName);
        model.addAttribute("studentCode", studentCode);
        model.addAttribute("submissions", submissions);
        model.addAttribute("exerciseNames", EXERCISE_NAMES);

        return "submissions";
    }
}