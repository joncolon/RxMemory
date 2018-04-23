package com.tronography.rxmemory.ui.game

import DEBUG
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
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.databinding.FragmentGameBinding
import com.tronography.rxmemory.ui.game.adapter.GameAdapter
import com.tronography.rxmemory.ui.game.adapter.GameItemAnimator
import com.tronography.rxmemory.ui.layoutmanagers.SpanningGridLayoutManager
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import com.tronography.rxmemory.utilities.DiffCallback
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import toast
import javax.inject.Inject

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
        setUpSubscriptions()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
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

    private fun setUpSubscriptions() {
        disposables.addAll(
                subscribeToDeck(),
                subscribeToFlipCount(),
                subscribeToGameState()
        )
    }

    private fun subscribeToDeck(): Disposable? {
        return viewModel.getDeck()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { cards ->
                    cards?.let {
                        DEBUG("${cards.size} cards received ")
                        updateList(cards)
                    }
                }
    }

    private fun subscribeToFlipCount(): Disposable? {
        return viewModel.getFlippedCardCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { count ->
                    DEBUG("$count FLIPPED CARDS")
                    if (count < 2) {
                        adapter.enableCardClick()
                    } else {
                        adapter.disableCardClicks()
                    }
                    DEBUG("ADAPTER CLICKABLE = ${adapter.isClickable}")
                }
    }

    private fun subscribeToGameState(): Disposable? {
        return viewModel.getGameState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { gameState ->
                    DEBUG("GAME STATE : ${gameState.name}")
                    when (gameState.name) {
                        GameState.GAME_OVER.name -> {
                            activity?.toast("GAME OVER")
                            adapter.disableCardClicks()
                        }

                        GameState.IN_PROGRESS.name -> {
                            adapter.enableCardClick()
                        }
                    }
                }
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