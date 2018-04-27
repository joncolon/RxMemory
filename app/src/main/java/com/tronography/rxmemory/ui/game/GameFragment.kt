package com.tronography.rxmemory.ui.game

import DEBUG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.data.state.GameState.*
import com.tronography.rxmemory.databinding.FragmentGameBinding
import com.tronography.rxmemory.ui.game.adapter.GameAdapter
import com.tronography.rxmemory.ui.game.adapter.GameItemAnimator
import com.tronography.rxmemory.ui.layoutmanagers.SpanningGridLayoutManager
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import com.tronography.rxmemory.utilities.DiffCallback
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @LayoutRes
    private val layoutId = R.layout.fragment_game

    private val bindingVariable = BR.viewModel

    private val disposables = CompositeDisposable()

    private lateinit var viewDataBinding: FragmentGameBinding

    private lateinit var rootView: View

    private lateinit var adapter: GameAdapter

    private lateinit var spanningGridLayoutManager: SpanningGridLayoutManager

    lateinit var viewModel: GameViewModel


    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
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
        setUpRecyclerView()
        observeLiveData()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        private const val MAX_FLIP_COUNT = 2
        const val TAG = "GameFragment"
        fun newInstance(): GameFragment {
            return GameFragment()
        }
    }

    private fun clearDisposables() {
        DEBUG("Clearing Disposables")
        disposables.dispose()
        DEBUG("Disposables cleared : ${disposables.isDisposed}")
    }

    private fun observeLiveData() {
        observeGameState()
        observeFlipCount()
        observeLiveDeck()
    }

    private fun observeFlipCount() {
        DEBUG("subscribing to flip count")
        viewModel.observeFlipCount()
                .observe(this, Observer { count ->
                    DEBUG("$count FLIPPED CARDS")
                    count?.let {
                        when {
                            count < MAX_FLIP_COUNT -> adapter.enableCardClick()
                            else -> adapter.disableCardClicks()
                        }
                        DEBUG("ADAPTER CLICKABLE = ${adapter.isClickable}")
                    }

                }
                )
    }

    private fun observeLiveDeck() {
        viewModel.observeDeck().observe(this, Observer { cards ->
            cards?.let {
                DEBUG("$cards received ")
                when (cards.size == 16) {
                    true -> updateList(cards)
                }
            }
        })
    }

    private fun observeGameState() {
        DEBUG("subscribing to game state")
        viewModel.getGameState()
                .observe(this, Observer { gameState ->
                    DEBUG("GAME STATE : ${gameState}")
                    when (gameState) {

                        GAME_OVER -> {
                            adapter.disableCardClicks()
                            onDestroy()
                        }

                        IN_PROGRESS -> {
                            adapter.enableCardClick()
                        }

                        NOT_IN_PROGRESS -> adapter.disableCardClicks()

                        LOADING -> adapter.disableCardClicks()

                        RESETTING_CARDS -> adapter.disableCardClicks()

                        else -> {
                            //do nothing
                        }
                    }
                })
    }

    private fun setUpRecyclerView() {
        activity?.let {
            adapter = GameAdapter(viewModel)
            spanningGridLayoutManager = SpanningGridLayoutManager(it, 4)
            viewDataBinding.recyclerView.layoutManager = spanningGridLayoutManager
            viewDataBinding.recyclerView.adapter = adapter
            viewDataBinding.recyclerView.itemAnimator = GameItemAnimator()
        }
    }

    private fun updateList(cards: List<Card>) {
        val oldCards = adapter.cards
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(DiffCallback(cards, oldCards))
        adapter.clearItems()
        adapter.addItems(cards)
        result.dispatchUpdatesTo(adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposables()
    }


}