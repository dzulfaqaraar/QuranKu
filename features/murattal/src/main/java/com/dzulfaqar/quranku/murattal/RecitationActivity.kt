package com.dzulfaqar.quranku.murattal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.di.RecitationModuleDependencies
import com.dzulfaqar.quranku.features.quran.QuranActivity
import com.dzulfaqar.quranku.murattal.databinding.ActivityRecitationBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import com.dzulfaqar.quranku.R as appR

class RecitationActivity : AppCompatActivity(R.layout.activity_recitation) {

    @Inject
    lateinit var factory: ViewModelFactory

    private val binding: ActivityRecitationBinding by viewBinding(R.id.container)
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

        window.decorView.setBackgroundColor(Color.TRANSPARENT)
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
