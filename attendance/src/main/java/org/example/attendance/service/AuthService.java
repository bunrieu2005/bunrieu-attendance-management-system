package org.example.attendance.service;

import org.example.attendance.dto.LoginRequest;
import org.example.attendance.dto.LoginResponse;
import org.example.attendance.dto.RegisterRequest;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public String register(RegisterRequest request) {
        if (employeeRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("email already exists");
        }
        Employee e = new Employee();
        e.setName(request.getName());
        e.setEmail(request.getEmail());
        e.setPassword(passwordEncoder.encode(request.getPassword()));
        e.setRole("USER");
        e.setStatus("ACTIVE");
        employeeRepo.save(e);
        return "register success!";
    }
    public LoginResponse login(LoginRequest request) {
        Employee e = employeeRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found!"));

        if (!passwordEncoder.matches(request.getPassword(), e.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        String token = jwtUtils.generateToken(e.getEmail(), e.getRole());

        return new LoginResponse(e.getId(), e.getName(), e.getEmail(), e.getRole(), token);
    }
}