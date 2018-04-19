package com.tronography.rxmemory.ui.game

import DEBUG
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.databinding.ActivityGameBinding
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import com.tronography.rxmemory.ui.base.BaseActivity
import com.tronography.rxmemory.utilities.DiffCallback
import com.tronography.rxmemory.utilities.GridItemOffsetDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import toast
import javax.inject.Inject

class GameActivity : BaseActivity<ActivityGameBinding, GameViewModel>() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    private lateinit var adapter: GameAdapter

    private val disposables = CompositeDisposable()


    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_game

    override val viewModel: GameViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        setUpSubscriptions()
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
                    adapter.isClickable = count < 2
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
                        GameState.GAME_OVER.name -> this.toast("GAME OVER")
                    }
                }
    }

    private fun setUpRecyclerView() {
        adapter = GameAdapter(viewModel)
        val itemDecoration = GridItemOffsetDecorator(this, R.dimen.item_offset)
        viewDataBinding.recyclerView.adapter = adapter
        viewDataBinding.recyclerView.addItemDecoration(itemDecoration)
        viewDataBinding.recyclerView.itemAnimator = GameItemAnimator()
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
