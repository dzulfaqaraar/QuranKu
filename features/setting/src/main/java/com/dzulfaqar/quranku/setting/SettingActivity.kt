package com.dzulfaqar.quranku.setting

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.dzulfaqar.quranku.di.SettingModuleDependencies
import com.dzulfaqar.quranku.setting.databinding.ActivitySettingBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import com.dzulfaqar.quranku.R as appR

class SettingActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSettingComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    SettingModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar()
        setupWindowInsets()
        setupToolbar()
        setupObserver()
        setupView()
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
        window.statusBarColor = getColor(appR.color.primaryDarkColor)
        @Suppress("DEPRECATION")
        window.navigationBarColor = getColor(appR.color.primaryDarkColor)
    }

    private fun setupWindowInsets() {
        // Handle window insets for edge-to-edge display
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            val windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(windowInsets)
            val systemBarsInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Apply minimal top padding to toolbar to account for status bar if needed
            val currentTopPadding = binding.toolbar.paddingTop
            if (currentTopPadding < systemBarsInsets.top) {
                binding.toolbar.setPadding(
                    binding.toolbar.paddingLeft,
                    systemBarsInsets.top,
                    binding.toolbar.paddingRight,
                    binding.toolbar.paddingBottom
                )
            }
            
            windowInsets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.also {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(appR.drawable.ic_arrow_back)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setupObserver() {
        viewModel.themeSetting.observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }
    }

    private fun setupView() {
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }
}
