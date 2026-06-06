package com.example.electroom.data.repository

import com.example.electroom.data.model.Ruangan
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RuanganRepository {
    private val db = FirebaseFirestore.getInstance()
    private val ruanganRef = db.collection("ruangan")

    suspend fun getAllRuangan(): Result<List<Ruangan>> {
        return try {
            val snapshot = ruanganRef.get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Ruangan::class.java)?.copy(id = doc.id)
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRuanganById(id: String): Result<Ruangan> {
        return try {
            val doc = ruanganRef.document(id).get().await()
            val ruangan = doc.toObject(Ruangan::class.java)?.copy(id = doc.id)
                ?: return Result.failure(Exception("Ruangan tidak ditemukan"))
            Result.success(ruangan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun tambahRuangan(ruangan: Ruangan): Result<Unit> {
        return try {
            ruanganRef.add(ruangan).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRuangan(ruangan: Ruangan): Result<Unit> {
        return try {
            ruanganRef.document(ruangan.id).set(ruangan).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hapusRuangan(id: String): Result<Unit> {
        return try {
            ruanganRef.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}