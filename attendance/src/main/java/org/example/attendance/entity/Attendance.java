package org.example.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    private LocalDate checkInAt;
    @Column(name="check_out_at")
    private LocalDate checkOutAt;
    @Column(name="total_minutes")
    private Integer totalMinutes;
    @Column(name="late_flag_",nullable=false)
    private boolean lateFlag;
    @Column(name="early_leave_flag",nullable=false)
    private boolean earlyLeaveFlag;
    @ManyToOne
    @JoinColumn(name="employee_id",referencedColumnName = "id")
    private Employee employee;
}
