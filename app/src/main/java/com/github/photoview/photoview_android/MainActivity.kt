package com.github.photoview.photoview_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.photoview.photoview_android.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Context.appSharedPreferences() = AppSharedPreferences(
    this.getSharedPreferences(packageName + "_preferences", AppCompatActivity.MODE_PRIVATE))

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        // Launch welcome screen to request a token
        if (baseContext.appSharedPreferences().getToken() == null) {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }
}