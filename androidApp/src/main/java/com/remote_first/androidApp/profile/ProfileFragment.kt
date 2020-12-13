package com.remote_first.androidApp.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.remote_first.androidApp.R
import com.remote_first.androidApp.databinding.ProfileFragmentBinding
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
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val profileVM: ProfileVM by viewModels()
    private val binding: ProfileFragmentBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            profileVM.bind(profileVM.provideInitialState()).observe().collect {
                when (it) {
                    is ProfileState -> binding.bindState(it)
                    is ProfileEffect -> it.bindEffect()
                    is Progress -> toggleLoadingViews(it.isLoading)
                    is Error -> bindError(it.message)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun ProfileFragmentBinding.bindState(state: ProfileState) {
    }

    private fun ProfileEffect.bindEffect() = Unit

    private fun bindError(errorMessage: String) =
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

    private fun toggleLoadingViews(isLoading: Boolean) = Unit
}
