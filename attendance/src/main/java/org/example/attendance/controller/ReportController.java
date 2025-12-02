package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.EmployeeAttendanceDTO;
import org.example.attendance.service.AttendanceReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ReportController {
    private final AttendanceReportService reportService;
    @GetMapping("/attendance/{employeeId}")
    public ResponseEntity<EmployeeAttendanceDTO> getAttendance(
            @PathVariable Long employeeId) {
        EmployeeAttendanceDTO ReportDTO = reportService.getEmployeeAttendance(employeeId);
        return ResponseEntity.ok(ReportDTO);
    }
        @GetMapping("/attendance/{employeeId}/range")
        public ResponseEntity<EmployeeAttendanceDTO> getAttendanceByRange(
                @PathVariable Long employeeId,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
            EmployeeAttendanceDTO ReportDTO = reportService.getEmployeeAttendance(
                    employeeId, startDate, endDate
            );
            return ResponseEntity.ok(ReportDTO);
        }
}