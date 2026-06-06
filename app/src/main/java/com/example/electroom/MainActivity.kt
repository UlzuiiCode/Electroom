package com.example.electroom

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.electroom.data.repository.AuthRepository
import com.example.electroom.ui.admin.AdminMainActivity
import com.example.electroom.ui.auth.LoginActivity
import com.example.electroom.ui.mahasiswa.MahasiswaMainActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!authRepository.isLoggedIn()) {
            goTo(LoginActivity::class.java)
            return
        }

        lifecycleScope.launch {
            val user = authRepository.getCurrentUser()
            when (user?.role) {
                "admin" -> goTo(AdminMainActivity::class.java)
                else -> goTo(MahasiswaMainActivity::class.java)
            }
        }
    }

    private fun goTo(cls: Class<*>) {
        startActivity(Intent(this, cls).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}