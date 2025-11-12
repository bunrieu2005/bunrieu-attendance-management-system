package org.example.attendance.repository;

import org.example.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance,Long> {
    List<Attendance> findByEmployee_Id(long id);
    List<Attendance> findByWorkDate(LocalDate workDate);
    //kiem tra da check in trong ngay chua
    Optional<Attendance> findByEmployeeIdAndWorkDate(long id,LocalDate workDate);
    // dung khi nhan vien muon check out
    Optional<Attendance> findTopByEmployeeIdAndCheckOutAtIsNullOrderByCheckInAtDesc(long employeeId);
    //lay danh sach cham cong trong time:xuat bao cao theo ngay

    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :empId AND a.workDate BETWEEN :from AND :to")
    List<Attendance> findRange(@Param("empId") Long empId, @Param("from") LocalDate from, @Param("to") LocalDate to);

}
