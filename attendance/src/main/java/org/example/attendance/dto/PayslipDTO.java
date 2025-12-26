package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    private double totalWorkDays;
    private double totalLateMinutes;

    private Double grossSalary;
    private Double deduction;
    private Double bonus;
    private Double realSalary;

    private String status;

    private Long employeeId;
    private String employeeName;
    private String departmentName;

    private String bankName;
    private String bankAccountNumber;
}