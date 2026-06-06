package com.example.electroom.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.electroom.R
import com.example.electroom.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etNim = findViewById<EditText>(R.id.etNim)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val spRole = findViewById<Spinner>(R.id.spRole)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvToLogin = findViewById<TextView>(R.id.tvToLogin)

        // Setup Spinner Role (Hanya Mahasiswa dan Dosen)
        val roles = arrayOf("Mahasiswa", "Dosen")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spRole.adapter = adapter

        // Logika untuk mengubah hint NIM menjadi NIP jika memilih Dosen
        spRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (roles[position] == "Dosen") {
                    etNim.hint = "NIP"
                } else {
                    etNim.hint = "NIM"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        tvToLogin.setOnClickListener {
            finish() // Kembali ke LoginActivity
        }

        btnRegister.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nim = etNim.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = spRole.selectedItem.toString().lowercase()

            if (nama.isEmpty() || nim.isEmpty() || email.isEmpty() || password.isEmpty()) {
                val label = if (role == "dosen") "NIP" else "NIM"
                Toast.makeText(this, "Semua data termasuk $label harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                authRepository.register(email, password, nama, nim, role).onSuccess {
                    Toast.makeText(this@RegisterActivity, "Registrasi Berhasil! Silakan tunggu persetujuan Admin sebelum login.", Toast.LENGTH_LONG).show()
                    finish()
                }.onFailure {
                    Toast.makeText(this@RegisterActivity, "Registrasi Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
