package com.example.electroom.ui.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electroom.R
import com.example.electroom.data.model.Ruangan
import com.example.electroom.data.repository.RuanganRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class KelolaRuanganFragment : Fragment() {

    private val ruanganRepository = RuanganRepository()
    private lateinit var adapter: AdminRuanganAdapter
    private lateinit var rvKelolaRuangan: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kelola_ruangan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvKelolaRuangan = view.findViewById(R.id.rvKelolaRuangan)
        tvEmpty = view.findViewById(R.id.tvEmptyKelolaRuangan)

        adapter = AdminRuanganAdapter(
            onEdit = { ruangan -> showEditDialog(ruangan) },
            onDelete = { ruangan -> showDeleteDialog(ruangan) }
        )

        rvKelolaRuangan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@KelolaRuanganFragment.adapter
        }

        view.findViewById<FloatingActionButton>(R.id.fabAddRuangan).setOnClickListener {
            showTambahDialog()
        }

        loadRuangan()
    }

    private fun loadRuangan() {
        lifecycleScope.launch {
            ruanganRepository.getAllRuangan().onSuccess { list ->
                adapter.submitList(list)
                if (list.isEmpty()) {
                    rvKelolaRuangan.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                } else {
                    rvKelolaRuangan.visibility = View.VISIBLE
                    tvEmpty.visibility = View.GONE
                }
            }.onFailure {
                Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTambahDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val etNama = EditText(requireContext()).apply { hint = "Nama Ruangan" }
        val etKapasitas = EditText(requireContext()).apply {
            hint = "Kapasitas"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val etFasilitas = EditText(requireContext()).apply { hint = "Fasilitas (pisah koma)" }
        val etLantai = EditText(requireContext()).apply { hint = "Lantai" }

        layout.addView(etNama)
        layout.addView(etKapasitas)
        layout.addView(etFasilitas)
        layout.addView(etLantai)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Ruangan")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val ruangan = Ruangan(
                    nama = etNama.text.toString().trim(),
                    kapasitas = etKapasitas.text.toString().toIntOrNull() ?: 0,
                    fasilitas = etFasilitas.text.toString().trim(),
                    lantai = etLantai.text.toString().trim()
                )
                lifecycleScope.launch {
                    ruanganRepository.tambahRuangan(ruangan).onSuccess {
                        loadRuangan()
                        Toast.makeText(requireContext(), "Ruangan ditambahkan!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(ruangan: Ruangan) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val spinnerStatus = Spinner(requireContext()).apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("tersedia", "terpakai", "maintenance")
            )
            setSelection(listOf("tersedia", "terpakai", "maintenance").indexOf(ruangan.status))
        }

        layout.addView(TextView(requireContext()).apply { text = "Ubah Status:" })
        layout.addView(spinnerStatus)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit: ${ruangan.nama}")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val updated = ruangan.copy(status = spinnerStatus.selectedItem.toString())
                lifecycleScope.launch {
                    ruanganRepository.updateRuangan(updated).onSuccess {
                        loadRuangan()
                        Toast.makeText(requireContext(), "Status diperbarui!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteDialog(ruangan: Ruangan) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Ruangan")
            .setMessage("Yakin hapus ${ruangan.nama}?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    ruanganRepository.hapusRuangan(ruangan.id).onSuccess {
                        loadRuangan()
                        Toast.makeText(requireContext(), "Ruangan dihapus!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
