package com.example.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpSession;

import com.example.travel.model.User;
import com.example.travel.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            if (userRepository.existsByEmail(email)) {
                return "redirect:/register.html?error=email_exists";
            }
            if (userRepository.existsByUsername(username)) {
                return "redirect:/register.html?error=username_exists";
            }

            User newUser = new User(username.trim(), email.trim().toLowerCase(), passwordEncoder.encode(password));
            userRepository.save(newUser);
            session.setAttribute("userEmail", newUser.getEmail());
            session.setAttribute("username", newUser.getUsername());
            return "redirect:/index.html?registered=success";
        } catch (Exception e) {
            return "redirect:/register.html?error=true";
        }
    }

    @PostMapping("/api/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByEmail(email.trim().toLowerCase());
        
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("username", user.getUsername());
            return "redirect:/index.html?login=success";
        } else {
            return "redirect:/register.html?error=invalid#login";
        }
    }

    @PostMapping("/api/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/register.html?logout=true#login";
    }
}
