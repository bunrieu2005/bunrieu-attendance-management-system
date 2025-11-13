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
    //check checkin in day
    Optional<Attendance> findByEmployeeIdAndWorkDate(long id,LocalDate workDate);
    //  user : employee want checkout
    Optional<Attendance> findTopByEmployeeIdAndCheckOutAtIsNullOrderByCheckInAtDesc(long employeeId);

}
