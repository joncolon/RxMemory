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
import android.widget.GridLayout
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.data.state.GameState.*
import com.tronography.rxmemory.databinding.FragmentGameBinding
import com.tronography.rxmemory.ui.game.adapter.GameAdapter
import com.tronography.rxmemory.ui.game.adapter.GameItemAnimator
import com.tronography.rxmemory.ui.game.viewmodel.GameViewModel
import com.tronography.rxmemory.ui.layoutmanagers.SpanningGridLayoutManager
import com.tronography.rxmemory.ui.navigation.fragmentNavigator
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @LayoutRes
    private val layoutId = R.layout.fragment_game

    private val bindingVariable = BR.viewModel

    private lateinit var viewDataBinding: FragmentGameBinding

    private lateinit var rootView: View

    private lateinit var adapter: GameAdapter

    private lateinit var spanningGridLayoutManager: SpanningGridLayoutManager

    lateinit var viewModel: GameViewModel


    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
        DEBUG("ATTACHED")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        initAdapter()
        setUpRecyclerView()
        observeLiveData()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    private fun observeLiveData() {
        observeGameState()
        observeLiveDeck()
    }

    private fun observeLiveDeck() {
        viewModel.observeDeck().observe(this, Observer { cards ->
            DEBUG("${cards?.size} received ")
            DEBUG("${cards} received ")

            cards?.let {
                when (cards.size == FULL_DECK_SIZE) {
                    true -> adapter.updateList(cards)
                }
            }
        })
    }

    private fun observeGameState() {
        viewModel.getGameState()
                .observe(this, Observer { gameState ->
                    DEBUG("GAME STATE : ${gameState}")
                    when (gameState) {

                        LOADING -> {
                            //todo: show loading status
                        }

                        LOAD_COMPLETE -> {
                        }

                        IN_PROGRESS -> adapter.enableCardClick()

                        RESETTING_CARDS -> adapter.disableCardClicks()

                        GAME_OVER -> {
                            adapter.disableCardClicks()
                            showGameOverFragment()
                        }

                    }
                })
    }

    private fun showGameOverFragment() {
        activity?.let { fragmentNavigator.showGameOverFragment(it) }
    }

    private fun setUpRecyclerView() {
        activity?.let {
            DEBUG("Setting up RecyclerView...")
            spanningGridLayoutManager = SpanningGridLayoutManager(it, 4, GridLayout.VERTICAL, false)
            viewDataBinding.recyclerView.layoutManager = spanningGridLayoutManager
            viewDataBinding.recyclerView.setHasFixedSize(true)
            viewDataBinding.recyclerView.itemAnimator = GameItemAnimator()
            viewDataBinding.recyclerView.itemAnimator.addDuration = 0
            viewDataBinding.recyclerView.adapter = adapter
        }
    }

    private fun initAdapter() {
        adapter = GameAdapter(viewModel)
        adapter.setHasStableIds(true)
    }

    override fun onDetach() {
        super.onDetach()
        DEBUG("DETACHED")
    }

    companion object {
        private const val FULL_DECK_SIZE = 16
        const val TAG = "GameFragment"
    }

}
