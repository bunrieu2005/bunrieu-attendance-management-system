package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.SalaryDetailDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.entity.SalaryDetail;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.repository.SalaryDetailRepo;
import org.example.attendance.mapper.SalaryDetailMapper;
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
            SalaryDetail salary = salaryDetailRepo.findByEmployeeId(emp.getId()).orElse(null);
            return SalaryDetailMapper.toDTO(emp, salary);
        }).collect(Collectors.toList());
    }
    public SalaryDetail updateSalary(SalaryDetailDTO dto) {
        SalaryDetail salary = salaryDetailRepo.findByEmployeeId(dto.getEmployeeId())
                .orElse(new SalaryDetail());
        if(salary.getEmployee() == null) {
            Employee emp = employeeRepo.findById(dto.getEmployeeId()).orElseThrow();
            salary.setEmployee(emp);
        }

        SalaryDetailMapper.updateEntityFromDTO(dto, salary);

        return salaryDetailRepo.save(salary);
    }
}