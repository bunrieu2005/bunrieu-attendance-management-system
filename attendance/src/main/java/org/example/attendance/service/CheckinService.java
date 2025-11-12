package org.example.attendance.service;

import org.example.attendance.dto.ReportDTO;
import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.AttendanceRepo;
import org.example.attendance.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class CheckinService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    public Attendance checkIn(Long empId, String ip, String method) {
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        LocalDate today = LocalDate.now();

        if (attendanceRepo.findByEmployeeIdAndWorkDate(empId, today).isPresent()) {
            throw new IllegalStateException("Already checked in today");
        }

        Attendance a = new Attendance();
        a.setEmployee(emp);
        a.setWorkDate(today);
        a.setCheckInAt(LocalDateTime.now());
        a.setMethod(method);
        a.setIp(ip);

        if (a.getCheckInAt().toLocalTime().isAfter(LocalTime.of(8, 15))) {
            a.setLateFlag(1);
        } else {
            a.setLateFlag(0);
        }

        a.setEarlyLeaveFlag(0);
        return attendanceRepo.save(a);
    }


    public Attendance checkOut(Long empId) {
        Attendance a = attendanceRepo
                .findTopByEmployeeIdAndCheckOutAtIsNullOrderByCheckInAtDesc(empId)
                .orElseThrow(() -> new IllegalStateException("No active check-in record"));

        a.setCheckOutAt(LocalDateTime.now());

        // Tính tổng phút làm việc
        long minutes = Duration.between(a.getCheckInAt(), a.getCheckOutAt()).toMinutes();
        a.setTotalMinutes((int) minutes);

        // Xác định về sớm
        if (a.getCheckOutAt().toLocalTime().isBefore(LocalTime.of(17, 0))) {
            a.setEarlyLeaveFlag(1);
        } else {
            a.setEarlyLeaveFlag(0);
        }

        return attendanceRepo.save(a);
    }


    public List<Attendance> report(Long empId, LocalDate from, LocalDate to) {
        return attendanceRepo.findRange(empId, from, to);
    }
    public ReportDTO reportMonthly(Long empId, LocalDate from, LocalDate to) {
        List<Attendance> records = attendanceRepo.findRange(empId, from, to);
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        int totalDays = records.size();
        int totalMinutes = 0;
        int lateCount = 0;
        int earlyLeaveCount = 0;

        for (Attendance a : records) {
            if (a.getTotalMinutes() != null) {
                totalMinutes += a.getTotalMinutes();
            }
            if (a.getLateFlag() == 1) {
                lateCount++;
            }
            if (a.getEarlyLeaveFlag() == 1) {
                earlyLeaveCount++;
            }
        }
      ReportDTO dto = new ReportDTO();
        dto.setEmployeeId(emp.getId());
        dto.setEmployeeName(emp.getName());
        dto.setFrom(from);
        dto.setTo(to);
        dto.setTotalDays(totalDays);
        dto.setWorkedMinutes(totalMinutes);
        dto.setLateCount(lateCount);
        dto.setEarlyLeaveCount(earlyLeaveCount);
        dto.setAbsentCount(0); // tạm thời 0, sau này tính bằng ngày làm - có record
        return dto;
    }

}
