package org.example.attendance.controller;

import org.example.attendance.entity.LoginHistory;
import org.example.attendance.repository.LoginHistoryRepo;
import org.example.attendance.service.LoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loginhistorys")
public class LoginHistoryRestController {
    @Autowired
    private LoginHistoryService loginHistoryService;
    @GetMapping
    public ResponseEntity<List<LoginHistory>> getAllEmployees()
    {
        List<LoginHistory> loginHistories = loginHistoryService.getAllLoginHistory();
        return ResponseEntity.ok().body(loginHistories);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getLoginHistoryById(@PathVariable long id)
    {
        Optional<LoginHistory> loginHistory = loginHistoryService.getLoginHistoryById(id);
        return loginHistory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable long employeeId)
        {
       List<LoginHistory> list = loginHistoryService.getLoginByEmployeeId(employeeId);
       return  ResponseEntity.ok().body(list);
        }
       @PostMapping
    public ResponseEntity<?> saveLoginHistory(@RequestBody LoginHistory loginHistory){
        LoginHistory savedLoginHistory = loginHistoryService.save(loginHistory);
        return ResponseEntity.ok().body(savedLoginHistory);
       }

       @DeleteMapping("/{id}")
       public ResponseEntity<?> deleteLoginHistory(@PathVariable Long id){
           loginHistoryService.deleteById(id);
           return ResponseEntity.ok("Deleted login history id = " + id);
       }



}
