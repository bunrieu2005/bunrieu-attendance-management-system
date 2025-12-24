package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.BankRequest;
import org.example.attendance.dto.BankResponse;
import org.example.attendance.entity.Payslip;
import org.example.attendance.entity.SalaryDetail;
import org.example.attendance.repository.PayslipRepo;
import org.example.attendance.repository.SalaryDetailRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service @RequiredArgsConstructor
public class PaymentService {

    private final PayslipRepo payslipRepo;
    private final SalaryDetailRepo salaryDetailRepo;

    // MockBankController
    private final String BANK_API_URL = "http://localhost:8080/api/mock-bank/transfer";

    @Transactional
    public String paySalary(Long payslipId) {

        Payslip payslip = payslipRepo.findById(payslipId)
                .orElseThrow(() -> new RuntimeException("ticked not found ID: " + payslipId));

        if ("PAID".equals(payslip.getStatus())) {
            return "this paycheck has already been paid!";
        }
        Long employeeId = payslip.getEmployee().getId();
        SalaryDetail salaryDetail = salaryDetailRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("this employee hasn't configured the bank account "));

        String bankAccount = salaryDetail.getBankAccountNumber();
        if (bankAccount == null || bankAccount.isEmpty()) {
            throw new RuntimeException("thee bank account number is currently blank");
        }


        RestTemplate restTemplate = new RestTemplate();

        BankRequest req = new BankRequest(
                bankAccount,
                payslip.getRealSalary(),
                "HUSC THANH TOAN LUONG"
        );

        try {
            BankResponse res = restTemplate.postForObject(BANK_API_URL, req, BankResponse.class);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                payslip.setStatus("PAID");
                payslipRepo.save(payslip);
                return "Payment successful ,  Transaction ID: " + res.getTransactionId();
            } else {
                return "The bank refused: " + (res != null ? res.getMessage() : "erro");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "errrou connet: " + e.getMessage();
        }
    }
}