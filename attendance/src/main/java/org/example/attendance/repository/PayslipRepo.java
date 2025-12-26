package org.example.attendance.repository;

import org.example.attendance.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepo extends JpaRepository<Payslip, Long> {


    Optional<Payslip> findByEmployeeIdAndStartDateAndEndDate(Long employeeId, LocalDate startDate, LocalDate endDate);

    
    List<Payslip> findByEmployeeIdOrderByStartDateDesc(Long employeeId);

    List<Payslip> findByEmployeeId(Long empId);
}
