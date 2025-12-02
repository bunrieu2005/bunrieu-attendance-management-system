package org.example.attendance.service;

import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.AttendanceRepo;
import org.example.attendance.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Arrays;
import java.util.List;

@Service
public class CheckinService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    private static final List<String> ALLOWED_IPS = Arrays.asList(
            "192.168.1.100",
            "0:0:0:0:0:0:0:1"
    );
    @Transactional// delete complete->create
        public Attendance checkIn(Long empId, String ip, String method) {
            if (!ALLOWED_IPS.contains(ip)) {
                throw new SecurityException("no accept  check in at ip : " + ip);
            }
Employee emp = employeeRepo.findById(empId) //select * from employees where id =5
        .orElseThrow(() -> new IllegalArgumentException("employee not found :{400 bad request}"));
LocalDate today = LocalDate.now();


//cách 3 :// --- ĐOẠN MỚI THÊM: DỌN DẸP RIÊNG CHO NV NÀY TRƯỚC KHI CHECKIN ---
//        // Tìm xem ông này có bản ghi nào chưa checkout mà KHÔNG PHẢI HÔM NAY không?
//        List<Attendance> oldRecords = attendanceRepo.findByEmployeeIdAndCheckOutAtIsNull(empId);
//
//        for (Attendance old : oldRecords) {
//            if (!old.getWorkDate().isEqual(today)) {
//                attendanceRepo.delete(old);
//                System.out.println("delete complete check-in ole employees: " + empId);
//            }
//        }


        if (attendanceRepo.findByEmployeeIdAndWorkDate(empId, today).isPresent()) { //select * from attendanc WHERE employeeid = 5 AND workdate = '2025-11-19'
        throw new IllegalStateException("already checked in today ,409 conflict");
        }
Attendance a = new Attendance();
        a.setEmployee(emp);
        a.setWorkDate(today);
        a.setCheckInAt(LocalDateTime.now());
        a.setMethod(method);
        a.setIp(ip);
        if (a.getCheckInAt().toLocalTime().isAfter(LocalTime.of(8, 15))) { //gio quy dinh : 8h45 : 8h after< 8h15-->  set 1
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
            .orElseThrow(() -> new IllegalStateException("no active check-in record,400 bad rq"));
    a.setCheckOutAt(LocalDateTime.now());
    // calculate total work minute
    long minutes = Duration.between(a.getCheckInAt(), a.getCheckOutAt()).toMinutes(); //8h15----convert(minit 1h* 60: )----9h15
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
