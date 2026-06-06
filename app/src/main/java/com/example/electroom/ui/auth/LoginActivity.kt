package com.example.electroom.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.electroom.R
import com.example.electroom.data.repository.AuthRepository
import com.example.electroom.ui.admin.AdminMainActivity
import com.example.electroom.ui.mahasiswa.MahasiswaMainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvToRegister = findViewById<TextView>(R.id.tvToRegister)

        tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                authRepository.login(email, password).onSuccess { user ->
                    Toast.makeText(this@LoginActivity, "Selamat datang, ${user.nama}!", Toast.LENGTH_SHORT).show()
                    
                    val intent = if (user.role.lowercase() == "admin") {
                        Intent(this@LoginActivity, AdminMainActivity::class.java)
                    } else {
                        Intent(this@LoginActivity, MahasiswaMainActivity::class.java)
                    }
                    
                    startActivity(intent)
                    finish()
                }.onFailure {
                    Toast.makeText(this@LoginActivity, "Login Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
