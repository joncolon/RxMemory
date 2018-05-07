package com.tronography.rxmemory.ui.navigation

import android.support.v4.app.FragmentActivity
import com.tronography.rxmemory.R
import com.tronography.rxmemory.ui.game.fragments.GameFragment
import com.tronography.rxmemory.ui.game.fragments.GameOverFragment
import com.tronography.rxmemory.ui.home.fragment.HomeFragment
import com.tronography.rxmemory.ui.pokedex.fragment.PokedexFragment
import showFragment

object fragmentNavigator {

    fun showGameOverFragment(activity: FragmentActivity) {
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
                false
        )
    }

    fun showHomeFragment(activity: FragmentActivity) {
        activity.showFragment(
                HomeFragment(),
                R.id.fragment_container,
                HomeFragment.TAG,
                false
        )
    }

    fun showPokedexFragment(activity: FragmentActivity) {
        activity.showFragment(
                PokedexFragment(),
                R.id.fragment_container,
                PokedexFragment.TAG,
                false
        )
    }


}