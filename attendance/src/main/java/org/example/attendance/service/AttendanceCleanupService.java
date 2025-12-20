package org.example.attendance.service;

import org.example.attendance.entity.Attendance;
import org.example.attendance.repository.AttendanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//@Scheduled(cron = "0 0 0 * * ?")
//Cách 1:chỉ kích hoạt đúng vào khoảnh khắc 00:00:00. nếu lúc đó server đang tắt, sự kiện đó sẽ bị bỏ qua
// Sáng mai bạn bật server lên, nó sẽ đợi đến 00:00 đêm hôm sau mới chạy tiếp -> Lúc đó record cũ chưa bị xóa, bạn check-in sẽ bị lỗi.
@Service
public class AttendanceCleanupService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteForgotCheckoutRecords() {
        // executeCleanup();
        executeSoftClose();  // new
    }

    // Cách2: chạy ngay lập tức khi ứng dụng vừa khởi động xong
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void runCleanupOnStartup() {
        System.out.println("Server run: quet don ban ghi cu");
        // executeCleanup();
        executeSoftClose();
    }

    // ==============================================f==========================
    // Cach moi : soft close
    // =========================================================================
    private void executeSoftClose() {
        LocalDate today = LocalDate.now();
        List<Attendance> staleRecords = attendanceRepo.findAllByCheckOutAtIsNullAndWorkDateBefore(today);

        if (staleRecords.isEmpty()) {
            System.out.println("Data clean (Soft Close). no hanging records.");
            return;
        }

        System.out.println("found " + staleRecords.size() + " records to auto-close.");

        for (Attendance att : staleRecords) {
            //chechin = checkout =0
            att.setCheckOutAt(att.getCheckInAt());
            att.setTotalMinutes(0);
            // att.setNote("system auto-closed: forgot Checkout");
        }
        attendanceRepo.saveAll(staleRecords);
        System.out.println("successfully auto-closed " + staleRecords.size() + " records.");
    }
    private void executeCleanup() {
        LocalDate today = LocalDate.now();
        // searach attent null checkout and day < today
        List<Attendance> staleRecords = attendanceRepo.findAllByCheckOutAtIsNullAndWorkDateBefore(today);
        if (!staleRecords.isEmpty()) {
            attendanceRepo.deleteAll(staleRecords);
            System.out.println("complete clean " + staleRecords.size() + " atten no check-out.");
        } else {
            System.out.println("data clean");
        }
    }// dam bao an toan
// Cách1:   public void deleteStaleAttendances() {
//        LocalDate today = LocalDate.now();
//        List<Attendance> staleRecords = attendanceRepo.findAllByCheckOutAtIsNullAndWorkDateBefore(today);
//        if (!staleRecords.isEmpty()) {
//            System.out.println("TODO delete " + staleRecords.size() + " record null checkout..");
//
//            attendanceRepo.deleteAll(staleRecords);
//             staleRecords.forEach(a -> System.out.println("complete delete: " + a.getEmployee().getId()));
//        }
//    }
}