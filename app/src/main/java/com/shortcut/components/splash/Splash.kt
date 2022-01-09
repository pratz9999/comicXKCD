package com.shortcut.components.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shortcut.components.dashboard.Dashboard
import com.shortcut.utils.Constants
import com.shortcut.xkcd.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(Constants.SPLASH_DELAY)
            gotoDashboard()
        }
    }

    /**
     * Generic method to Navigate
     */
    private fun gotoDashboard() {
        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }
}
