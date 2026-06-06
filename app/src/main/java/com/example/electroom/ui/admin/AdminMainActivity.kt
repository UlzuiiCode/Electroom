package com.example.electroom.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.electroom.R
import com.example.electroom.data.repository.AuthRepository
import com.example.electroom.ui.auth.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavAdmin)

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }
                R.id.nav_kelola -> {
                    loadFragment(KelolaRuanganFragment())
                    true
                }
                R.id.nav_approve_booking -> {
                    loadFragment(ApproveBookingFragment())
                    true
                }
                R.id.nav_approve_user -> {
                    loadFragment(ApproveUserFragment())
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    false
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
            .replace(R.id.fragmentContainerAdmin, fragment)
            .commit()
    }
}
