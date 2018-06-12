package com.tronography.rxmemory.ui.pokedex.entry.fragment

import DEBUG
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
import com.tronography.rxmemory.databinding.FragmentPokedexEntryBinding
import com.tronography.rxmemory.ui.pokedex.viewmodel.PokedexViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PokedexEntryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @LayoutRes
    private val layoutId = R.layout.fragment_pokedex_entry

    private val bindingVariable = BR.viewModel

    private lateinit var viewDataBinding: FragmentPokedexEntryBinding

    private lateinit var rootView: View

    lateinit var viewModel: PokedexViewModel


    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
        DEBUG("ATTACHED")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(PokedexViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    override fun onDetach() {
        super.onDetach()
        DEBUG("DETACHED")
    }

    companion object {
        const val TAG = "PokedexEntryFragment"
    }

}
