package com.remote_first.androidApp.explore

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.remote_first.androidApp.R
import com.remote_first.androidApp.databinding.ExploreFragmentBinding
import com.remote_first.androidApp.utils.viewBinding
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.Progress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.explore_fragment) {

    private val exploreVM: ExploreVM by viewModels()
    private val binding: ExploreFragmentBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            exploreVM.bind(exploreVM.provideInitialState()).observe().collect {
                when (it) {
                    is ExploreState -> binding.bindState(it)
                    is ExploreEffect -> it.bindEffect()
                    is Progress -> toggleLoadingViews(it.isLoading)
                    is Error -> bindError(it.message)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun ExploreFragmentBinding.bindState(state: ExploreState) {
    }

    private fun ExploreEffect.bindEffect() = Unit

    private fun bindError(errorMessage: String) =
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

    private fun toggleLoadingViews(isLoading: Boolean) = Unit
}
