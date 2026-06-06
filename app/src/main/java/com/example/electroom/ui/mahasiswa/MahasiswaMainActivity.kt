package com.example.electroom.ui.mahasiswa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.electroom.R
import com.example.electroom.data.repository.AuthRepository
import com.example.electroom.ui.auth.LoginActivity
import com.example.electroom.ui.shared.StatusBookingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MahasiswaMainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mahasiswa_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavMahasiswa)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_status -> {
                    loadFragment(StatusBookingFragment())
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    false // Return false so the logout item doesn't stay selected
                }
                else -> false
            }
        }
    }

    private fun logout() {
        authRepository.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMahasiswa, fragment)
            .commit()
    }
}
