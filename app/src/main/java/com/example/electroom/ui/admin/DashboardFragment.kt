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
import com.example.electroom.data.repository.BookingRepository
import com.example.electroom.data.repository.RuanganRepository
import com.example.electroom.ui.mahasiswa.BookingAdapter
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private val ruanganRepository = RuanganRepository()
    private val bookingRepository = BookingRepository()
    private lateinit var adapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvJumlahRuangan = view.findViewById<TextView>(R.id.tvJumlahRuangan)
        val tvBookingMenunggu = view.findViewById<TextView>(R.id.tvBookingMenunggu)
        val rvBookingTerbaru = view.findViewById<RecyclerView>(R.id.rvBookingTerbaru)
        val tvEmptyDashboard = view.findViewById<TextView>(R.id.tvEmptyDashboard)

        adapter = BookingAdapter()
        rvBookingTerbaru.layoutManager = LinearLayoutManager(requireContext())
        rvBookingTerbaru.adapter = adapter

        lifecycleScope.launch {
            // Load jumlah ruangan
            ruanganRepository.getAllRuangan().onSuccess { list ->
                tvJumlahRuangan.text = list.size.toString()
            }

            // Load booking menunggu
            bookingRepository.getBookingMenunggu().onSuccess { list ->
                tvBookingMenunggu.text = list.size.toString()
            }

            // Load semua booking terbaru
            bookingRepository.getAllBooking().onSuccess { list ->
                if (list.isEmpty()) {
                    rvBookingTerbaru.visibility = View.GONE
                    tvEmptyDashboard.visibility = View.VISIBLE
                } else {
                    rvBookingTerbaru.visibility = View.VISIBLE
                    tvEmptyDashboard.visibility = View.GONE
                    adapter.submitList(list.take(5))
                }
            }.onFailure {
                Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
