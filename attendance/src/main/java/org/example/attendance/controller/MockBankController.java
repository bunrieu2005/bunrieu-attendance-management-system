package org.example.attendance.controller;

import org.example.attendance.dto.BankRequest;
import org.example.attendance.dto.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/mock-bank")
public class MockBankController {

    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> transferMoney(@RequestBody BankRequest request) throws InterruptedException {
        // 2s
        System.out.println("MockBank: currently trading for the account " + request.getAccountNumber());
        Thread.sleep(2000);
        String bankTxnId = "BANK_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return ResponseEntity.ok(new BankResponse("SUCCESS", "successful transaction", bankTxnId));
    }
}