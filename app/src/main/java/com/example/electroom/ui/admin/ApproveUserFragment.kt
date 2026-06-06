package com.example.electroom.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electroom.R
import com.example.electroom.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ApproveUserFragment : Fragment() {

    private val authRepository = AuthRepository()
    private lateinit var adapter: ApproveUserAdapter
    private lateinit var rvApproveUser: RecyclerView
    private lateinit var tvEmptyUser: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_approve_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvApproveUser = view.findViewById(R.id.rvApproveUser)
        tvEmptyUser = view.findViewById(R.id.tvEmptyUser)

        adapter = ApproveUserAdapter(
            onApprove = { user -> updateStatus(user.uid, "aktif", "Akun disetujui") },
            onReject = { user -> updateStatus(user.uid, "ditolak", "Akun ditolak") }
        )

        rvApproveUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@ApproveUserFragment.adapter
        }

        loadPendingUsers()
    }

    private fun loadPendingUsers() {
        lifecycleScope.launch {
            authRepository.getUsersPending().onSuccess { list ->
                adapter.submitList(list)
                if (list.isEmpty()) {
                    rvApproveUser.visibility = View.GONE
                    tvEmptyUser.visibility = View.VISIBLE
                } else {
                    rvApproveUser.visibility = View.VISIBLE
                    tvEmptyUser.visibility = View.GONE
                }
            }.onFailure {
                Toast.makeText(requireContext(), "Gagal memuat data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStatus(uid: String, status: String, message: String) {
        lifecycleScope.launch {
            authRepository.updateUserStatus(uid, status).onSuccess {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                loadPendingUsers()
            }.onFailure {
                Toast.makeText(requireContext(), "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
