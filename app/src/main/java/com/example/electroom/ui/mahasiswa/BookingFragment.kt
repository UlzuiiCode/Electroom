package com.example.electroom.ui.mahasiswa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.electroom.R
import com.example.electroom.data.model.Booking
import com.example.electroom.data.repository.AuthRepository
import com.example.electroom.data.repository.BookingRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.util.Calendar

class BookingFragment : Fragment() {

    private val bookingRepository = BookingRepository()
    private val authRepository = AuthRepository()

    private var ruanganId = ""
    private var namaRuangan = ""
    private var tanggalDipilih = ""
    private var jamMulaiDipilih = ""
    private var jamSelesaiDipilih = ""

    companion object {
        fun newInstance(ruanganId: String, namaRuangan: String): BookingFragment {
            return BookingFragment().apply {
                arguments = Bundle().apply {
                    putString("ruanganId", ruanganId)
                    putString("namaRuangan", namaRuangan)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ruanganId = arguments?.getString("ruanganId") ?: ""
        namaRuangan = arguments?.getString("namaRuangan") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNamaRuangan = view.findViewById<TextView>(R.id.tvNamaRuanganBooking)
        val btnTanggal = view.findViewById<Button>(R.id.btnPilihTanggal)
        val btnJamMulai = view.findViewById<Button>(R.id.btnPilihJamMulai)
        val btnJamSelesai = view.findViewById<Button>(R.id.btnPilihJamSelesai)
        val etKeperluan = view.findViewById<TextInputEditText>(R.id.etKeperluan)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmitBooking)

        tvNamaRuangan.text = namaRuangan

        btnTanggal.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                tanggalDipilih = "$d/${m + 1}/$y"
                btnTanggal.text = tanggalDipilih
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnJamMulai.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, h, m ->
                jamMulaiDipilih = String.format("%02d:%02d", h, m)
                btnJamMulai.text = jamMulaiDipilih
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        btnJamSelesai.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, h, m ->
                jamSelesaiDipilih = String.format("%02d:%02d", h, m)
                btnJamSelesai.text = jamSelesaiDipilih
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        btnSubmit.setOnClickListener {
            val keperluan = etKeperluan.text.toString().trim()

            if (tanggalDipilih.isEmpty() || jamMulaiDipilih.isEmpty() ||
                jamSelesaiDipilih.isEmpty() || keperluan.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSubmit.isEnabled = false
            btnSubmit.text = "Mengirim..."

            lifecycleScope.launch {
                val user = authRepository.getCurrentUser()
                if (user == null) {
                    Toast.makeText(requireContext(), "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val booking = Booking(
                    userId = user.uid,
                    namaUser = user.nama,
                    roleUser = user.role,
                    ruanganId = ruanganId,
                    namaRuangan = namaRuangan,
                    tanggal = tanggalDipilih,
                    jamMulai = jamMulaiDipilih,
                    jamSelesai = jamSelesaiDipilih,
                    keperluan = keperluan
                )

                val result = bookingRepository.buatBooking(booking)
                result.onSuccess {
                    Toast.makeText(requireContext(), "Booking berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
                result.onFailure {
                    Toast.makeText(requireContext(), "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    btnSubmit.isEnabled = true
                    btnSubmit.text = "Kirim Booking"
                }
            }
        }
    }
}