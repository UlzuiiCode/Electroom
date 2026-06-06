package com.example.electroom.ui.mahasiswa

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.electroom.R
import com.example.electroom.data.model.Booking

class BookingAdapter : ListAdapter<Booking, BookingAdapter.BookingViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        holder.bind(booking)
    }

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaRuangan: TextView = itemView.findViewById(R.id.tvNamaRuanganBooking)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatusBooking)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalBooking)
        private val tvJam: TextView = itemView.findViewById(R.id.tvJamBooking)
        private val tvKeperluan: TextView = itemView.findViewById(R.id.tvKeperluanBooking)

        fun bind(booking: Booking) {
            tvNamaRuangan.text = booking.namaRuangan
            tvStatus.text = booking.status.replaceFirstChar { it.uppercase() }
            tvTanggal.text = "Tanggal: ${booking.tanggal}"
            tvJam.text = "Waktu: ${booking.jamMulai} - ${booking.jamSelesai}"
            tvKeperluan.text = "Keperluan: ${booking.keperluan}"

            // Atur warna badge berdasarkan status
            val statusColor = when (booking.status.lowercase()) {
                "disetujui" -> "#4CAF50" // Hijau
                "ditolak" -> "#F44336"   // Merah
                else -> "#FF9800"        // Oranye (menunggu)
            }
            tvStatus.background.setTint(Color.parseColor(statusColor))
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }
}
