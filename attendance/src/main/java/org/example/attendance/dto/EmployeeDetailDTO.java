package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private String gender;
    private String role;
    private String status;
    private LocalDate hireDate;
    private String image;

    private DepartmentInfo department;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentInfo {
        private Long id;
        private String name;
        private String description;
    }
}
