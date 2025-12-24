package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //
    @PostMapping("/pay/{payslipId}")
    public ResponseEntity<?> paySalary(@PathVariable Long payslipId) {
        try {
            String result = paymentService.paySalary(payslipId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("errro system");
        }
    }
}