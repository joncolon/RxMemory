package com.tronography.rxmemory.ui.base

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import com.tronography.rxmemory.ui.navigation.fragmentNavigator
import dagger.android.AndroidInjection


abstract class BaseActivity<T : ViewDataBinding, out V : ViewModel> : FragmentActivity() {

    lateinit var viewDataBinding: T

    abstract val viewModel: V

    abstract val bindingVariable: Int

    private var mViewModel: ViewModel? = null

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding.setVariable(bindingVariable, mViewModel)
        viewDataBinding.executePendingBindings()
    }

}