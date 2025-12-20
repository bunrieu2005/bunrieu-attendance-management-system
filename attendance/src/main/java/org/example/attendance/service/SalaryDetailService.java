package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.SalaryDetailDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.entity.SalaryDetail;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.repository.SalaryDetailRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryDetailService {

    private final EmployeeRepo employeeRepo;
    private final SalaryDetailRepo salaryDetailRepo;

    public List<SalaryDetailDTO> getAllSalaryDetails() {
        List<Employee> employees = employeeRepo.findAll();

        return employees.stream().map(emp -> {
            SalaryDetail salary = salaryDetailRepo.findByEmployeeId(emp.getId()).orElse(new SalaryDetail());

            SalaryDetailDTO dto = new SalaryDetailDTO();
            dto.setEmployeeId(emp.getId());
            dto.setEmployeeName(emp.getName());
            dto.setDepartmentName(emp.getDepartment() != null ? emp.getDepartment().getName() : "N/A");
            dto.setBaseSalary(salary.getBaseSalary() != null ? salary.getBaseSalary() : 0.0);
            dto.setAllowance(salary.getAllowance() != null ? salary.getAllowance() : 0.0);
            dto.setBankName(salary.getBankName() != null ? salary.getBankName() : "");
            dto.setBankAccountNumber(salary.getBankAccountNumber() != null ? salary.getBankAccountNumber() : "");

            return dto;
        }).collect(Collectors.toList());
    }
    public SalaryDetail updateSalary(SalaryDetailDTO dto) {
        SalaryDetail salary = salaryDetailRepo.findByEmployeeId(dto.getEmployeeId())
                .orElse(new SalaryDetail());
        if(salary.getEmployee() == null) {
            Employee emp = employeeRepo.findById(dto.getEmployeeId()).orElseThrow();
            salary.setEmployee(emp);
        }

        salary.setBaseSalary(dto.getBaseSalary());
        salary.setAllowance(dto.getAllowance());
        salary.setBankName(dto.getBankName());
        salary.setBankAccountNumber(dto.getBankAccountNumber());

        return salaryDetailRepo.save(salary);
    }
}