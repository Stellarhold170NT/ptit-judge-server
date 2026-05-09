package vn.ptit.judge.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ptit.judge.model.User;
import vn.ptit.judge.service.AuthService;

import java.util.Optional;

@Controller
public class DashboardController {

    private final AuthService authService;

    public DashboardController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String dashboard(HttpServletRequest request, Model model) {
        String token = WebAuthController.getAuthToken(request);
        boolean loggedIn = false;
        String displayName = null;
        String studentCode = null;

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

        return "dashboard";
    }
}