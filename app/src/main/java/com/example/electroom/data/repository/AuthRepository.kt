package com.example.electroom.data.repository

import com.example.electroom.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Login gagal"))
            val userDoc = db.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java) ?: return Result.failure(Exception("Data user tidak ditemukan"))
            
            // Cek status akun jika bukan admin
            if (user.role != "admin" && user.status != "aktif") {
                val message = when(user.status) {
                    "ditolak" -> "Akun Anda ditolak oleh Admin."
                    else -> "Akun Anda belum aktif/disetujui oleh Admin."
                }
                auth.signOut()
                return Result.failure(Exception(message))
            }
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        nama: String,
        nimNip: String,
        role: String
    ): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Register gagal"))
            
            val user = User(
                uid = uid,
                nama = nama,
                email = email,
                role = role,
                nimNip = nimNip,
                status = "pending" 
            )
            db.collection("users").document(uid).set(user).await()
            auth.signOut()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersPending(): Result<List<User>> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("status", "pending")
                .get().await()
            val list = snapshot.toObjects(User::class.java)
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserStatus(uid: String, newStatus: String): Result<Unit> {
        return try {
            db.collection("users").document(uid).update("status", newStatus).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val doc = db.collection("users").document(uid).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun logout() = auth.signOut()

    fun isLoggedIn() = auth.currentUser != null
}