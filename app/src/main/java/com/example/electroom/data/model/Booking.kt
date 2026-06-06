package com.example.electroom.data.model

data class Booking(
    val id: String = "",
    val userId: String = "",
    val namaUser: String = "",
    val roleUser: String = "",
    val ruanganId: String = "",
    val namaRuangan: String = "",
    val tanggal: String = "",
    val jamMulai: String = "",
    val jamSelesai: String = "",
    val keperluan: String = "",
    val status: String = "menunggu", // "menunggu", "disetujui", "ditolak"
    val timestamp: Long = System.currentTimeMillis()
)