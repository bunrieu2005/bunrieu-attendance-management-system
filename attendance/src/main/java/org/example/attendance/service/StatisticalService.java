package org.example.attendance.service;

import lombok.RequiredArgsConstructor;

import org.example.attendance.dto.DepartmentStatsDTO;
import org.example.attendance.dto.EmployeeStatsSummaryDTO;
import org.example.attendance.dto.GeneralStatsDTO;
import org.example.attendance.repository.AttendanceRepo;
import org.example.attendance.repository.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticalService {

    private final AttendanceRepo attendanceRepo;
  private final EmployeeRepo employeeRepo;

    public List<DepartmentStatsDTO> getStatsByDepartment(LocalDate start, LocalDate end) {
        return attendanceRepo.getDepartmentStats(start, end);
    }
    public List<EmployeeStatsSummaryDTO> getStatsByEmployee(LocalDate start, LocalDate end) {
        return attendanceRepo.getEmployeeStats(start, end);
    }
    public GeneralStatsDTO getGeneralStats(LocalDate start, LocalDate end) {
        GeneralStatsDTO dto = new GeneralStatsDTO();
        dto.setTotalEmployees(employeeRepo.count());
        dto.setNewEmployees(employeeRepo.countByHireDateBetween(start, end));
        dto.setAdminCount(employeeRepo.countByRole("ADMIN"));
        dto.setUserCount(employeeRepo.countByRole("USER"));
        dto.setMaleCount(employeeRepo.countByGender("Male"));
        dto.setFemaleCount(employeeRepo.countByGender("Female"));
        dto.setTotalLateOccurrences(attendanceRepo.countTotalLate(start, end));
        dto.setEmployeesWithLate(attendanceRepo.countLateEmployees(start, end));

        return dto;
    }
}