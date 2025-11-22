package CaseBased;
import java.util.Scanner;

public class CaseBasedASD {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph malang = null;

        System.out.println("==================================================");
        System.out.println("       SISTEM NAVIGASI KOTA MALANG       ");
        System.out.println("==================================================");
        
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Input Data Lokasi dan Jalur");
            System.out.println("2. Cari Rute (Dari lokasi ke lokasi)");
            System.out.println("3. Tampilkan Semua Jalur (BFS, DFS, Dijkstra)");
            System.out.println("4. Tampilkan Informasi Graf");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu (1-5): ");
            
            int menu = 0;
            try {
                menu = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Input tidak valid!");
                continue;
            }

            switch (menu) {
                case 1:
                    if (malang != null) {
                        System.out.println("\n" + "=".repeat(50));
                        System.out.println("PERINGATAN: Data lokasi dan jalur sudah ada!");
                        System.out.println("=".repeat(50));
                        System.out.print("Apakah Anda ingin menimpa data lama? (y/n): ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        
                        if (confirm.equals("y") || confirm.equals("yes")) {
                            malang = inputDataLokasidanJalur(scanner);
                        } else {
                            System.out.println("Input data dibatalkan. Data lama tetap disimpan.");
                        }
                    } else {
                        malang = inputDataLokasidanJalur(scanner);
                    }
                    break;
                case 2:
                    if (malang != null) {
                        cariRute(scanner, malang);
                    } else {
                        System.out.println("Silakan input data lokasi dan jalur terlebih dahulu!");
                    }
                    break;
                case 3:
                    if (malang != null) {
                        tampilkanSemuaJalur(scanner, malang);
                    } else {
                        System.out.println("Silakan input data lokasi dan jalur terlebih dahulu!");
                    }
                    break;
                case 4:
                    if (malang != null) {
                        malang.tampilkanInfo();
                    } else {
                        System.out.println("Silakan input data lokasi dan jalur terlebih dahulu!");
                    }
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Menu tidak valid!");
            }
        }

