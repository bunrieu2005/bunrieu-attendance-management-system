package org.example.attendance.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Department;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class EmployeeProfileDTO {
    private Long id;
    private String name;
    private String role;
    private String gender;
    private String email;
    private LocalDate hireDate;
    private LocalDate dob;
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentInfo{
           private String id;
           private String name;
           private String description;
    }
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class AttendanceInfo{
      //tong so ngay tren thang
      private long dayMonth;
      private LocalDateTime checkInAt;
      private LocalDateTime checkOutAt;
    }
}
