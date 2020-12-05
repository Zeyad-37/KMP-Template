package com.remote_first.androidApp.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.remote_first.androidApp.R
import com.remote_first.androidApp.explore.ExploreFragment
import com.remote_first.androidApp.main.MainVM.Companion.BOTTOM_NAVIGATION_SIZE
import com.remote_first.androidApp.main.MainVM.Companion.EXPLORE_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.NOTIFICATIONS_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.PROFILE_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.SPACE_X_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.SUMMARY_TAB_INDEX
import com.remote_first.androidApp.notifications.NotificationsFragment
import com.remote_first.androidApp.profile.ProfileFragment
import com.remote_first.androidApp.space_x.SpaceXFragment
import com.remote_first.androidApp.summary.SummaryTabFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class MainAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val spaceXTabId: Long = R.id.action_space_x.toLong()
    private val exploreTabId: Long = R.id.action_explore.toLong()
    private val summaryTabId: Long = R.id.action_summary.toLong()
    private val notificationsTabId: Long = R.id.action_notifications.toLong()
    private val profileTabId: Long = R.id.action_profile.toLong()

    override fun getItemCount(): Int = BOTTOM_NAVIGATION_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            SPACE_X_TAB_INDEX -> SpaceXFragment()
            EXPLORE_TAB_INDEX -> ExploreFragment()
            SUMMARY_TAB_INDEX -> SummaryTabFragment()
            NOTIFICATIONS_TAB_INDEX -> NotificationsFragment()
            PROFILE_TAB_INDEX -> ProfileFragment()
            else -> throw IndexOutOfBoundsException(
                    "Unexpected position index, cannot create a fragment for this index: $position"
            )
        }
    }

    override fun getItemId(position: Int): Long =
            when (position) {
                SPACE_X_TAB_INDEX -> spaceXTabId
                EXPLORE_TAB_INDEX -> exploreTabId
                SUMMARY_TAB_INDEX -> summaryTabId
                NOTIFICATIONS_TAB_INDEX -> notificationsTabId
                PROFILE_TAB_INDEX -> profileTabId
                else -> throw IndexOutOfBoundsException(
                        "Unexpected position index, cannot create a fragment for this index: $position"
                )
            }

    override fun containsItem(itemId: Long): Boolean =
            itemId == profileTabId || itemId == spaceXTabId || itemId == exploreTabId || itemId == summaryTabId ||
                    itemId == notificationsTabId
}
