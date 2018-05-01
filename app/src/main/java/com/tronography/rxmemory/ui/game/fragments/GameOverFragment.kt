package com.tronography.rxmemory.ui.game.fragments

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
import com.tronography.rxmemory.databinding.FragmentGameOverBinding
import com.tronography.rxmemory.ui.game.viewmodel.GameViewModel
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameOverFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @LayoutRes
    private val layoutId = R.layout.fragment_game_over

    private val bindingVariable = BR.viewModel

    private lateinit var viewDataBinding: FragmentGameOverBinding

    private lateinit var rootView: View

    lateinit var viewModel: GameViewModel


    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(GameViewModel::class.java)
        } catch (error : Exception) {kotlin.error("Activity can not be null")}
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        setUpSubscriptions()
        setUpClickListeners()
        viewDataBinding.attemptCountValueTv.text = viewModel.getAttemptCount().toString()
    }

    private fun setUpClickListeners() {
        viewDataBinding.yesButton.setOnClickListener {
            viewModel.restartGame()
            onDestroy()
        }
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        const val TAG = "GameOverFragment"
        fun newInstance(): GameOverFragment {
            return GameOverFragment()
        }
    }

    private fun setUpSubscriptions() {
        //todo add subscriptions

    }

}