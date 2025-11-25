package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.attendance.entity.Department;

import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor
public class EmployeeDTO {
        private Long id;
        private String name;
        private LocalDate dob;
        private String email;
        private String role;
        private String gender;
        private LocalDate hireDate;
        private Department department;
        private String image;
}

