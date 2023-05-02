package com.example.shppyad23.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @Autowired
    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user, HttpServletResponse response) {
        System.out.println("trying register");
        if (userService.createUser(user)) {
            System.out.println("auth");
            session.setAttribute("authenticated_user", user.getUsername());

            return ResponseEntity
                    .ok()
                    .body("Successfully created new user");
        } else {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user, HttpServletResponse response) {
        System.out.println("trying login");
        if (userService.loginUser(user)) {
            System.out.println("auth");
            session.setAttribute("authenticated_user", user.getUsername());

            return ResponseEntity
                    .ok()
                    .body("Logged in");
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("logged out");
    }
}
