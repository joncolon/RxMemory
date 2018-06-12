package com.tronography.rxmemory.ui.home.fragment

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
import androidx.navigation.Navigation
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.databinding.FragmentHomeBinding
import com.tronography.rxmemory.ui.home.viewmodel.HomeViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import getResourceEntryName
import toast
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
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(HomeViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        setUpClickListeners()
        observeNetworkError()
        observeDatabaseCount()
        observeNavigationEvents()
    }

    private fun observeNavigationEvents() {
        viewModel.navigateToGame.observe(this, Observer { viewId ->
            DEBUG("$viewId clicked")
            Navigation.findNavController(activity!!, R.id.nav_host).navigate(R.id.action_homeFragment_to_gameFragment)
        })

        viewModel.navigateToPokedex.observe(this, Observer { viewId ->
            DEBUG("$viewId clicked")
            Navigation.findNavController(activity!!, R.id.nav_host).navigate(R.id.action_homeFragment_to_pokedexFragment)
        })
    }

    private fun observeDatabaseCount() {
        viewModel.getPokemonDatabaseCount().observe(this, Observer {count ->
            DEBUG("DB Count = $count")
            count?.let {
                if (count >= 8) {
                    hideProgressBar()
                } else {
                    showProgressBar()
                }

                if (count < 151) {
                    showDownloadCountStatus()
                    val remainingCount = "$count/151"
                    viewDataBinding.downloadCountTv.text = remainingCount
                } else {
                    hideDownloadCountStatus()
                }
            }

        })
    }

    fun observeNetworkError(){
        viewModel.getNetworkError().observe(this, Observer { error ->
            error?.let {
                activity!!.toast("ERROR")
            }
        })
    }

    private fun showDownloadCountStatus() {
        viewDataBinding.downloadingTv.visibility = View.VISIBLE
        viewDataBinding.downloadCountTv.visibility = View.VISIBLE
    }

    private fun hideDownloadCountStatus() {
        viewDataBinding.downloadingTv.visibility = View.GONE
        viewDataBinding.downloadCountTv.visibility = View.GONE
    }

    private fun hideProgressBar() {
        viewDataBinding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        viewDataBinding.progressBar.visibility = View.VISIBLE
    }

    private fun setUpClickListeners() {
        viewDataBinding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked(it.getResourceEntryName())
        }

        viewDataBinding.pokedexButton.setOnClickListener {
            viewModel.onPokedexButtonClicked(it.getResourceEntryName())
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
    }

}