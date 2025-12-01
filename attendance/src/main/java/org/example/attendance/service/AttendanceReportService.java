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
import java.time.Duration;
import java.util.*;
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
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        List<Attendance> attendances = attendanceRepo
                .findByEmployeeIdAndWorkDateBetweenOrderByWorkDateDesc(
                        employeeId, startDate, endDate
                );
        List<AttendanceRecordDTO> records = attendances.stream()
                .map(this::toRecordDTO)
                .collect(Collectors.toList());

        Aggregates aggregates = computeAggregates(attendances);

        return EmployeeAttendanceDTO.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getName())
                .departmentName(employee.getDepartment() != null ?
                        employee.getDepartment().getName() : "N/A")
                .records(records)
                .totalMinutes(aggregates.totalMinutes)
                .totalHours(aggregates.totalMinutes / 60.0)
                .lateDays(aggregates.lateDays)
                .build();
    }
    public Map<Long, EmployeeAttendanceDTO> getMultipleEmployeesAttendance(
            List<Long> employeeIds) {
        Map<Long, EmployeeAttendanceDTO> result = new HashMap<>();

        for (Long employeeId : employeeIds) {
            try {
                Employee employee = employeeRepo.findById(employeeId)
                        .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

                List<Attendance> attendances = attendanceRepo
                        .findByEmployeeIdOrderByWorkDateDesc(employeeId);

                List<AttendanceRecordDTO> records = attendances.stream()
                        .map(this::toRecordDTO)
                        .collect(Collectors.toList());

                Aggregates aggregates = computeAggregates(attendances);

                EmployeeAttendanceDTO dto = EmployeeAttendanceDTO.builder()
                        .employeeId(employee.getId())
                        .employeeName(employee.getName())
                        .departmentName(employee.getDepartment() != null ?
                                employee.getDepartment().getName() : "N/A")
                        .records(records)
                        .totalMinutes(aggregates.totalMinutes)
                        .totalHours(aggregates.totalMinutes / 60.0)
                        .lateDays(aggregates.lateDays)
                        .build();

                result.put(employeeId, dto);

            } catch (Exception e) {
                System.err.println("Error getting attendance for employee " +
                        employeeId + ": " + e.getMessage());
            }
        }
        return result;
    }
    public Map<Long, EmployeeAttendanceDTO> getMultipleEmployeesAttendance(
            List<Long> employeeIds,
            LocalDate startDate,
            LocalDate endDate) {

        Map<Long, EmployeeAttendanceDTO> result = new HashMap<>();

        for (Long employeeId : employeeIds) {
            try {
                EmployeeAttendanceDTO dto = getEmployeeAttendance(
                        employeeId, startDate, endDate);
                result.put(employeeId, dto);
            } catch (Exception e) {
                System.err.println("Error getting attendance for employee " +
                        employeeId + ": " + e.getMessage());
            }
        }
        return result;
    }
    private AttendanceRecordDTO toRecordDTO(Attendance attendance) {
        return AttendanceRecordDTO.builder()
                .date(attendance.getWorkDate())
                .checkIn(attendance.getCheckInAt())
                .checkOut(attendance.getCheckOutAt())
                .build();
    }

    private Aggregates computeAggregates(List<Attendance> attendances) {
        int totalMinutes = 0;
        int lateDays = 0;
        for (Attendance a : attendances) {
            if (a.getLateFlag() > 0) {
                lateDays += 1;
            }

            Integer recMinutes = a.getTotalMinutes();
            if (recMinutes == null) {
                if (a.getCheckInAt() != null && a.getCheckOutAt() != null &&
                        a.getCheckOutAt().isAfter(a.getCheckInAt())) {
                    long mins = Duration.between(a.getCheckInAt(), a.getCheckOutAt()).toMinutes();
                    if (mins > 0 && mins < Integer.MAX_VALUE) {
                        recMinutes = (int) mins;
                    }
                }
            }
            if (recMinutes != null && recMinutes > 0) {
                totalMinutes += recMinutes;
            }
        }
        return new Aggregates(totalMinutes, lateDays);
    }

    private record Aggregates(int totalMinutes, int lateDays) {}
}