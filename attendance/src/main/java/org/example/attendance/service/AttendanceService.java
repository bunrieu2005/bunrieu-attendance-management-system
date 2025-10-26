package org.example.attendance.service;

import org.example.attendance.entity.Attendance;
import org.example.attendance.repository.AttendanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepo attendanceRepo;
    //1:ghi nhận giờ vào ra
    //2: lấy danh sách chấm công theo nhân viên ,ngày
    //3:tính tổng giờ hàm
    public List<Attendance> getAllAttendance(){
        return attendanceRepo.findAll();
    }
    public Optional<Attendance> getAttendanceById(Long id){
        return attendanceRepo.findById(id);
    }
    public List<Attendance> getAttendanceByEmployee(Long emplooyeeId){
        return attendanceRepo.findByEmployee_Id(emplooyeeId);
    }
    public List<Attendance> getAttendanceByDate(LocalDate date){
        return attendanceRepo.findByWorkDate(date);
    }
    public Attendance saveAttendance(Attendance attendance){
        return attendanceRepo.save(attendance);
    }
    public void deleteAttendanceById(Long id){
        attendanceRepo.deleteById(id);
    }

}
