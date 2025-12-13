package org.example.attendance.mapper;

import org.example.attendance.dto.PayslipDTO;
import org.example.attendance.entity.Payslip;
import org.springframework.stereotype.Component;

@Component
public class PayslipMapper {

    public PayslipDTO toDTO(Payslip entity) {
        if (entity == null) {
            return null;
        }

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
        }

        return dto;
    }
}