package com.tronography.rxmemory.ui.navigation

import android.app.Activity
import android.support.v4.app.FragmentActivity
import com.tronography.rxmemory.R
import com.tronography.rxmemory.ui.game.fragments.GameFragment
import com.tronography.rxmemory.ui.game.fragments.GameOverFragment
import com.tronography.rxmemory.ui.game.fragments.HomeFragment
import showFragment

object fragmentNavigator {

    fun showGameOverFragment(activity : FragmentActivity) {
        activity.showFragment(
                GameOverFragment(),
                R.id.fragment_container,
                GameOverFragment.TAG,
                false
        )
    }

    fun showGameFragment(activity: FragmentActivity) {
        activity.showFragment(
                GameFragment(),
                R.id.fragment_container,
                GameFragment.TAG,
                true
        )
    }

    fun showHomeFragment(activity : FragmentActivity) {
        activity.showFragment(
                HomeFragment(),
                R.id.fragment_container,
                HomeFragment.TAG,
                false
        )
    }


}