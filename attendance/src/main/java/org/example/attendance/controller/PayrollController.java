package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.PayslipDTO;
import org.example.attendance.entity.Payslip;
import org.example.attendance.mapper.PayslipMapper;
import org.example.attendance.repository.PayslipRepo;
import org.example.attendance.service.PaymentService;
import org.example.attendance.service.PayrollService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class PayrollController {

    private final PayrollService payrollService;
    private final PaymentService paymentService;
    private final PayslipRepo payslipRepo;
    private final PayslipMapper payslipMapper;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateSalary(
            @RequestParam Long empId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Payslip payslipEntity = payrollService.calculateSalary(empId, startDate, endDate);
            PayslipDTO payslipDTO = payslipMapper.toDTO(payslipEntity);
            return ResponseEntity.ok(payslipDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/history/{empId}")
    public ResponseEntity<List<PayslipDTO>> getPayslipHistory(@PathVariable Long empId) {
        List<Payslip> listEntity = payslipRepo.findByEmployeeIdOrderByStartDateDesc(empId);
        List<PayslipDTO> listDTO = listEntity.stream()
                .map(payslipMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }
    @PostMapping("/pay/{payslipId}")
    public ResponseEntity<?> paySalary(@PathVariable Long payslipId) {
        try {
            String result = paymentService.paySalary(payslipId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}