package org.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor
public class EmployeeDTO {
        private Long id;
        private String name;
        private LocalDate dob;
        private String role;
}
