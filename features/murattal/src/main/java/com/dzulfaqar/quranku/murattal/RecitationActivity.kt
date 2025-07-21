package com.dzulfaqar.quranku.murattal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.di.RecitationModuleDependencies
import com.dzulfaqar.quranku.features.quran.QuranActivity
import com.dzulfaqar.quranku.murattal.databinding.ActivityRecitationBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import com.dzulfaqar.quranku.R as appR

class RecitationActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: ActivityRecitationBinding
    private val viewModel: RecitationViewModel by viewModels { factory }

    private var recitationAdapter: RecitationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerRecitationComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    RecitationModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityRecitationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar()
        setupWindowInsets()
        setupToolbar()

        recitationAdapter = RecitationAdapter { reciter ->
            val resultIntent = Intent()
            resultIntent.putExtra(QuranActivity.EXTRA_SELECTED_DATA, reciter)
            setResult(QuranActivity.RESULT_CODE, resultIntent)
            finish()
        }

        setupView()
        setupObserver()
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
        supportActionBar?.title = getString(appR.string.list_recitations)
    }

    private fun setupView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = recitationAdapter
        }
    }

    private fun setupObserver() {
        viewModel.listReciter.observe(this) { state ->
            if (state != null) {
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE

                        if (state.data?.isEmpty() == true) {
                            binding.recyclerView.visibility = View.GONE
                            binding.empty.visibility = View.VISIBLE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.empty.visibility = View.GONE

                            recitationAdapter?.submitList(state.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.empty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.adapter = null
    }
}
