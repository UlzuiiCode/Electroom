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
import com.example.electroom.data.model.User

class ApproveUserAdapter(
    private val onApprove: (User) -> Unit,
    private val onReject: (User) -> Unit
) : ListAdapter<User, ApproveUserAdapter.UserViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_approve_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.tvApproveNama)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvApproveEmail)
        private val tvRole: TextView = itemView.findViewById(R.id.tvApproveRole)
        private val tvNimNip: TextView = itemView.findViewById(R.id.tvApproveNimNip)
        private val btnApprove: Button = itemView.findViewById(R.id.btnApproveUser)
        private val btnReject: Button = itemView.findViewById(R.id.btnRejectUser)

        fun bind(user: User) {
            val labelId = if (user.role.lowercase() == "dosen") "NIP" else "NIM"
            
            tvNama.text = user.nama
            tvEmail.text = user.email
            tvRole.text = "Role: ${user.role.replaceFirstChar { it.uppercase() }}"
            tvNimNip.text = "$labelId: ${user.nimNip}"

            btnApprove.setOnClickListener { onApprove(user) }
            btnReject.setOnClickListener { onReject(user) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.uid == newItem.uid
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }
}
