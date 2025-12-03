package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;


import org.example.attendance.dto.DepartmentStatsDTO;
import org.example.attendance.dto.EmployeeStatsSummaryDTO;
import org.example.attendance.dto.GeneralStatsDTO;
import org.example.attendance.service.StatisticalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class StatisticalController {

    private final StatisticalService statisticalService;
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentStatsDTO>> getDepartmentStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(statisticalService.getStatsByDepartment(startDate, endDate));
    }
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeStatsSummaryDTO>> getEmployeeStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(statisticalService.getStatsByEmployee(startDate, endDate));
    }
    // ...
    @GetMapping("/general")
    public ResponseEntity<GeneralStatsDTO> getGeneralStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(statisticalService.getGeneralStats(startDate, endDate));
    }
}