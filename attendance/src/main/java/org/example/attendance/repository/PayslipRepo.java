package org.example.attendance.repository;

import org.example.attendance.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepo extends JpaRepository<Payslip, Long> {

    Optional<Payslip> findByEmployeeIdAndMonthAndYear(Long employeeId, int month, int year);


    List<Payslip> findByEmployeeIdOrderByYearDescMonthDesc(Long employeeId);

}
