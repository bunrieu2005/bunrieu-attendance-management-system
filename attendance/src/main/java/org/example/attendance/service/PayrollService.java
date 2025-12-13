package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.entity.*;
import org.example.attendance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final SalaryDetailRepo salaryDetailRepo;
    private final SystemConfigRepo systemConfigRepo;
    private final PayslipRepo payslipRepo;
    private final EmployeeRepo employeeRepo;
    private final AttendanceRepo attendanceRepo;

    @Transactional

    public Payslip calculateSalary(Long employeeId, LocalDate startDate, LocalDate endDate) {


        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên ID: " + employeeId));
        SalaryDetail salaryDetail = salaryDetailRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Nhân viên chưa được cấu hình lương"));

        long totalMinutes = attendanceRepo.sumTotalMinutesInMonth(employeeId, startDate, endDate);
        long totalLateTimes = attendanceRepo.countLateTimesInMonth(employeeId, startDate, endDate);

        double totalHours = Math.round((totalMinutes / 60.0) * 100.0) / 100.0;
        double hourlyRate = salaryDetail.getBaseSalary();


        double grossSalary = (totalHours * hourlyRate) + salaryDetail.getAllowance();

        double penaltyPerTime = Double.parseDouble(getConfig("LATE_PENALTY_PER_TIME", "50000"));
        double deduction = totalLateTimes * penaltyPerTime;

        double bonus = 0;
        double finalSalary = grossSalary - deduction + bonus;
        if (finalSalary < 0) finalSalary = 0;


        Payslip payslip = payslipRepo.findByEmployeeIdAndStartDateAndEndDate(employeeId, startDate, endDate)
                .orElse(new Payslip());

        payslip.setEmployee(emp);


        payslip.setStartDate(startDate);
        payslip.setEndDate(endDate);

        payslip.setTotalWorkDays(totalHours);
        payslip.setTotalLateMinutes((double) totalLateTimes);

        payslip.setGrossSalary((double) Math.round(grossSalary));
        payslip.setDeduction(deduction);
        payslip.setBonus(bonus);
        payslip.setRealSalary((double) Math.round(finalSalary));

        if (payslip.getStatus() == null) {
            payslip.setStatus("DRAFT");
        }

        return payslipRepo.save(payslip);
    }

    private String getConfig(String key, String defaultValue) {
        return systemConfigRepo.findById(key)
                .map(SystemConfig::getValue)
                .orElse(defaultValue);
    }
}