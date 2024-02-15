package com.example.springsecuritytest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class AppController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        model.addAttribute("username", username);

        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin";
        } else if (request.isUserInRole("USER")) {
            return "redirect:/user";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "user";
    }

    @GetMapping("/all")
    public String allRolesPage() {
        return "all";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }
}
