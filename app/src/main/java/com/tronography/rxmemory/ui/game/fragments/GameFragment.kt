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
import com.tronography.rxmemory.ui.common.layoutmanagers.SpanningGridLayoutManager
import com.tronography.rxmemory.ui.game.recyclerview.GameAdapter
import com.tronography.rxmemory.ui.game.recyclerview.GameItemAnimator
import com.tronography.rxmemory.ui.game.viewmodel.GameViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import com.tronography.rxmemory.utilities.GlideUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var glideUtils: GlideUtils

    @LayoutRes
    private val layoutId = R.layout.fragment_game

    private val bindingVariable = BR.viewModel

    private lateinit var viewDataBinding: FragmentGameBinding

    private lateinit var rootView: View

    private var gameAdapter: GameAdapter? = null

    lateinit var viewModel: GameViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        performDependencyInjection()
        DEBUG("ATTACHED")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        setOnClickListeners()
        return rootView
    }

    private fun setOnClickListeners() {
        viewDataBinding.gameOver.yesButton.setOnClickListener {
            viewModel.startGame()
        }
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
        observeGameState()
        observeLiveDeck()
        viewModel.getAttemptCount().observe(this, Observer { count ->
            count?.let {
                viewDataBinding.gameOver.attemptCountValueTv.text = count.toString()
            }
        })

        viewModel.getCardsMatched().observe(this, Observer { cards ->
            DEBUG("matched cards from viewmodel = $cards")
            cards?.let {
                with(viewDataBinding.gameOver) {
                    glideUtils.loadImage(cards[0].photoUrl, pokemonOne)
                    glideUtils.loadImage(cards[1].photoUrl, pokemonTwo)
                    glideUtils.loadImage(cards[2].photoUrl, pokemonThree)
                    glideUtils.loadImage(cards[3].photoUrl, pokemonFour)
                    glideUtils.loadImage(cards[4].photoUrl, pokemonFive)
                    glideUtils.loadImage(cards[5].photoUrl, pokemonSix)
                    glideUtils.loadImage(cards[6].photoUrl, pokemonSeven)
                    glideUtils.loadImage(cards[7].photoUrl, pokemonEight)
                }
            }
        })
    }

    private fun observeLiveDeck() {
        viewModel.observeDeck().observe(this, Observer { cards ->
            cards?.let {
                DEBUG("cards = ${cards.size}")
                gameAdapter?.updateList(cards)
            }
        })
    }

    private fun observeGameState() {
        viewModel.getGameState()
                .observe(this, Observer { gameState ->
                    DEBUG("GAME STATE : $gameState")
                    when (gameState) {
                        LOADING -> {
                            hideGameOverLayout()
                            setUpRecyclerView()
                        }

                        LOAD_COMPLETE -> {
                        }

                        IN_PROGRESS -> gameAdapter?.enableCardClick()

                        RESETTING_CARDS -> gameAdapter?.disableCardClicks()

                        GAME_OVER -> {
                            gameAdapter?.disableCardClicks()
                            showGameOverLayout()
                        }

                        ERROR -> {
                            //todo: show error status
                        }
                    }
                })
    }

    private fun showGameOverLayout() {
        viewDataBinding.gameOver.constraintLayout.visibility = View.VISIBLE
        viewDataBinding.recyclerView.visibility = View.INVISIBLE
    }

    private fun hideGameOverLayout() {
        viewDataBinding.gameOver.constraintLayout.visibility = View.GONE
        viewDataBinding.recyclerView.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        activity?.let {
            DEBUG("Setting up RecyclerView...")
            gameAdapter = GameAdapter(viewModel)

            with(viewDataBinding.recyclerView) {
                itemAnimator = GameItemAnimator()
                adapter = gameAdapter
                layoutManager = SpanningGridLayoutManager(it, 4, GridLayout.VERTICAL, false)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        DEBUG("DETACHED")
    }

    companion object {
        const val TAG = "GameFragment"
    }

}
