package org.example.attendance.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportDTO {
    private Long employeeId;
    private String employeeName;
    private LocalDate from;
    private LocalDate to;
    private int totalDays;
    private int workedMinutes;
    private int lateCount;
    private int earlyLeaveCount;
    private int absentCount;
}
