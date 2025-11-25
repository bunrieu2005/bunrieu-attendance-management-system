package org.example.attendance.controller;

import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.service.AttendanceService;
import org.example.attendance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AttendanceService attendanceService;
//all:
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        LocalDate today = LocalDate.now();
        long totalEmployees = employeeService.countAll();
        long workingToday = attendanceService.countWorkingToday(today);
        long absentToday = totalEmployees - workingToday;
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployees", totalEmployees);
        stats.put("workingToday", workingToday);
        stats.put("absentToday", absentToday);
        return ResponseEntity.ok(stats);
    }
    //employess working
    @GetMapping("/working-today")
    public ResponseEntity<List<Map<String, Object>>> getWorkingToday() {
        LocalDate today = LocalDate.now();
        List<Attendance> attendances = attendanceService.findByDateAndCheckOutIsNull(today);

        List<Map<String, Object>> result = attendances.stream()
                .map(att -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", att.getEmployee().getId());
                    map.put("employeeName", att.getEmployee().getName());
                    map.put("departmentName", att.getEmployee().getDepartment() != null
                            ? att.getEmployee().getDepartment().getName() : "N/A");
                    map.put("image", att.getEmployee().getImage());
                    map.put("checkInTime", att.getCheckInAt());

                    return map;
                })
                .collect(Collectors.toList()); // gom map tra json
        return ResponseEntity.ok(result);
    }
    //employees offline
    @GetMapping("/absent-today")
    public ResponseEntity<List<Map<String, Object>>> getAbsentToday() {
        LocalDate today = LocalDate.now();
        List<Employee> allEmployees = employeeService.getAllEmployees();
        List<Long> workedEmployeeIds = attendanceService.getAttendanceByDate(today)
                .stream()
                .map(att -> att.getEmployee().getId())
                .collect(Collectors.toList());
        List<Map<String, Object>> result = allEmployees.stream()
                .filter(emp -> !workedEmployeeIds.contains(emp.getId()))
                .map(emp -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", emp.getId());
                    map.put("employeeName", emp.getName());
                    map.put("departmentName", emp.getDepartment() != null
                            ? emp.getDepartment().getName() : "N/A");
                    map.put("image", emp.getImage());
                    map.put("reason", "Don Xin Nghi");
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}