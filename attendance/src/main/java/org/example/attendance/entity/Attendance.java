package org.example.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
      private long id;
    @Column(name="word_date",nullable=false)
    private LocalDate workDate;
    @Column(name="check_in_at",nullable=false)
    private LocalDateTime checkInAt;
    @Column(name="check_out_at")
    private LocalDateTime checkOutAt;
    @Column(name="total_minutes")
    private Integer totalMinutes;
    @Column(name="late_flag",nullable=false)
    private int lateFlag;
    @Column(name="early_leave_flag",nullable=false)
    private int earlyLeaveFlag;
    @ManyToOne
    @JoinColumn(name="employee_id",referencedColumnName = "id")
    private Employee employee;
    @Column
    private String method;
    @Column
    private String ip;
}
