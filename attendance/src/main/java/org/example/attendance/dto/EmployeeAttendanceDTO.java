package org.example.attendance.dto;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmployeeAttendanceDTO {
    private Long employeeId;
    private String employeeName;
    private String departmentName;
    private List<AttendanceRecordDTO> records;
    private Integer totalMinutes; // tổng phút làm việc trong khoảng
    private Double totalHours;    // quy đổi giờ = totalMinutes / 60.0
    private Integer lateDays;     // số ngày đi trễ trong khoảng
}