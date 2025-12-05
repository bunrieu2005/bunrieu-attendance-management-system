package org.example.attendance.dto;

import lombok.Data;
import org.example.attendance.enums.LeaveType;
import org.example.attendance.enums.LeaveStatus;
import java.time.LocalDate;

@Data
public class LeaveRequestDTO {
    //input: user create leave request
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveType type;

    // output: admin view
    private Long id;
    private String employeeName;
    private String departmentName;
    private LeaveStatus status;
    private String image;
}