        System.out.println("==================================================");
        System.out.println("Terima kasih telah menggunakan Sistem Navigasi Kota Malang");
        System.out.println("==================================================");
        scanner.close();
    }

    private static Graph inputDataLokasidanJalur(Scanner scanner) {
        System.out.print("Masukkan jumlah lokasi: ");
        int maxLocations = 0;
        boolean jumlahValid = false;
        
        while (!jumlahValid) {
            try {
                maxLocations = scanner.nextInt();
                scanner.nextLine();
                
                if (maxLocations <= 0) {
                    System.out.println("Error: Jumlah lokasi harus lebih dari 0!");
                    System.out.print("Masukkan jumlah lokasi: ");
                    continue;
                }
                
                if (maxLocations == 1) {
                    System.out.println("Error: Minimal harus ada 2 lokasi untuk membuat graf dengan jalur!");
                    System.out.print("Masukkan jumlah lokasi: ");
                    continue;
                }
                
                jumlahValid = true;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Error: Input tidak valid!");
                System.out.print("Masukkan jumlah lokasi: ");
            }
        }
        
        Graph malang = new Graph(maxLocations);
        int lokasiTerisi = 0;

        System.out.println("\n--- INPUT LOKASI ---");
        for (int i = 1; i <= maxLocations; i++) {
            boolean lokasiValid = false;
            while (!lokasiValid) {
                System.out.print("Nama Lokasi ke-" + i + ": ");
                String namaLokasi = scanner.nextLine().trim();
                
                if (namaLokasi.isEmpty()) {
                    System.out.println("Error: Nama lokasi tidak boleh kosong!");
                    continue;
                }
                
                if (malang.isLokasiDuplikat(namaLokasi)) {
                    System.out.println("Error: Lokasi '" + namaLokasi + "' sudah ada. Silakan input nama lokasi yang berbeda!");
                    continue;
                }
                
                malang.tambahLokasi(namaLokasi);
                lokasiTerisi++;
                lokasiValid = true;
            }
        }
        
        if (lokasiTerisi == 0) {
            System.out.println("Error: Tidak ada lokasi yang berhasil diinput!");
            return null;
        }

        System.out.println("\n--- INPUT JALUR ---");
        System.out.print("Berapa banyak jalur yang ingin ditambahkan? ");
        int jumlahJalur = scanner.nextInt();
        scanner.nextLine();
        
        if (jumlahJalur < 0) {
            System.out.println("Error: Jumlah jalur tidak boleh negatif!");
            System.out.println("Data lokasi berhasil diinput tanpa jalur.");
            return malang;
        }
        
        int jalurTerisi = 0;

        for (int i = 1; i <= jumlahJalur; i++) {
            boolean jalurValid = false;
            while (!jalurValid) {
                System.out.println("\nJalur ke-" + i + ":");
                
                System.out.print("Dari (Nama Lokasi): ");
                String dari = scanner.nextLine().trim();
                
                if (!malang.isLokasiAda(dari)) {
                    System.out.println("Error: Lokasi '" + dari + "' tidak ditemukan!");
                    continue;
                }
                
                System.out.print("Ke (Nama Lokasi): ");
                String ke = scanner.nextLine().trim();
                
                if (!malang.isLokasiAda(ke)) {
                    System.out.println("Error: Lokasi '" + ke + "' tidak ditemukan!");
                    continue;
                }
                
                if (dari.equalsIgnoreCase(ke)) {
                    System.out.println("Error: Lokasi asal dan tujuan tidak boleh sama!");
                    continue;
                }
                
                System.out.print("Jarak (km): ");
                double jarak = 0;
                boolean jarakValid = false;
                try {
                    jarak = scanner.nextDouble();
                    if (jarak <= 0) {
                        System.out.println("Error: Jarak harus lebih dari 0!");
                        scanner.nextLine();
                        continue;
                    }
                    jarakValid = true;
                } catch (Exception e) {
                    System.out.println("Error: Input jarak tidak valid!");
                    scanner.nextLine();
                    continue;
                }
                
                System.out.print("Waktu (menit): ");
                int waktu = 0;
                boolean waktuValid = false;
                try {
                    waktu = scanner.nextInt();
                    if (waktu <= 0) {
                        System.out.println("Error: Waktu harus lebih dari 0!");
                        scanner.nextLine();
                        continue;
                    }
                    waktuValid = true;
                } catch (Exception e) {
                    System.out.println("Error: Input waktu tidak valid!");
                    scanner.nextLine();
                    continue;
                }
                scanner.nextLine();

                if (jarakValid && waktuValid) {
                    malang.tambahJalur(dari, ke, jarak, waktu);
                    jalurTerisi++;
                    jalurValid = true;
                }
            }
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DATA BERHASIL DIINPUT");
        System.out.println("=".repeat(50));
        System.out.println("Total Lokasi: " + lokasiTerisi);
        System.out.println("Total Jalur: " + jalurTerisi);
        System.out.println("=".repeat(50));
        return malang;
    }

    private static void cariRute(Scanner scanner, Graph malang) {
        System.out.println("\n--- PENCARIAN RUTE ---");
        
        // Input asal dengan validasi loop
        String asal = "";
        boolean asalValid = false;
        while (!asalValid) {
            System.out.print("Masukkan titik asal: ");
            asal = scanner.nextLine().trim();
            
            if (asal.isEmpty()) {
                System.out.println("Error: Titik asal tidak boleh kosong!");
                continue;
            }
            
            if (!malang.isLokasiAda(asal)) {
                System.out.println("Error: Lokasi asal '" + asal + "' tidak ditemukan!");
                malang.tampilkanDaftarLokasi();
                continue;
            }
            
            asalValid = true;
        }
        
        // Input tujuan dengan validasi loop
        String tujuan = "";
        boolean tujuanValid = false;
        while (!tujuanValid) {
            System.out.print("Masukkan titik tujuan: ");
            tujuan = scanner.nextLine().trim();
            
            if (tujuan.isEmpty()) {
                System.out.println("Error: Titik tujuan tidak boleh kosong!");
                continue;
            }
            
            if (!malang.isLokasiAda(tujuan)) {
                System.out.println("Error: Lokasi tujuan '" + tujuan + "' tidak ditemukan!");
                malang.tampilkanDaftarLokasi();
                continue;
            }
            
            if (asal.equalsIgnoreCase(tujuan)) {
                System.out.println("Error: Titik asal dan tujuan tidak boleh sama!");
                continue;
            }
            
            tujuanValid = true;
        }
        
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println("HASIL PENCARIAN RUTE");
        System.out.println("Dari: " + asal + " -> Ke: " + tujuan);
        System.out.println("=".repeat(50));

        boolean status = malang.bisaPergi(asal, tujuan);
        if (status) {
            System.out.println("Status: Dapat dijangkau\n");
            malang.pergi(asal, tujuan);
            System.out.println();
            malang.jalurTerpendek(asal, tujuan);
        } else {
            System.out.println("Status: Tidak dapat dijangkau");
        }
        System.out.println("=".repeat(50));
    }

    private static void tampilkanSemuaJalur(Scanner scanner, Graph malang) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TAMPILKAN SEMUA RUTE");
        System.out.println("=".repeat(50));
        System.out.println("Menampilkan rute untuk semua pasangan lokasi (BFS, DFS, Dijkstra)?");
        System.out.print("Lanjutkan? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            malang.tampilkanSemuaRute();
        } else {
            System.out.println("Dibatalkan.");
        }
    }
}

