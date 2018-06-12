package com.tronography.rxmemory.ui.home.activity

import DEBUG
import ERROR
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.databinding.ActivityHomeBinding
import com.tronography.rxmemory.ui.base.BaseActivity
import com.tronography.rxmemory.ui.home.viewmodel.HomeViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HasSupportFragmentInjector,
        NavController.OnNavigatedListener {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        navController = findNavController(R.id.nav_host)
        navController.addOnNavigatedListener(this)
    }

    override fun onNavigated(controller: NavController, destination: NavDestination) {
        DEBUG("\n Navigation Listener : \n Controller $controller \n NavDestination : ${destination.id}")
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    lateinit var navController: NavController

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_home

    override val viewModel: HomeViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host).navigateUp()

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
