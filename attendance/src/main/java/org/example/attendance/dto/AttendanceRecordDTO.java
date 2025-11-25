package org.example.attendance.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttendanceRecordDTO {
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
}
