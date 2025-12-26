package org.example.attendance.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskDTO {
    private String title;
    private String content;
    private LocalDate deadline;

    private String targetType;
    private Long targetId;
}
