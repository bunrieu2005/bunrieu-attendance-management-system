package org.example.attendance.mapper;

import org.example.attendance.dto.LeaveRequestDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.entity.LeaveRequest;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestMapper {

//entity -> dto
    public LeaveRequestDTO toDTO(LeaveRequest entity) {
        if (entity == null) return null;

        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setReason(entity.getReason());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());


        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
            dto.setEmployeeName(entity.getEmployee().getName());
            dto.setImage(entity.getEmployee().getImage());

            if (entity.getEmployee().getDepartment() != null) {
                dto.setDepartmentName(entity.getEmployee().getDepartment().getName());
            } else {
                dto.setDepartmentName("N/A");
            }
        }
        return dto;
    }

   //dto -> entity
    public LeaveRequest toEntity(LeaveRequestDTO dto) {
        if (dto == null) return null;

        LeaveRequest entity = new LeaveRequest();
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setReason(dto.getReason());
        entity.setType(dto.getType());
        return entity;
    }
}