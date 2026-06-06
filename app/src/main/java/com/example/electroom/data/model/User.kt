package com.example.electroom.data.model

data class User(
    val uid: String = "",
    val nama: String = "",
    val email: String = "",
    val role: String = "", // "mahasiswa", "dosen", "admin"
    val nimNip: String = "",
    val status: String = "pending" // "pending", "aktif", "ditolak"
)