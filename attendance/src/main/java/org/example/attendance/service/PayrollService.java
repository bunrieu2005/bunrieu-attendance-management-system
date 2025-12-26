package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.PayslipDTO;
import org.example.attendance.entity.*;
import org.example.attendance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final SalaryDetailRepo salaryDetailRepo;
    private final SystemConfigRepo systemConfigRepo;
    private final PayslipRepo payslipRepo;
    private final EmployeeRepo employeeRepo;
    private final AttendanceRepo attendanceRepo;

    @Transactional
    public PayslipDTO calculateSalary(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("not found employeed id: " + employeeId));
        SalaryDetail salaryDetail = salaryDetailRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("employeed config salary not fonnd"));

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

        Payslip savedPayslip = payslipRepo.save(payslip);
        return toDTO(savedPayslip);
    }

    public List<PayslipDTO> getPayslipHistory(Long empId) {
        List<Payslip> history = payslipRepo.findByEmployeeId(empId);
        return history.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void paySalary(Long payslipId) {
        Payslip payslip = payslipRepo.findById(payslipId)
                .orElseThrow(() -> new RuntimeException("Payslip not found"));
        payslip.setStatus("PAID");
        payslipRepo.save(payslip);
    }

    private PayslipDTO toDTO(Payslip entity) {
        if (entity == null) return null;

        PayslipDTO dto = new PayslipDTO();

        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setTotalWorkDays(entity.getTotalWorkDays());
        dto.setTotalLateMinutes(entity.getTotalLateMinutes());
        dto.setGrossSalary(entity.getGrossSalary());
        dto.setDeduction(entity.getDeduction());
        dto.setBonus(entity.getBonus());
        dto.setRealSalary(entity.getRealSalary());
        dto.setStatus(entity.getStatus());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
            dto.setEmployeeName(entity.getEmployee().getName());

            if (entity.getEmployee().getDepartment() != null) {
                dto.setDepartmentName(entity.getEmployee().getDepartment().getName());
            } else {
                dto.setDepartmentName("N/A");
            }

            SalaryDetail config = salaryDetailRepo.findByEmployeeId(entity.getEmployee().getId())
                    .orElse(null);

            if (config != null) {
                dto.setBankName(config.getBankName());
                dto.setBankAccountNumber(config.getBankAccountNumber());
            } else {
                dto.setBankName("");
                dto.setBankAccountNumber("");
            }
        }
        return dto;
    }

    private String getConfig(String key, String defaultValue) {
        return systemConfigRepo.findById(key)
                .map(SystemConfig::getValue)
                .orElse(defaultValue);
    }
}