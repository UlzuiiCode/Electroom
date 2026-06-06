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
import com.example.electroom.data.model.Ruangan

class AdminRuanganAdapter(
    private val onEdit: (Ruangan) -> Unit,
    private val onDelete: (Ruangan) -> Unit
) : ListAdapter<Ruangan, AdminRuanganAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Ruangan>() {
        override fun areItemsTheSame(oldItem: Ruangan, newItem: Ruangan) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Ruangan, newItem: Ruangan) = oldItem == newItem
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaRuanganAdmin)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusRuanganAdmin)
        val tvKapasitas: TextView = itemView.findViewById(R.id.tvKapasitasAdmin)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditRuangan)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapusRuangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruangan_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ruangan = getItem(position)

        holder.tvNama.text = ruangan.nama
        holder.tvKapasitas.text = "Kapasitas: ${ruangan.kapasitas} orang"
        holder.tvStatus.text = ruangan.status.replaceFirstChar { it.uppercase() }

        val badgeColor = when (ruangan.status) {
            "tersedia" -> android.graphics.Color.parseColor("#2E7D32")
            "terpakai" -> android.graphics.Color.parseColor("#E53935")
            else -> android.graphics.Color.parseColor("#F57F17")
        }
        holder.tvStatus.setBackgroundColor(badgeColor)

        holder.btnEdit.setOnClickListener { onEdit(ruangan) }
        holder.btnHapus.setOnClickListener { onDelete(ruangan) }
    }
}