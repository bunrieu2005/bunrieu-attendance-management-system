package org.example.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="salary_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="base_salary")
    private Double baseSalary;
    @Column(name="allowance")
    private Double allowance;

    @Column(name="bank_name")
    private String bankName;
    @Column(name="bank_account_number")
    private String bankAccountNumber;


    @OneToOne
    @JoinColumn(name = "employee_id", unique = true, nullable = false)
    private Employee employee;
}