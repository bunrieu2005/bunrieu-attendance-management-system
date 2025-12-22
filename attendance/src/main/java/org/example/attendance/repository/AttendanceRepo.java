package org.example.attendance.repository;

import org.example.attendance.dto.DepartmentStatsDTO;
import org.example.attendance.dto.EmployeeStatsSummaryDTO;
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

    List<Attendance> findByEmployeeIdOrderByWorkDateDesc(Long employeeId);// Lấy attendance theo employee ID và khoảng thời gian

    List<Attendance> findByEmployeeIdAndWorkDateBetweenOrderByWorkDateDesc(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
    // new funtion: all record checkout = null < today -> delete checkin
    List<Attendance> findAllByCheckOutAtIsNullAndWorkDateBefore(LocalDate date);

    @Query("SELECT new org.example.attendance.dto.DepartmentStatsDTO(" +
            "   d.name, " +
            "   COUNT(DISTINCT e.id), " +

            "   CAST(COALESCE(SUM(a.totalMinutes), 0) AS double) / 60.0, " +

            "   CAST(COALESCE(SUM(a.lateFlag), 0) AS long) " +
            ") " +
            "FROM Employee e " +
            "LEFT JOIN e.department d " +
            "LEFT JOIN Attendance a ON a.employee.id = e.id " +
            "WHERE a.workDate BETWEEN :startDate AND :endDate " +
            "GROUP BY d.name")
    List<DepartmentStatsDTO> getDepartmentStats(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    @Query("SELECT new org.example.attendance.dto.EmployeeStatsSummaryDTO(" +
            "   e.id, " +
            "   e.name, " +
            "   d.name, " +

            "   CAST(COALESCE(SUM(a.totalMinutes), 0) AS double) / 60.0, " +
            "   COUNT(a.id), " +

            "   CAST(COALESCE(SUM(a.lateFlag), 0) AS long) " +
            ") " +
            "FROM Attendance a " +
            "JOIN a.employee e " +
            "LEFT JOIN e.department d " +
            "WHERE a.workDate BETWEEN :startDate AND :endDate " +
            "GROUP BY e.id, e.name, d.name " +
            "ORDER BY SUM(a.totalMinutes) DESC")
    List<EmployeeStatsSummaryDTO> getEmployeeStats(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);



    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.lateFlag > 0 AND a.workDate BETWEEN :startDate AND :endDate")
    long countTotalLate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT COUNT(DISTINCT a.employee.id) FROM Attendance a WHERE a.lateFlag > 0 AND a.workDate BETWEEN :startDate AND :endDate")
    long countLateEmployees(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // payroll

    @Query("SELECT COALESCE(SUM(a.totalMinutes), 0) FROM Attendance a WHERE a.employee.id = :empId AND a.workDate BETWEEN :startDate AND :endDate")
    long sumTotalMinutesInMonth(@Param("empId") Long empId,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);



    @Query("SELECT COALESCE(SUM(a.lateFlag), 0) FROM Attendance a WHERE a.employee.id = :empId AND a.workDate BETWEEN :startDate AND :endDate")
    long countLateTimesInMonth(@Param("empId") Long empId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);
}