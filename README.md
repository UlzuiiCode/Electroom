# Electroom - Aplikasi Peminjaman Ruangan

Electroom adalah aplikasi Android berbasis Kotlin yang dirancang untuk mengelola peminjaman ruangan di lingkungan kampus atau perkantoran. Aplikasi ini menggunakan **Firebase** sebagai backend untuk autentikasi dan penyimpanan data secara real-time.

## 🚀 Fitur Utama

### 🔐 Autentikasi & Manajemen Pengguna
*   **Multi-Role**: Mendukung peran Admin, Dosen, dan Mahasiswa.
*   **Sistem Persetujuan Akun**: Pengguna baru yang mendaftar akan berstatus `pending` dan harus disetujui oleh Admin sebelum dapat login.
*   **Input Dinamis**: Formulir pendaftaran otomatis menyesuaikan input (NIM untuk Mahasiswa, NIP untuk Dosen).

### 👨‍💼 Fitur Admin
*   **Dashboard Monitoring**: Ringkasan total ruangan, jumlah booking menunggu, dan aktivitas terbaru.
*   **Kelola Ruangan**: Tambah, edit, dan hapus data ruangan (Nama, Kapasitas, Fasilitas, Lantai).
*   **Persetujuan Booking**: Menyetujui atau menolak permohonan peminjaman ruangan.
*   **Persetujuan User**: Menyetujui atau menolak pendaftaran akun baru.

### 🎓 Fitur Mahasiswa & Dosen
*   **Daftar Ruangan**: Melihat daftar ruangan yang tersedia secara real-time.
*   **Booking Ruangan**: Melakukan pemesanan dengan memilih tanggal dan mengisi keperluan.
*   **Status Booking**: Memantau status permohonan booking (Menunggu, Disetujui, atau Ditolak).

## 🛠️ Tech Stack
*   **Bahasa**: Kotlin
*   **Backend**: Firebase Authentication, Cloud Firestore
*   **Arsitektur**: Clean Architecture (Repository Pattern)
*   **UI Components**: Material Design, ViewBinding, RecyclerView
*   **Library**: Coroutines, Lifecycle (ViewModel/Runtime), Fragment KTX

## 📂 Struktur Proyek
```
Electroom/
├── app/src/main/java/com/example/electroom/
│   ├── data/            # Entity Model & Repository (Firebase Logic)
│   ├── ui/              # Activity, Fragment, & Adapter (UI Logic)
│   │   ├── admin/       # Fitur Khusus Admin
│   │   ├── auth/        # Login & Register
│   │   ├── mahasiswa/   # Fitur Mahasiswa/Dosen
│   │   └── shared/      # Fitur Bersama (Status)
│   └── MainActivity.kt  # Splash & Router Utama
└── app/src/main/res/    # Layout XML, Drawable, & Menu
```

## ⚙️ Cara Menjalankan
1.  Clone repositori ini.
2.  Buka di **Android Studio (Ladybug atau terbaru)**.
3.  Hubungkan dengan proyek Firebase Anda.
4.  Tambahkan file `google-services.json` ke folder `app/`.
5.  Aktifkan **Email/Password Auth** dan **Cloud Firestore** di Firebase Console.
6.  Build dan jalankan aplikasi.

---
Dikembangkan oleh **[Nama Anda/Kelompok]** sebagai proyek Pemrograman Perangkat Bergerak.
