package vn.ptit.judge.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.model.User;
import vn.ptit.judge.service.AuthService;
import vn.ptit.judge.service.ScoringService;

import java.util.*;

@Controller
public class ExerciseController {

    private static final Map<String, ExerciseInfo> EXERCISES = new LinkedHashMap<>();

    static {
        EXERCISES.put("4siaIVgn", new ExerciseInfo("Tính tổng dãy số nguyên", "REST",
                "Nhận danh sách số nguyên, tính tổng và gửi kết quả.", "data"));
        EXERCISES.put("oPW4mpqm", new ExerciseInfo("Sắp xếp từ theo thứ tự alphabet", "REST",
                "Nhận chuỗi các từ, sắp xếp theo thứ tự alphabet và gửi kết quả.", "character"));
        EXERCISES.put("2ucfcULf", new ExerciseInfo("Tính giá cuối của sản phẩm", "REST",
                "Nhận thông tin sản phẩm (giá, thuế, giảm giá), tính giá cuối và gửi kết quả.", "object"));
        EXERCISES.put("kpFm4QvE", new ExerciseInfo("Tính tổng dãy số nguyên (gRPC)", "gRPC",
                "Sử dụng gRPC client để gửi request và nhận kết quả. Không hỗ trợ trực tiếp trên trình duyệt.", null));
        EXERCISES.put("0VdN4dph", new ExerciseInfo("Sắp xếp từ theo thứ tự alphabet (gRPC)", "gRPC",
                "Sử dụng gRPC client để gửi request và nhận kết quả. Không hỗ trợ trực tiếp trên trình duyệt.", null));
        EXERCISES.put("1zog4SNL", new ExerciseInfo("Tính giá cuối sản phẩm (gRPC)", "gRPC",
                "Sử dụng gRPC client để gửi request và nhận kết quả. Không hỗ trợ trực tiếp trên trình duyệt.", null));
        EXERCISES.put("yyhHZUpt", new ExerciseInfo("Tính tổng dãy số nguyên (SOAP)", "SOAP",
                "Sử dụng SOAP client để gửi request và nhận kết quả. Không hỗ trợ trực tiếp trên trình duyệt.", null));
        EXERCISES.put("qvCGMEhW", new ExerciseInfo("Đảo ngược chuỗi (SOAP)", "SOAP",
                "Sử dụng SOAP client để gửi request và nhận kết quả. Không hỗ trợ trực tiếp trên trình duyệt.", null));
        EXERCISES.put("7u07dNqg", new ExerciseInfo("Tính giá cuối sản phẩm (SOAP)", "SOAP",
                "Sử dụng SOAP client để gửi request và nhận kết quả. Không hỗ trợ trực tiếp trên trình duyệt.", null));
    }

    private final AuthService authService;
    private final ScoringService scoringService;

    public ExerciseController(AuthService authService, ScoringService scoringService) {
        this.authService = authService;
        this.scoringService = scoringService;
    }

    @GetMapping("/exercise/{qCode}")
    public String exerciseDetail(@PathVariable String qCode, HttpServletRequest request, Model model) {
        ExerciseInfo info = EXERCISES.get(qCode);
        if (info == null) {
            return "redirect:/";
        }

        String token = WebAuthController.getAuthToken(request);
        boolean loggedIn = false;
        String studentCode = null;
        String displayName = null;

        if (token != null && authService.isTokenValid(token)) {
            Optional<User> userOpt = authService.getUserFromToken(token);
            if (userOpt.isPresent()) {
                loggedIn = true;
                displayName = userOpt.get().getDisplayName();
                studentCode = userOpt.get().getStudentCode();
            }
        }

        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("displayName", displayName);
        model.addAttribute("studentCode", studentCode);
        model.addAttribute("qCode", qCode);
        model.addAttribute("exercise", info);
        model.addAttribute("isRest", "REST".equals(info.protocol));
        model.addAttribute("isGrpc", "gRPC".equals(info.protocol));
        model.addAttribute("isSoap", "SOAP".equals(info.protocol));

        return "exercise-detail";
    }

    @PostMapping("/exercise/{qCode}/request")
    public String requestData(@PathVariable String qCode, HttpServletRequest request, Model model,
                              RedirectAttributes redirectAttributes) {
        ExerciseInfo info = EXERCISES.get(qCode);
        if (info == null || !"REST".equals(info.protocol)) {
            return "redirect:/exercise/" + qCode;
        }

        String token = WebAuthController.getAuthToken(request);
        if (token == null || !authService.isTokenValid(token)) {
            return "redirect:/login";
        }

        try {
            Map<String, Object> result = scoringService.generateExerciseData(qCode);
            String requestId = (String) result.get("requestId");
            Object data = result.get("data");

            redirectAttributes.addFlashAttribute("requestId", requestId);
            redirectAttributes.addFlashAttribute("exerciseData", data);
            redirectAttributes.addFlashAttribute("dataRequested", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lấy dữ liệu: " + e.getMessage());
        }

        return "redirect:/exercise/" + qCode;
    }

    @PostMapping("/exercise/{qCode}/submit")
    public String submitAnswer(@PathVariable String qCode,
                               @RequestParam String requestId,
                               @RequestParam String answer,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        ExerciseInfo info = EXERCISES.get(qCode);
        if (info == null || !"REST".equals(info.protocol)) {
            return "redirect:/exercise/" + qCode;
        }

        String token = WebAuthController.getAuthToken(request);
        if (token == null || !authService.isTokenValid(token)) {
            return "redirect:/login";
        }

        Optional<User> userOpt = authService.getUserFromToken(token);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        try {
            ScoringResult result = scoringService.scoreSubmission(qCode, requestId, answer);
            scoringService.recordSubmission(
                    userOpt.get().getStudentCode(), qCode, requestId,
                    answer, result.getCorrectAnswer(), result.getStatus()
            );

            redirectAttributes.addFlashAttribute("submitResult", result.getStatus());
            redirectAttributes.addFlashAttribute("submitMessage", result.getMessage());
            if (!"AC".equals(result.getStatus())) {
                redirectAttributes.addFlashAttribute("correctAnswer", result.getCorrectAnswer());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("submitResult", "RTE");
            redirectAttributes.addFlashAttribute("submitMessage", "Lỗi: " + e.getMessage());
        }

        return "redirect:/exercise/" + qCode;
    }

    static class ExerciseInfo {
        public final String title;
        public final String protocol;
        public final String description;
        public final String endpoint;

        public ExerciseInfo(String title, String protocol, String description, String endpoint) {
            this.title = title;
            this.protocol = protocol;
            this.description = description;
            this.endpoint = endpoint;
        }
    }
}