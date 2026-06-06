package com.example.electroom.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.electroom.R
import com.example.electroom.data.model.Booking

class ApproveBookingAdapter(
    private val onApprove: (Booking) -> Unit,
    private val onReject: (Booking) -> Unit
) : ListAdapter<Booking, ApproveBookingAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Booking, newItem: Booking) = oldItem == newItem
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaUser: TextView = itemView.findViewById(R.id.tvNamaUserApprove)
        val tvNamaRuangan: TextView = itemView.findViewById(R.id.tvNamaRuanganApprove)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalApprove)
        val tvJam: TextView = itemView.findViewById(R.id.tvJamApprove)
        val tvKeperluan: TextView = itemView.findViewById(R.id.tvKeperluanApprove)
        val btnApprove: Button = itemView.findViewById(R.id.btnApprove)
        val btnReject: Button = itemView.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_approve_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = getItem(position)

        holder.tvNamaUser.text = "${booking.namaUser} (${booking.roleUser})"
        holder.tvNamaRuangan.text = booking.namaRuangan
        holder.tvTanggal.text = "Tanggal: ${booking.tanggal}"
        holder.tvJam.text = "Jam: ${booking.jamMulai} - ${booking.jamSelesai}"
        holder.tvKeperluan.text = "Keperluan: ${booking.keperluan}"

        holder.btnApprove.setOnClickListener { onApprove(booking) }
        holder.btnReject.setOnClickListener { onReject(booking) }
    }
}