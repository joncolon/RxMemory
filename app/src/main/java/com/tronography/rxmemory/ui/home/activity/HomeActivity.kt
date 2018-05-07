package com.tronography.rxmemory.ui.home.activity

import ERROR
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.databinding.ActivityHomeBinding
import com.tronography.rxmemory.ui.base.BaseActivity
import com.tronography.rxmemory.ui.home.fragment.HomeFragment
import com.tronography.rxmemory.ui.home.viewmodel.HomeActivityViewModel
import com.tronography.rxmemory.ui.navigation.fragmentNavigator
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_home

    override val viewModel: HomeActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(HomeActivityViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNavigator.showHomeFragment(this)
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

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG)
        when (homeFragment) {
            null -> fragmentNavigator.showHomeFragment(this)
            else -> super.onBackPressed()
        }
    }

}
