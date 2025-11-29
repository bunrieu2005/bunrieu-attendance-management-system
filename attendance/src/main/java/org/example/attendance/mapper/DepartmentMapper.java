package org.example.attendance.mapper;

import org.example.attendance.dto.DepartmentDTO;
import org.example.attendance.entity.Department;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentMapper {
    public static DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(String.valueOf(department.getId()));
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        return dto;
    }
    public  static  List<DepartmentDTO> toDTOList(List<Department> departments) {
        return departments.stream()
                .map(DepartmentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
