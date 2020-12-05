package com.remote_first.androidApp.notifications

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.remote_first.androidApp.R
import com.remote_first.androidApp.databinding.NotificationsFragmentBinding
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
class NotificationsFragment : Fragment(R.layout.notifications_fragment) {

    private val notificationVM: NotificationVM by viewModels()
    private val binding: NotificationsFragmentBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            notificationVM.bind().observe().collect {
                when (it) {
                    is NotificationsState -> binding.bindState(it)
                    is NotificationsEffect -> it.bindEffect()
                    is Progress -> toggleLoadingViews(it.isLoading)
                    is Error -> bindError(it.message)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun NotificationsFragmentBinding.bindState(state: NotificationsState) {
    }

    private fun NotificationsEffect.bindEffect() = Unit

    private fun bindError(errorMessage: String) =
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

    private fun toggleLoadingViews(isLoading: Boolean) = Unit
}
