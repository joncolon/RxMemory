package com.tronography.rxmemory.ui

import DEBUG
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.databinding.ActivityGameBinding
import com.tronography.rxmemory.injection.DaggerViewModelFactory
import com.tronography.rxmemory.ui.base.BaseActivity
import com.tronography.rxmemory.utilities.DiffCallback
import com.tronography.rxmemory.utilities.GridItemOffsetDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityGameBinding, MainActivityViewModel>() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var adapter: GameAdapter


    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_game

    override val viewModel: MainActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpRecyclerView()
        viewModel.onDeckChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { cards -> cards?.let {
                DEBUG(cards.toString())
                updateList(cards)
            }
        }
        viewModel.startGame()
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
}
