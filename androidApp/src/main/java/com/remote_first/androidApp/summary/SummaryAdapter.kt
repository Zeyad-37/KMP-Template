package com.remote_first.androidApp.summary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SummaryAdapter(
        fm: FragmentManager,
        private val items: List<Pair<Fragment, String>>,
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = items[position].first

    override fun getPageTitle(position: Int): CharSequence = items[position].second

    override fun getCount(): Int = items.size
}
