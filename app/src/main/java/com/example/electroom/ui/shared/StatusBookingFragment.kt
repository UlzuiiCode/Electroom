package com.example.electroom.ui.shared

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
import com.example.electroom.data.repository.BookingRepository
import com.example.electroom.ui.mahasiswa.BookingAdapter
import kotlinx.coroutines.launch

class StatusBookingFragment : Fragment() {

    private val bookingRepository = BookingRepository()
    private val authRepository = AuthRepository()
    private lateinit var adapter: BookingAdapter
    private lateinit var rvStatusBooking: RecyclerView
    private lateinit var tvEmptyBookingStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_status_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvStatusBooking = view.findViewById(R.id.rvStatusBooking)
        tvEmptyBookingStatus = view.findViewById(R.id.tvEmptyBookingStatus)

        adapter = BookingAdapter()

        rvStatusBooking.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@StatusBookingFragment.adapter
        }

        loadBooking()
    }

    private fun loadBooking() {
        lifecycleScope.launch {
            val user = authRepository.getCurrentUser() ?: return@launch
            val result = bookingRepository.getBookingByUser(user.uid)
            result.onSuccess { list ->
                adapter.submitList(list)
                if (list.isEmpty()) {
                    rvStatusBooking.visibility = View.GONE
                    tvEmptyBookingStatus.visibility = View.VISIBLE
                } else {
                    rvStatusBooking.visibility = View.VISIBLE
                    tvEmptyBookingStatus.visibility = View.GONE
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Gagal memuat: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
