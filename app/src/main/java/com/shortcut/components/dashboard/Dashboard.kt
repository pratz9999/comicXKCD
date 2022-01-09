package com.shortcut.components.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.shortcut.components.dashboard.common.SnackbarActivty
import com.shortcut.xkcd.R
import com.shortcut.xkcd.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity(), SnackbarActivty {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment

        NavigationUI.setupWithNavController(
            binding.bottomNav,
            navHostFragment.navController
        )
    }

    override fun getSnackbar(errorMsg: String): Snackbar {
        val snackBar = Snackbar.make(
            findViewById(R.id.coordinator_layout),
            errorMsg, Snackbar.LENGTH_LONG
        )
        snackBar.anchorView = binding.bottomNav
        return snackBar
    }

}