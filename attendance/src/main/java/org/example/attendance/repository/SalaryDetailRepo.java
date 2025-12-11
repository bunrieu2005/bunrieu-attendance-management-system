package org.example.attendance.repository;

import org.example.attendance.entity.SalaryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryDetailRepo extends JpaRepository<SalaryDetail,Long> {
    Optional<SalaryDetail> findByEmployeeId(int id);
}
