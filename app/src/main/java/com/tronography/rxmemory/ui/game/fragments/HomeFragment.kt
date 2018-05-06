package com.tronography.rxmemory.ui.game.fragments

import DEBUG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.databinding.FragmentHomeBinding
import com.tronography.rxmemory.ui.game.viewmodel.HomeViewModel
import com.tronography.rxmemory.ui.navigation.fragmentNavigator
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import getResourceEntryName
import showFragment
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @LayoutRes
    private val layoutId = R.layout.fragment_home

    private val bindingVariable = BR.viewModel

    private lateinit var viewDataBinding: FragmentHomeBinding

    private lateinit var rootView: View

    lateinit var viewModel: HomeViewModel


    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
        DEBUG("ATTACHED")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        setUpClickListeners()

        observeNavigationEvents()
    }

    private fun observeNavigationEvents() {
        viewModel.navigateToGameFragment.observe(this, Observer { viewId ->
            DEBUG("$viewId clicked")
            activity?.let { fragmentNavigator.showGameFragment(it) }
        })
    }

    private fun setUpClickListeners() {
        viewDataBinding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked(it.getResourceEntryName())
        }
    }


    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    override fun onDetach() {
        super.onDetach()
        DEBUG("DETACHED")
    }

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}