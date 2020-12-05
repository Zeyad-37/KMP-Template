package com.remote_first.androidApp.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.remote_first.androidApp.databinding.ActivitySplashBinding
import com.remote_first.androidApp.main.MainActivity
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.Progress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashVM: SplashVM by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        lifecycleScope.launchWhenCreated {
            splashVM.bind().observe().collect {
                when (it) {
                    is SplashState -> binding.bindState(it)
                    is SplashEffect -> it.bindEffect()
                    is Progress -> toggleLoadingViews(it.isLoading)
                    is Error -> bindError(it.message)
                }
            }
            splashVM.processInitialize()
        }
    }

    private fun setupUI() {
        binding = ActivitySplashBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun ActivitySplashBinding.bindState(state: SplashState) {
        splashVM.processInitialize()
    }

    private fun SplashEffect.bindEffect() = when (this) {
        is ToMain -> {
            startActivity(MainActivity.makeIntent(this@SplashActivity))
            finish()
        }
    }

    private fun bindError(errorMessage: String) = Unit

    private fun toggleLoadingViews(isLoading: Boolean) = Unit
}