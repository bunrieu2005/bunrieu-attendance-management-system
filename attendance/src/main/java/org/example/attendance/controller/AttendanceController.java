package org.example.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.EmployeeAttendanceDTO;
import org.example.attendance.dto.FaceVerifyResponse;
import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.service.AttendanceService;
import org.example.attendance.service.AttendanceReportService;
import org.example.attendance.service.CheckinService;
import org.example.attendance.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendances")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AttendanceController {


    @Autowired
    private FaceRecognitionService faceService;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private CheckinService checkinService;
    private final AttendanceService attendanceService;
    private final AttendanceReportService reportService;
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        List<Attendance> attendances = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAttendanceById(@PathVariable Long id) {
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        return attendance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getAttendancesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Attendance> attendances = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getAttendanceByEmployeeId(@PathVariable Long employeeId) {
        List<Attendance> list = attendanceService.getAttendanceByEmployee(employeeId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> saveAttendance(@RequestBody Attendance attendance) {
        Attendance saved = attendanceService.saveAttendance(attendance);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendanceById(@PathVariable Long id) {
        attendanceService.deleteAttendanceById(id);
        return ResponseEntity.ok(Map.of("message", "Deleted attendance id = " + id));
    }
    @PostMapping("/employees")
    public ResponseEntity<Map<Long, EmployeeAttendanceDTO>> getMultipleEmployeesAttendance(
            @RequestBody Map<String, List<Long>> request) {

        List<Long> employeeIds = request.get("employeeIds");

        if (employeeIds == null || employeeIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Map<Long, EmployeeAttendanceDTO> result =
                reportService.getMultipleEmployeesAttendance(employeeIds);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/employees/range")
    public ResponseEntity<Map<Long, EmployeeAttendanceDTO>> getMultipleEmployeesAttendanceByRange(
            @RequestBody Map<String, List<Long>> request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Long> employeeIds = request.get("employeeIds");

        if (employeeIds == null || employeeIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Map<Long, EmployeeAttendanceDTO> result =
                reportService.getMultipleEmployeesAttendance(employeeIds, startDate, endDate);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/employee/{employeeId}/records")
    public ResponseEntity<EmployeeAttendanceDTO> getSingleEmployeeAttendance(
            @PathVariable Long employeeId) {

        EmployeeAttendanceDTO dto = reportService.getEmployeeAttendance(employeeId);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/employee/{employeeId}/records/range")
    public ResponseEntity<EmployeeAttendanceDTO> getSingleEmployeeAttendanceByRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {   
        EmployeeAttendanceDTO dto = reportService.getEmployeeAttendance(
                employeeId, startDate, endDate);
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/check-in/face")
    public ResponseEntity<?> checkInByFace(@RequestParam("image") MultipartFile image,
                                           HttpServletRequest request) {

        Long recognizedId = null;

        try {
            FaceVerifyResponse result = faceService.verifyFace(image);

            if (!result.isSuccess()) {
                return ResponseEntity.status(401).body("Khuôn mặt không hợp lệ!");
            }

            recognizedId = Long.valueOf(result.getEmployeeId());
            String clientIp = request.getRemoteAddr();

            Attendance attendance = checkinService.checkIn(recognizedId, clientIp, "FACE");
            return ResponseEntity.ok(attendance);

        } catch (IllegalStateException e) {
            Map<String, String> response = new HashMap<>();


            if (recognizedId != null) {
                Employee emp = employeeRepo.findById(recognizedId).orElse(null);
                String name = (emp != null) ? emp.getName() : "Nhân viên";
                response.put("employeeName", name);
            } else {
                response.put("employeeName", "Bạn");
            }

            response.put("message", "Hôm nay đã check-in rồi");
            return ResponseEntity.status(409).body(response);

        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}
