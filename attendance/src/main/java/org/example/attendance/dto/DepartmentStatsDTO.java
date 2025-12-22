package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentStatsDTO {
    private String departmentName;
    private Long totalEmployees;
    private Double totalHours;
    private Long totalLate;

    public DepartmentStatsDTO(String departmentName, Long totalEmployees, int totalHours, long totalLate) {
        this.departmentName = departmentName;
        this.totalEmployees = totalEmployees;
        this.totalHours = (double) totalHours;
        this.totalLate = totalLate;
    }


}