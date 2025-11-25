package org.example.attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.prefs.Preferences;
@Data
@Entity
@Table(name="employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    @Column(name="name",nullable = false,length=100)
    private String name;
    @Column(name="email",nullable = false,length=120)
    private String email;
    @Column(name="password",nullable = false)
    private String password;
    @Column(name="dob")
    private LocalDate dob;
    @Column(name="gender")
    private String gender;
    @Column(nullable=false,length=100)
    private String role;
    @Column(name="status")
    private String status;
    @Column (name="image")
    private String image;
    // N employee-1department
    @ManyToOne
    @JoinColumn(name = "department_id",referencedColumnName = "" +
            "id")
    private Department department;
    @Column(name="hireDate")
    private LocalDate hireDate;
    @JsonIgnore
    // 1 employee  - N attendance
    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Attendance> attendances;
    //1 employee - N leaveRequest
    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests;
    //1 employee - N loginHistory
    @OneToMany(mappedBy ="employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY )
    private List<LoginHistory>  loginHistories;
}
