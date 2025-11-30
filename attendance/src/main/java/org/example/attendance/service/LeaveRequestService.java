package org.example.attendance.service;

import org.example.attendance.entity.Employee;
import org.example.attendance.entity.LeaveRequest;
import org.example.attendance.repository.LeaveRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService
{
    @Autowired
    LeaveRequestRepo leaveRequestRepo;
    public List<LeaveRequest> getAllLeaveRequest()
    {
        return leaveRequestRepo.findAll();
    }
    public Optional<LeaveRequest> getLeaveRequestById(long id){
        return  leaveRequestRepo.findById(id);
    }
    public List<LeaveRequest> getLeaveRequestByEmployeeId(long employeeId){
                return leaveRequestRepo.findByEmployee_Id(employeeId);
    }
    public List<LeaveRequest> getByStatus(String status){
        return leaveRequestRepo.findByStatus(status);
    }
    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest){
        return leaveRequestRepo.save(leaveRequest);
    }

    public void deleteLeaveRequestById(long id){
        leaveRequestRepo.deleteById(id);
    }

}
