package com.remote_first.splash

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.lifecycleScope
import com.remote_first.androidApp.databinding.ActivitySplashBinding
import com.remote_first.navigation.IMainActivityIntentFactory
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.Progress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1010
    }

    @Inject
    lateinit var mainActivityIntentFactory: IMainActivityIntentFactory

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
        }
    }

    private fun setupUI() {
        binding = ActivitySplashBinding.inflate(layoutInflater).apply { setContentView(root) }
    }

    private fun ActivitySplashBinding.bindState(state: SplashState) = when (state) {
        is InitialState -> splashVM.processInitialize()
        is RequestPermissions -> onRequestPermissions()
    }

    private fun onRequestPermissions() =
            requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            splashVM.processInitialize()
        }
    }

    private fun SplashEffect.bindEffect() = when (this) {
        is ToMain -> {
            startActivity(mainActivityIntentFactory.create(this@SplashActivity))
            finish()
        }
    }

    private fun bindError(errorMessage: String) = Unit

    private fun toggleLoadingViews(isLoading: Boolean) = Unit
}
