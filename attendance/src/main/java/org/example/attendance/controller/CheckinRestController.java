package org.example.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.attendance.dto.ReportDTO;
import org.example.attendance.entity.Attendance;
import org.example.attendance.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendances")
public class CheckinRestController {
    @Autowired
    private CheckinService checkinService;
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<?> checkIn(@PathVariable Long employeeId,
                                     HttpServletRequest request,
                                     @RequestParam(defaultValue = "QR_IP") String method) {
        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .orElse(request.getRemoteAddr());
        Attendance result = checkinService.checkIn(employeeId, ip, method);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<?> checkOut(@PathVariable Long employeeId) {
        Attendance result = checkinService.checkOut(employeeId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/report")
    public ResponseEntity<?> report(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        from = LocalDate.parse(from.toString().trim());
        to = LocalDate.parse(to.toString().trim());
        ReportDTO report = checkinService.reportMonthly(employeeId, from, to);
        return ResponseEntity.ok(report);
    }


}
