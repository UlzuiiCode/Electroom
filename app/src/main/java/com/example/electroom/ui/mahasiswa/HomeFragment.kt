package com.example.electroom.ui.mahasiswa

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
import com.example.electroom.data.repository.RuanganRepository
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val ruanganRepository = RuanganRepository()
    private val authRepository = AuthRepository()
    private lateinit var adapter: RuanganAdapter
    private lateinit var rvRuangan: RecyclerView
    private lateinit var tvEmptyRuangan: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNamaUser = view.findViewById<TextView>(R.id.tvNamaUser)
        rvRuangan = view.findViewById(R.id.rvRuangan)
        tvEmptyRuangan = view.findViewById(R.id.tvEmptyRuangan)

        // Tampilkan nama user
        lifecycleScope.launch {
            val user = authRepository.getCurrentUser()
            tvNamaUser.text = "Halo, ${user?.nama ?: "Pengguna"}!"
        }

        adapter = RuanganAdapter { ruangan ->
            // Navigasi ke form booking
            val fragment = BookingFragment.newInstance(ruangan.id, ruangan.nama)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMahasiswa, fragment)
                .addToBackStack(null)
                .commit()
        }

        rvRuangan.layoutManager = LinearLayoutManager(requireContext())
        rvRuangan.adapter = adapter

        loadRuangan()
    }

    private fun loadRuangan() {
        lifecycleScope.launch {
            val result = ruanganRepository.getAllRuangan()
            result.onSuccess { list ->
                adapter.submitList(list)
                if (list.isEmpty()) {
                    rvRuangan.visibility = View.GONE
                    tvEmptyRuangan.visibility = View.VISIBLE
                } else {
                    rvRuangan.visibility = View.VISIBLE
                    tvEmptyRuangan.visibility = View.GONE
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Gagal memuat ruangan: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
