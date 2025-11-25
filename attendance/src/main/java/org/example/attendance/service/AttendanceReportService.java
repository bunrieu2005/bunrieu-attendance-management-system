package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.AttendanceRecordDTO;
import org.example.attendance.dto.EmployeeAttendanceDTO;
import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.AttendanceRepo;
import org.example.attendance.repository.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AttendanceReportService {
    private final EmployeeRepo employeeRepo;
    private final AttendanceRepo attendanceRepo;
    public EmployeeAttendanceDTO getEmployeeAttendance(Long employeeId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        return getEmployeeAttendance(employeeId, startDate, endDate);
    }
    public EmployeeAttendanceDTO getEmployeeAttendance(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        List<Attendance> attendances = attendanceRepo
                .findByEmployeeIdAndWorkDateBetweenOrderByWorkDateDesc(
                        employeeId, startDate, endDate
                );

        List<AttendanceRecordDTO> records = attendances.stream()
                .map(att -> AttendanceRecordDTO.builder()
                        .date(att.getWorkDate())
                        .checkIn(att.getCheckInAt())
                        .checkOut(att.getCheckOutAt())
                        .build())
                .collect(Collectors.toList());
        return EmployeeAttendanceDTO.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getName())
                .department(employee.getDepartment() != null ?
                        employee.getDepartment().getName() : "N/A")
                .records(records)
                .build();
    }
}
