package org.example.attendance.mapper;

import org.example.attendance.dto.SalaryDetailDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.entity.SalaryDetail;

public class SalaryDetailMapper {

    private SalaryDetailMapper() {
    }

    public static SalaryDetailDTO toDTO(Employee employee, SalaryDetail salary) {
        if (employee == null) {
            return null;
        }
        SalaryDetailDTO dto = new SalaryDetailDTO();
        dto.setEmployeeId(employee.getId());
        dto.setEmployeeName(employee.getName());
        dto.setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : "N/A");

        if (salary != null) {
            dto.setBaseSalary(salary.getBaseSalary() != null ? salary.getBaseSalary() : 0.0);
            dto.setAllowance(salary.getAllowance() != null ? salary.getAllowance() : 0.0);
            dto.setBankName(salary.getBankName() != null ? salary.getBankName() : "");
            dto.setBankAccountNumber(salary.getBankAccountNumber() != null ? salary.getBankAccountNumber() : "");
        } else {
            dto.setBaseSalary(0.0);
            dto.setAllowance(0.0);
            dto.setBankName("");
            dto.setBankAccountNumber("");
        }

        return dto;
    }

    public static void updateEntityFromDTO(SalaryDetailDTO dto, SalaryDetail salary) {
        if (dto == null || salary == null) return;
        salary.setBaseSalary(dto.getBaseSalary());
        salary.setAllowance(dto.getAllowance());
        salary.setBankName(dto.getBankName());
        salary.setBankAccountNumber(dto.getBankAccountNumber());
    }
}
