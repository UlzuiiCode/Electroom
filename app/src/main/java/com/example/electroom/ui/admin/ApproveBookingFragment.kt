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
import kotlinx.coroutines.launch

class ApproveBookingFragment : Fragment() {

    private val bookingRepository = BookingRepository()
    private lateinit var adapter: ApproveBookingAdapter
    private lateinit var rvApproveBooking: RecyclerView
    private lateinit var tvEmptyBooking: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_approve_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvApproveBooking = view.findViewById(R.id.rvApproveBooking)
        tvEmptyBooking = view.findViewById(R.id.tvEmptyBooking)

        adapter = ApproveBookingAdapter(
            onApprove = { booking ->
                lifecycleScope.launch {
                    bookingRepository.updateStatusBooking(booking.id, "disetujui").onSuccess {
                        Toast.makeText(requireContext(), "Booking disetujui!", Toast.LENGTH_SHORT).show()
                        loadBooking()
                    }
                }
            },
            onReject = { booking ->
                lifecycleScope.launch {
                    bookingRepository.updateStatusBooking(booking.id, "ditolak").onSuccess {
                        Toast.makeText(requireContext(), "Booking ditolak!", Toast.LENGTH_SHORT).show()
                        loadBooking()
                    }
                }
            }
        )

        rvApproveBooking.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@ApproveBookingFragment.adapter
        }

        loadBooking()
    }

    private fun loadBooking() {
        lifecycleScope.launch {
            bookingRepository.getBookingMenunggu().onSuccess { list ->
                adapter.submitList(list)
                if (list.isEmpty()) {
                    rvApproveBooking.visibility = View.GONE
                    tvEmptyBooking.visibility = View.VISIBLE
                } else {
                    rvApproveBooking.visibility = View.VISIBLE
                    tvEmptyBooking.visibility = View.GONE
                }
            }.onFailure {
                Toast.makeText(requireContext(), "Gagal memuat: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
