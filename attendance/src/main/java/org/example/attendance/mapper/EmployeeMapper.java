package org.example.attendance.mapper;

import org.example.attendance.dto.EmployeeDTO;
import org.example.attendance.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {
    public static EmployeeDTO toDTO(Employee employee) {

        if (employee == null) {
            return null;
        }
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDob(employee.getDob());
        dto.setGender(employee.getGender());
        dto.setRole(employee.getRole());
        dto.setStatus(employee.getStatus());
        dto.setHireDate(employee.getHireDate());
        dto.setImage(employee.getImage());
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
            EmployeeDTO.DepartmentInfo deptInfo = new EmployeeDTO.DepartmentInfo();
            deptInfo.setId(employee.getDepartment().getId());
            deptInfo.setName(employee.getDepartment().getName());
            deptInfo.setDescription(employee.getDepartment().getDescription());

            dto.setDepartment(deptInfo);
        }
        return dto;
    }
    public static List<EmployeeDTO> toDTOList(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }
    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        if (dto.getId() != null) {
            employee.setId(dto.getId());
        }
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setDob(dto.getDob());
        employee.setGender(dto.getGender());
        employee.setRole(dto.getRole());
        employee.setStatus(dto.getStatus());
        employee.setHireDate(dto.getHireDate());
        employee.setImage(dto.getImage());
        return employee;
    }
}