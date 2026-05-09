package vn.ptit.judge.controller.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ptit.judge.model.User;
import vn.ptit.judge.service.AuthService;

import java.util.Optional;

@Controller
public class WebAuthController {

    private final AuthService authService;

    public WebAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        String token = getAuthToken(request);
        if (token != null && authService.isTokenValid(token)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        try {
            String token = authService.login(email, password);
            Cookie cookie = new Cookie("authToken", token);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(HttpServletRequest request, Model model) {
        String token = getAuthToken(request);
        if (token != null && authService.isTokenValid(token)) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String displayName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String studentCode,
                           HttpServletResponse response,
                           Model model) {
        try {
            authService.register(email, password, studentCode, displayName);
            String token = authService.login(email, password);
            Cookie cookie = new Cookie("authToken", token);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("displayName", displayName);
            model.addAttribute("email", email);
            model.addAttribute("studentCode", studentCode);
            return "register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String token = getAuthToken(request);
        if (token != null) {
            try {
                authService.logout(token);
            } catch (Exception ignored) {
            }
        }
        Cookie cookie = new Cookie("authToken", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }

    static String getAuthToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    if (value != null && !value.isEmpty()) {
                        return value;
                    }
                }
            }
        }
        String header = request.getHeader("X-Auth-Token");
        if (header != null && !header.isEmpty()) {
            return header;
        }
        return null;
    }
}