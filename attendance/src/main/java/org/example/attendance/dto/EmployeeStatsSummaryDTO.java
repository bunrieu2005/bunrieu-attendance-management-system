package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeStatsSummaryDTO {
    //cách Constructor: Java chấp nhận mọi kiểu Database trả về, rồi tự convert lại sau.cách 2: cash : lỗi
    private Long employeeId;
    private String employeeName;
    private String departmentName;
    private Double totalHours;
    private Long totalWorkDays;
    private Long lateCount;

    public EmployeeStatsSummaryDTO(long employeeId,
                                   String employeeName,
                                   String departmentName,
                                   int totalHours,
                                   Long totalWorkDays,
                                   long lateCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.totalHours = (double) totalHours;
        this.totalWorkDays = totalWorkDays;
        this.lateCount = lateCount;
    }

    public EmployeeStatsSummaryDTO(Long employeeId,
                                   String employeeName,
                                   String departmentName,
                                   Integer totalHours,
                                   Long totalWorkDays,
                                   Long lateCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.totalHours = totalHours == null ? 0.0 : totalHours.doubleValue();
        this.totalWorkDays = totalWorkDays;
        this.lateCount = lateCount;
    }
}