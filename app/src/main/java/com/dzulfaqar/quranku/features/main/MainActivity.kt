package com.dzulfaqar.quranku.features.main

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        (navHostFragment as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar()
        setupWindowInsets()
        setupToolbar()
        setupObserver()

        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupStatusBar() {
        // Enable edge-to-edge display while keeping system bars visible
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30+ - Configure status bar appearance
            windowInsetsController.apply {
                // Keep system bars visible but configure appearance
                isAppearanceLightStatusBars = false // Dark content on light background
                isAppearanceLightNavigationBars = false
            }
        } else {
            // API 23+ - Use legacy system UI visibility flags for layout only
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        }
        
        // Set status bar color programmatically (deprecated but still functional)
        @Suppress("DEPRECATION")
        window.statusBarColor = getColor(R.color.primaryDarkColor)
        @Suppress("DEPRECATION")
        window.navigationBarColor = getColor(R.color.primaryDarkColor)
    }

    private fun setupWindowInsets() {
        // Handle window insets for edge-to-edge display
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            val windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(windowInsets)
            val systemBarsInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Apply minimal top padding to toolbar to account for status bar if needed
            val currentTopPadding = binding.layoutToolbar.root.paddingTop
            if (currentTopPadding < systemBarsInsets.top) {
                binding.layoutToolbar.root.setPadding(
                    binding.layoutToolbar.root.paddingLeft,
                    systemBarsInsets.top,
                    binding.layoutToolbar.root.paddingRight,
                    binding.layoutToolbar.root.paddingBottom
                )
            }
            
            // Apply minimal bottom padding to bottom navigation if needed  
            val currentBottomPadding = binding.bottomNavigation.paddingBottom
            if (currentBottomPadding < systemBarsInsets.bottom) {
                binding.bottomNavigation.setPadding(
                    binding.bottomNavigation.paddingLeft,
                    binding.bottomNavigation.paddingTop,
                    binding.bottomNavigation.paddingRight,
                    systemBarsInsets.bottom
                )
            }
            
            windowInsets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.layoutToolbar.toolbar)
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    private fun setupObserver() {
        viewModel.themeSetting.observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Reapply immersive mode after theme change
            setupStatusBar()
        }
    }
}
