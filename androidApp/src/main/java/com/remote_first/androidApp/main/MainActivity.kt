package com.remote_first.androidApp.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.remote_first.androidApp.R
import com.remote_first.androidApp.databinding.ActivityMainBinding
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.Progress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import reactivecircus.flowbinding.material.itemSelections

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun makeIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private val mainVM: MainVM by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var mainAdapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        lifecycleScope.launchWhenCreated {
            mainVM.bind(mainVM.provideInitialState()).observe().collect {
                when (it) {
                    is MainState -> binding.bindState(it)
                    is MainEffect -> it.bindEffect()
                    is Progress -> toggleLoadingViews(it.isLoading)
                    is Error -> bindError(it.message)
                }
            }
        }
    }

    private fun setupUI() {
        mainAdapter = MainAdapter(this)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            bnvTabs.inflateMenu(R.menu.main_bottom_navigation_menu)
            lifecycleScope.launchWhenCreated {
                bnvTabs.itemSelections()
                        .drop(1)
                        .distinctUntilChanged()
                        .collect { mainVM.processTabClicked(it.itemId) }
            }
            vpTabs.isUserInputEnabled = false
            vpTabs.adapter = mainAdapter
        }
    }

    private fun ActivityMainBinding.bindState(state: MainState) {
        vpTabs.setCurrentItem(state.index, false)
        bnvTabs.menu[state.index].isChecked = true
        title = state.title
    }

    private fun MainEffect.bindEffect() = Unit

    private fun bindError(errorMessage: String) = Unit

    private fun toggleLoadingViews(isLoading: Boolean) = Unit

    override fun onDestroy() {
        mainAdapter = null
        super.onDestroy()
    }
}
