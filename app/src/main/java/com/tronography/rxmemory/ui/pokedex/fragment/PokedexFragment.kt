package com.tronography.rxmemory.ui.pokedex.fragment

import DEBUG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.databinding.FragmentPokedexBinding
import com.tronography.rxmemory.ui.pokedex.recyclerview.PokedexAdapter
import com.tronography.rxmemory.ui.pokedex.recyclerview.PokedexItemAnimator
import com.tronography.rxmemory.ui.pokedex.viewmodel.PokedexViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PokedexFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @LayoutRes
    private val layoutId = R.layout.fragment_pokedex

    private val bindingVariable = BR.viewModel

    private lateinit var viewDataBinding: FragmentPokedexBinding

    private lateinit var rootView: View

    private lateinit var pokedexAdapter: PokedexAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

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
        setUpRecyclerView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        observeLiveData()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    private fun observeLiveData() {
        observePokemonDatabase()
        observeOnPokemonSelected()
    }

    private fun observeOnPokemonSelected() {
        viewModel.getSelectedPokemon.observe(this, Observer { pokemon ->
            DEBUG("$pokemon clicked")
            pokemon?.let {
                Navigation.findNavController(activity!!, R.id.nav_host)
                        .navigate(R.id.action_pokedexFragment_to_pokedexEntryFragment)
            }
        })
    }

    private fun observePokemonDatabase() {
        viewModel.getPokemon().observe(this, Observer { pokemonEntries ->
            DEBUG("${pokemonEntries?.size} pokemon entries received ")
            pokemonEntries?.let { pokedexAdapter.updateList(pokemonEntries) }
        })
    }

    private fun setUpRecyclerView() {
        activity.let { context ->
            DEBUG("Setting up RecyclerView...")
            pokedexAdapter = PokedexAdapter(viewModel)
            with(viewDataBinding.recyclerView) {
                linearLayoutManager = LinearLayoutManager(context, VERTICAL, false)
                layoutManager = linearLayoutManager
                adapter = pokedexAdapter
                itemAnimator = PokedexItemAnimator()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        DEBUG("DETACHED")
    }

    companion object {
        const val TAG = "PokedexFragment"
    }

}
