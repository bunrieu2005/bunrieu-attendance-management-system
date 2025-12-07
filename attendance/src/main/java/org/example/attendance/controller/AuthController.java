package org.example.attendance.controller;

import org.example.attendance.dto.LoginRequest;
import org.example.attendance.dto.RegisterRequest;
import org.example.attendance.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//POST api/auth/login: {username(tan) + password(123456)
// get request ,  call Authservice
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
        /// HTTP 200 OK { token{}, email{}│
        //│   fronted   save token : localStorage.setItem('token', response.token)               │
        //│            navigate to /dashboard
    }
}
