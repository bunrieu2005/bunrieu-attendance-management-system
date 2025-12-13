package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.SalaryDetailDTO;
import org.example.attendance.service.SalaryDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary-details")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class SalaryDetailController {

    private final SalaryDetailService salaryDetailService;

    @GetMapping
    public ResponseEntity<List<SalaryDetailDTO>> getAll() {
        return ResponseEntity.ok(salaryDetailService.getAllSalaryDetails());
    }

    @PostMapping
    public ResponseEntity<?> updateSalary(@RequestBody SalaryDetailDTO dto) {
        return ResponseEntity.ok(salaryDetailService.updateSalary(dto));
    }
}