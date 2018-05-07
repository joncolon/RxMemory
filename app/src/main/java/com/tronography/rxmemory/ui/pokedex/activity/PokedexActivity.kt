package com.tronography.rxmemory.ui.pokedex.activity

import ERROR
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.databinding.ActivityGameBinding
import com.tronography.rxmemory.databinding.ActivityPokedexBinding
import com.tronography.rxmemory.ui.base.BaseActivity
import com.tronography.rxmemory.ui.game.viewmodel.GameActivityViewModel
import com.tronography.rxmemory.ui.navigation.fragmentNavigator
import com.tronography.rxmemory.ui.pokedex.viewmodel.PokedexActivityViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class PokedexActivity : BaseActivity<ActivityPokedexBinding, PokedexActivityViewModel>(),
        HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_pokedex

    override val viewModel: PokedexActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(PokedexActivityViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNavigator.showPokedexFragment(this)
    }

    private fun enableBlueIndicator() {
        viewDataBinding.blueIndicator.setImageResource(R.drawable.gradient_blue_glow)
    }

    private fun disableBlueIndicator() {
        viewDataBinding.blueIndicator.setImageResource(R.drawable.gradient_blue_dim)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onDestroy() {
        super.onDestroy()
        ERROR("ON DESTROY CALLED")
    }

}
