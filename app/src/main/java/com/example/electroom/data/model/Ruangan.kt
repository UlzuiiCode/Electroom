package com.example.electroom.data.model

data class Ruangan(
    val id: String = "",
    val nama: String = "",
    val kapasitas: Int = 0,
    val fasilitas: String = "",
    val status: String = "tersedia", // "tersedia", "terpakai", "maintenance"
    val lantai: String = "",
    val gedung: String = "Gedung Elektro"
)