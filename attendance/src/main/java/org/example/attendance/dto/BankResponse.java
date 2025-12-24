package org.example.attendance.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    private String status;
    private String message;
    private String transactionId;
}
