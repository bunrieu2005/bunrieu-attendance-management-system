package org.example.attendance.service;

import org.example.attendance.entity.LoginHistory;
import org.example.attendance.repository.LoginHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginHistoryService
{
    @Autowired
    private LoginHistoryRepo loginHistoryRepo;
    public List<LoginHistory> getAllLoginHistory()
    {
        return loginHistoryRepo.findAll();
    }
    public Optional<LoginHistory> getLoginHistoryById(long id)
    {
        return loginHistoryRepo.getLoginHistoryById(id);
    }
    public List<LoginHistory>  getLoginByEmployeeId(Long employeeId)
    {
        return loginHistoryRepo.getLoginByEmployeeId(employeeId);
    }
    public LoginHistory save(LoginHistory loginHistory){
        return loginHistoryRepo.save(loginHistory);
    }
    public void deleteById(Long id){
        loginHistoryRepo.deleteById(id);
    }
}
