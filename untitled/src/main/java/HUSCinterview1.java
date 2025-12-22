// [JDK] - Bước 1: Để có được file này, bạn cần JDK (javac) để biên dịch code.
// Nếu không có JDK, máy tính không hiểu file văn bản này là gì.

public class HUSCinterview1 {

    // [JVM] - Bước 2: Khi bạn gõ lệnh 'java HUSCinterview1',
    // JVM (Máy ảo) sẽ tìm ngay hàm 'main' này để bắt đầu chạy chương trình.
    public static void main(String[] args) {

        // [JRE] - Thư viện:
        // 'System' và 'String' là các lớp có sẵn nằm trong thư viện của JRE.
        // Bạn không cần viết ra chúng, JRE cung cấp sẵn để bạn dùng.
        String thongDiep = "Chao mung ban den voi HUSC";

        // [JVM] - Thực thi:
        // JVM nhận lệnh này, dịch sang ngôn ngữ máy và điều khiển màn hình in ra dòng chữ.
        System.out.println(thongDiep);

        // [JVM] - Quản lý bộ nhớ:
        // Khi dòng lệnh này chạy, JVM sẽ cấp phát RAM để lưu biến 'namKinhNghiem'.
        int namKinhNghiem = 5;

        // [JRE] + [JVM] kết hợp:
        // JRE cung cấp hàm in (println), còn JVM thực hiện việc in số 5 ra màn hình.
        System.out.println("Kinh nghiem: " + namKinhNghiem + " nam.");
    }
}