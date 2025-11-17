package org.example.attendance.service;

import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.AttendanceRepo;
import org.example.attendance.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
@Service
public class CheckinService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    public Attendance checkIn(Long empId, String ip, String method) {
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new IllegalArgumentException("employee not found"));
        LocalDate today = LocalDate.now();
        if (attendanceRepo.findByEmployeeIdAndWorkDate(empId, today).isPresent()) {
            throw new IllegalStateException("already checked in today");
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
                .orElseThrow(() -> new IllegalStateException("no active check-in record"));
        a.setCheckOutAt(LocalDateTime.now());
        // calculate total work minute
        long minutes = Duration.between(a.getCheckInAt(), a.getCheckOutAt()).toMinutes();
        a.setTotalMinutes((int) minutes);
       // leave early
        if (a.getCheckOutAt().toLocalTime().isBefore(LocalTime.of(17, 0))) {
            a.setEarlyLeaveFlag(1);
        } else {
            a.setEarlyLeaveFlag(0);
        }
        return attendanceRepo.save(a); //save db
    }

}
