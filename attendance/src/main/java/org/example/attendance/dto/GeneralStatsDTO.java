
        package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralStatsDTO {

    private Long totalEmployees;
    private Long newEmployees;

    private Long adminCount;
    private Long userCount;
    private Long maleCount;
    private Long femaleCount;

    private Long totalLateOccurrences;
    private Long employeesWithLate;
}