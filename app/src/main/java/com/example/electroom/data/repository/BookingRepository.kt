package com.example.electroom.data.repository

import com.example.electroom.data.model.Booking
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class BookingRepository {
    private val db = FirebaseFirestore.getInstance()
    private val bookingRef = db.collection("bookings")

    suspend fun buatBooking(booking: Booking): Result<Unit> {
        return try {
            bookingRef.add(booking).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingByUser(userId: String): Result<List<Booking>> {
        return try {
            val snapshot = bookingRef
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Booking::class.java)?.copy(id = doc.id)
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllBooking(): Result<List<Booking>> {
        return try {
            val snapshot = bookingRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Booking::class.java)?.copy(id = doc.id)
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingMenunggu(): Result<List<Booking>> {
        return try {
            val snapshot = bookingRef
                .whereEqualTo("status", "menunggu")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Booking::class.java)?.copy(id = doc.id)
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateStatusBooking(bookingId: String, status: String): Result<Unit> {
        return try {
            bookingRef.document(bookingId).update("status", status).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}