package com.dzulfaqar.quranku.features.main.juz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.ui.JuzAdapter
import com.dzulfaqar.quranku.databinding.FragmentJuzBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JuzFragment : Fragment(R.layout.fragment_juz) {

    private var _binding: FragmentJuzBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JuzViewModel by viewModels()
    private var juzAdapter: JuzAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentJuzBinding.bind(view)
        if (activity != null) {
            juzAdapter = JuzAdapter()
            juzAdapter?.onItemClick = { selectedData ->
                val action =
                    JuzFragmentDirections.actionJuzFragmentToQuranActivity(juz = selectedData)
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
            adapter = juzAdapter
        }
    }

    private fun setupObserver() {
        viewModel.listJuz.observe(viewLifecycleOwner) { state ->
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
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.syncFailed.root.visibility = View.GONE

                            juzAdapter?.submitList(state.data)
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
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
