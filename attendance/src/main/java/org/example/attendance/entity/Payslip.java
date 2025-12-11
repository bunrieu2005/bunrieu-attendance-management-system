package org.example.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="payslips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="month")
    private int month;
    @Column(name="year")
    private int year;


    @Column(name ="total_work_days")
    private double totalWorkDays;
    @Column(name="total_late_minutes")
    private double totalLateMinutes;

    private Double grossSalary;//1
    private Double deduction;   //2
    private Double bonus;      //3

    private Double realSalary;  //1+2+3
    private String status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}