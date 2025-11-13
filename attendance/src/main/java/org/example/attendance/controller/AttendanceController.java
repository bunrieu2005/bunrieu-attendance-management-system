package org.example.attendance.controller;

import org.example.attendance.entity.Attendance;
import org.example.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(("/api/attendances"))
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        List<Attendance> attendances = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendances);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAttendanceById(@PathVariable Long id){
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        return attendance.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getAttendanceByEmployeeId(@PathVariable Long employeeId){
        List<Attendance> list =attendanceService.getAttendanceByEmployee(employeeId);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getAttendancesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
      List<Attendance> attendances = attendanceService.getAttendanceByDate(date);
       return ResponseEntity.ok(attendances);
    }
    @PostMapping
    public ResponseEntity<?> saveAttendance(@RequestBody Attendance attendance){
        attendanceService.saveAttendance(attendance);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendanceById(@PathVariable Long id) {
        attendanceService.deleteAttendanceById(id);
        return ResponseEntity.ok("Deleted attendance id = " + id);
    }
}
