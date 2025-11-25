package org.example.attendance.mapper;

import org.example.attendance.dto.EmployeeDTO;
import org.example.attendance.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDob(employee.getDob());
        dto.setRole(employee.getRole());
        dto.setGender(employee.getGender());
        dto.setHireDate(employee.getHireDate());
        dto.setDepartment(employee.getDepartment());
        dto.setImage(employee.getImage());
        return dto;
    }

    public static List<EmployeeDTO> toDTOList(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }
}