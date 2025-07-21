package com.dzulfaqar.quranku.features.main.surah

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.databinding.FragmentSurahBinding
import com.dzulfaqar.quranku.ui.SurahAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurahFragment : Fragment(R.layout.fragment_surah) {

    private var _binding: FragmentSurahBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SurahViewModel by viewModels()
    private var surahAdapter: SurahAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSurahBinding.bind(view)
        if (activity != null) {
            surahAdapter = SurahAdapter()
            surahAdapter?.onItemClick = { selectedData ->
                val action =
                    SurahFragmentDirections.actionSurahFragmentToQuranActivity(surah = selectedData)
                findNavController().navigate(action)
            }

            setupView()
            setupObserver()
        }
    }

    private fun setupView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = surahAdapter
        }
    }

    private fun setupObserver() {
        viewModel.listSurah.observe(viewLifecycleOwner) { state ->
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
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.syncFailed.root.visibility = View.GONE

                        surahAdapter?.submitList(state.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.root.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.syncFailed.root.visibility = View.VISIBLE
                    Toast.makeText(
                        activity,
                        getString(R.string.something_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
