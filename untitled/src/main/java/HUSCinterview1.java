void main() {



    // JRE cung cap cac thu vien san co (vi du: System, Runtime) de code chay duoc.
    IO.println("\n[1] Thong tin JRE (Moi truong chay):");

    // Lay thong tin tu he thong (System Properties)
    String javaVersion = System.getProperty("java.version");
    String javaHome = System.getProperty("java.home");
    String javaVendor = System.getProperty("java.vendor");

    IO.println(" - Phien ban Java: " + javaVersion);
    IO.println(" - Nha cung cap: " + javaVendor);
    IO.println(" - Duong dan cai dat JRE: " + javaHome);

    // 2. PHAN MEM: DEMO VAI TRO CUA JVM (Java Virtual Machine)
    // JVM quan ly bo nho va thuc thi code. Doan nay lay thong tin bo nho JVM.
    IO.println("\n[2] Thong tin JVM (May ao Java):");

    Runtime runtime = Runtime.getRuntime();
    long maxMemory = runtime.maxMemory(); // Bo nho toi da JVM duoc cap
    long totalMemory = runtime.totalMemory(); // Bo nho hien tai dang dung

    // Doi tu Byte sang Megabyte (MB) cho de doc
    long maxMemoryMB = maxMemory / (1024 * 1024);
    long totalMemoryMB = totalMemory / (1024 * 1024);

    IO.println(" - Bo nho toi da (Max Heap): " + maxMemoryMB + " MB");
    IO.println(" - Bo nho dang cap phat: " + totalMemoryMB + " MB");
    IO.println(" -> JVM dang hoat dong tot de xu ly cac lenh nay!");

    // 3. LY THUYET: VAI TRO CUA JDK (Java Development Kit)
    // JDK khong xuat hien khi chay (run), no xuat hien luc bien dich (compile).
    IO.println("\n[3] Vay JDK dau?");
    IO.println(" - Neu ban thay dong chu nay, tuc la JDK da hoan thanh nhiem vu.");
    IO.println(" - JDK da dung 'javac' de bien dich file .java thanh .class truoc khi ban chay lenh nay.");
}