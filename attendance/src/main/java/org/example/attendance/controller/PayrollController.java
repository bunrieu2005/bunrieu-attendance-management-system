package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.PayslipDTO;
import org.example.attendance.service.PayrollService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateSalary(
            @RequestParam Long empId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            PayslipDTO payslipDTO = payrollService.calculateSalary(empId, startDate, endDate);
            return ResponseEntity.ok(payslipDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/history/{empId}")
    public ResponseEntity<List<PayslipDTO>> getPayslipHistory(@PathVariable Long empId) {
        List<PayslipDTO> listDTO = payrollService.getPayslipHistory(empId);
        return ResponseEntity.ok(listDTO);
    }

    @PostMapping("/pay/{payslipId}")
    public ResponseEntity<?> paySalary(@PathVariable Long payslipId) {
        try {
            payrollService.paySalary(payslipId);
            return ResponseEntity.ok("SUSSCESFULLY");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}