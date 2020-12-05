package com.remote_first.androidApp.summary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.remote_first.androidApp.R
import com.remote_first.androidApp.bookings.BookingsFragment
import com.remote_first.androidApp.databinding.SummaryTabFragmentBinding
import com.remote_first.androidApp.favorites.FavoritesFragment
import com.remote_first.androidApp.utils.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SummaryTabFragment : Fragment(R.layout.summary_tab_fragment) {

    private lateinit var summaryAdapter: SummaryAdapter
    private val binding: SummaryTabFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf(FavoritesFragment() to getString(R.string.favorites),
                BookingsFragment() to getString(R.string.bookings))
        summaryAdapter = SummaryAdapter(childFragmentManager, items)
        with(binding) {
            viewpager.adapter = summaryAdapter
            tabs.setupWithViewPager(viewpager)
        }
    }
}
