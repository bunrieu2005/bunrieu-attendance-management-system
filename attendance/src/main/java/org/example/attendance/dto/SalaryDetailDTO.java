package org.example.attendance.dto;

import lombok.Data;

@Data
public class SalaryDetailDTO {
    private Long employeeId;
    private String employeeName;
    private String departmentName;
    private Double baseSalary;
    private Double allowance;
    private String bankName;
    private String bankAccountNumber;
}