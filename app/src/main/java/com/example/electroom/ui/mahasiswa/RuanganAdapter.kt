package com.example.electroom.ui.mahasiswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.electroom.R
import com.example.electroom.data.model.Ruangan

class RuanganAdapter(
    private val onBookingClick: (Ruangan) -> Unit
) : ListAdapter<Ruangan, RuanganAdapter.RuanganViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Ruangan>() {
        override fun areItemsTheSame(oldItem: Ruangan, newItem: Ruangan) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Ruangan, newItem: Ruangan) = oldItem == newItem
    }

    inner class RuanganViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaRuangan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusRuangan)
        val tvKapasitas: TextView = itemView.findViewById(R.id.tvKapasitas)
        val tvFasilitas: TextView = itemView.findViewById(R.id.tvFasilitas)
        val btnBooking: Button = itemView.findViewById(R.id.btnBooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuanganViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruangan, parent, false)
        return RuanganViewHolder(view)
    }

    override fun onBindViewHolder(holder: RuanganViewHolder, position: Int) {
        val ruangan = getItem(position)

        holder.tvNama.text = ruangan.nama
        holder.tvStatus.text = ruangan.status.replaceFirstChar { it.uppercase() }
        holder.tvKapasitas.text = "Kapasitas: ${ruangan.kapasitas} orang"
        holder.tvFasilitas.text = "Fasilitas: ${ruangan.fasilitas}"

        // Warna badge status
        val badgeColor = when (ruangan.status) {
            "tersedia" -> android.graphics.Color.parseColor("#2E7D32")
            "terpakai" -> android.graphics.Color.parseColor("#E53935")
            else -> android.graphics.Color.parseColor("#F57F17")
        }
        holder.tvStatus.setBackgroundColor(badgeColor)

        // Nonaktifkan tombol booking kalau tidak tersedia
        holder.btnBooking.isEnabled = ruangan.status == "tersedia"
        holder.btnBooking.alpha = if (ruangan.status == "tersedia") 1f else 0.5f

        holder.btnBooking.setOnClickListener {
            onBookingClick(ruangan)
        }
    }
}