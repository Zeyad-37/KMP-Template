package com.remote_first.androidApp.space_x

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.remote_first.androidApp.R
import com.remote_first.androidApp.databinding.SpaceXFragmentBinding
import com.remote_first.androidApp.space_x.vm.SpaceXVM
import com.remote_first.androidApp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SpaceXFragment : Fragment(R.layout.space_x_fragment) {

    private val spaceXVM: SpaceXVM by viewModels()
    private val binding: SpaceXFragmentBinding by viewBinding()
    private var launchesRvAdapter: LaunchesRvAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        lifecycleScope.launchWhenCreated {
//            spaceXVM.bind().observe().collect {
//                when (it) {
//                    is SpaceXState -> binding.bindState(it)
//                    is SpaceXEffect -> it.bindEffect()
//                    is Progress -> toggleLoadingViews(it.isLoading)
//                    is Error -> bindError(it.message)
//                }
//            }
//        }
        spaceXVM.bind().observe(lifecycle) {
            states { binding.bindState((it as SpaceXState)) }
            effects { (it as SpaceXEffect).bindEffect() }
            progress { toggleLoadingViews(it.isLoading) }
            errors { bindError(it.message) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchesRvAdapter = LaunchesRvAdapter(emptyList())
        with(binding) {
            launchesListRv.adapter = launchesRvAdapter
            launchesListRv.layoutManager = LinearLayoutManager(requireContext())
            swipeContainer.setOnRefreshListener { dispatchInputs() }
        }
    }

    override fun onResume() {
        super.onResume()
        dispatchInputs()
    }

    private fun dispatchInputs() {
        spaceXVM.processGetLaunches()
        spaceXVM.processShowEffect()
        spaceXVM.processDoNothing()
    }

    private fun SpaceXFragmentBinding.bindState(state: SpaceXState) {
        when (state) {
            InitialState -> requireActivity().title = "SpaceX Launches"
            is FullState -> {
                launchesRvAdapter?.launches = state.launches
                launchesRvAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun SpaceXEffect.bindEffect() {
        binding.launchesListRv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorSuccessful))
    }

    private fun bindError(errorMessage: String) =
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

    private fun toggleLoadingViews(isLoading: Boolean) {
        binding.swipeContainer.isRefreshing = isLoading
    }

    override fun onDestroyView() {
        launchesRvAdapter = null
        super.onDestroyView()
    }
}
