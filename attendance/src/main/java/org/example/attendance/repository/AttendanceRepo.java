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
    Optional<Attendance> findByEmployeeIdAndWorkDate(long id, LocalDate workDate);
    Optional<Attendance> findTopByEmployeeIdAndCheckOutAtIsNullOrderByCheckInAtDesc(long employeeId);//SELECT * FROM attendancWHERE employee_id = 5
                                                                                                      // AND check_out_at IS NULL ORDER BY check_in_at DESCLIMIT 1;
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.workDate = :date AND a.checkOutAt IS NULL")
    long countByWorkDateAndCheckOutAtIsNull(@Param("date") LocalDate date);
    List<Attendance> findByWorkDateAndCheckOutAtIsNull(LocalDate workDate);
}
