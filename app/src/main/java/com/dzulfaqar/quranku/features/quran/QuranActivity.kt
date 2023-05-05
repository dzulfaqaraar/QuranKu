package com.dzulfaqar.quranku.features.quran

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.ui.AyatAdapter
import com.dzulfaqar.quranku.databinding.ActivityQuranBinding
import com.dzulfaqar.quranku.player.AudioPlayer
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuranActivity : AppCompatActivity(R.layout.activity_quran), AudioPlayer.AudioPlayerListener {

    private val binding: ActivityQuranBinding by viewBinding(R.id.container)
    private val viewModel: QuranViewModel by viewModels()
    private val args: QuranActivityArgs by navArgs()

    private var ayatAdapter: AyatAdapter? = null
    private var audioPlayer: AudioPlayer? = null

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_CODE && result.data != null) {
            recitationSelected(result.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        if (args.surah != null) {
            viewModel.isFromSurah = true
        } else if (args.juz != null) {
            viewModel.isFromSurah = false
        }

        audioPlayer = AudioPlayer.getInstance(this)

        ayatAdapter = AyatAdapter { ayat, isBookmark ->
            viewModel.bookmarkAyat(ayat, isBookmark)
        }

        getExtras()
        setupView()
        setupObserver()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.layoutToolbar.toolbar)
        supportActionBar?.also {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun getExtras() {
        if (args.surah != null) {
            viewModel.surah = args.surah
            supportActionBar?.title = viewModel.surah?.name
        } else if (args.juz != null) {
            viewModel.juz = args.juz
            supportActionBar?.title = String.format("Juz %s", viewModel.juz?.number)
        }
    }

    private fun setupView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = ayatAdapter
        }

        with(binding) {
            playRecitation.setOnClickListener {
                if (viewModel.isAudioPaused) {
                    audioPlayer?.resume()
                    viewModel.resumeAudioPlayer()

                    playRecitation.visibility = View.GONE
                    pauseRecitation.visibility = View.VISIBLE
                } else {
                    if (viewModel.selectedReciter?.name?.isEmpty() == true) {
                        showErrorToast(R.string.select_recitation)
                    } else {
                        if (recitationName.text.toString() != getString(R.string.no_recitation)) {
                            viewModel.setLatestRecitation()
                        }

                        playAudio()
                    }
                }
            }

            pauseRecitation.setOnClickListener {
                audioPlayer?.pause()
                viewModel.pauseAudioPlayer()
            }

            recitationName.setOnClickListener {
                installMurattalModule()
            }
        }
    }

    private fun setupObserver() {
        viewModel.listAyatData.observe(this) { data ->
            ayatAdapter?.submitList(data)
        }

        viewModel.listAyatState.observe(this) { state ->
            if (state != null) {
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.root.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.syncFailed.root.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.root.visibility = View.GONE

                        if (state.data?.isEmpty() == true) {
                            binding.recyclerView.visibility = View.GONE
                            binding.syncFailed.root.visibility = View.VISIBLE
                            binding.layoutRecitation.visibility = View.GONE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.syncFailed.root.visibility = View.GONE
                            binding.layoutRecitation.visibility =
                                if (viewModel.isFromSurah == true) View.VISIBLE
                                else View.GONE
                        }

                        loadData()
                    }
                    is Resource.Error -> {
                        binding.progressBar.root.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.syncFailed.root.visibility = View.VISIBLE
                        binding.layoutRecitation.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.latestRecitation.observe(this) {
            viewModel.selectedReciter = it
            if (viewModel.selectedReciter?.name?.isEmpty() == true) {
                binding.recitationName.text = getString(R.string.no_recitation)
            } else {
                binding.recitationName.text = viewModel.selectedReciter?.name ?: ""
            }
        }

        viewModel.eventAudioPlaying.observe(this) { event ->
            event.getContentIfNotHandled()?.let { state ->
                with(binding) {
                    if (state) {
                        playRecitation.visibility = View.GONE
                        pauseRecitation.visibility = View.VISIBLE

                        audioPlayer?.play(viewModel.requiredDownload)
                    } else {
                        playRecitation.visibility = View.VISIBLE
                        pauseRecitation.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.listAudioFiles.observe(this) { state ->
            if (state != null) {
                when (state) {
                    is Resource.Loading -> {
                        binding.playRecitation.visibility = View.GONE
                        binding.progressBarAudio.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.playRecitation.visibility = View.VISIBLE
                        binding.progressBarAudio.visibility = View.GONE

                        if (state.data?.isEmpty() == true) {
                            showErrorToast(R.string.data_not_found)
                        } else {
                            viewModel.playAudioPlayer(state.data)
                        }
                    }
                    is Resource.Error -> showErrorToast(R.string.something_wrong)
                }
            }
        }
    }

    private fun loadData() {
        if (args.surah != null) {
            viewModel.getAllAyatBySurah(viewModel.surah)
        } else if (args.juz != null) {
            viewModel.getAllAyatByJuz(viewModel.juz)
        }
    }

    private fun installMurattalModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleMurattal = "murattal"
        if (splitInstallManager.installedModules.contains(moduleMurattal)) {
            showRecitationActivity()
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleMurattal)
                .build()
            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    showRecitationActivity()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error installing module", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showRecitationActivity() {
        val recitation = Class.forName("com.dzulfaqar.quranku.murattal.RecitationActivity")
        val moveForResultIntent = Intent(this, recitation)
        resultLauncher.launch(moveForResultIntent)
    }

    private fun recitationSelected(data: Intent?) {
        viewModel.selectedReciter = data?.getParcelableExtra(EXTRA_SELECTED_DATA)
        with(binding) {
            if (viewModel.selectedReciter?.style != null) {
                recitationName.text =
                    String.format(
                        "%s - %s",
                        viewModel.selectedReciter?.name,
                        viewModel.selectedReciter?.style
                    )
            } else {
                recitationName.text = String.format("%s", viewModel.selectedReciter?.name)
            }
        }

        audioPlayer?.reset()
        viewModel.resetPlayer()
    }

    private fun playAudio() {
        viewModel.surah?.id?.let { surahId ->
            viewModel.selectedReciter?.let { selectedReciter ->
                viewModel.getRecitationByChapter(selectedReciter.id, surahId)
            }
        }
    }

    private fun showErrorToast(msg: Int) {
        Toast.makeText(this, getString(msg), Toast.LENGTH_SHORT).show()
    }

    override fun onFinish() {
        with(binding) {
            playRecitation.visibility = View.VISIBLE
            pauseRecitation.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.adapter = null
        audioPlayer?.release()
        audioPlayer = null
    }

    companion object {
        const val RESULT_CODE = 100
        const val EXTRA_SELECTED_DATA = "extra_selected_data"
    }
}
