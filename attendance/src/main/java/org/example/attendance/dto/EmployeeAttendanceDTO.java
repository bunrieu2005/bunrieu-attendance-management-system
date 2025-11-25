package org.example.attendance.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAttendanceDTO {
    private Long employeeId;
    private String employeeName;
    private String department;
    private List<AttendanceRecordDTO> records;
}