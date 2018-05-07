package com.tronography.rxmemory.ui.game.fragments

import DEBUG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
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
import com.tronography.rxmemory.ui.home.activity.HomeActivity
import com.tronography.rxmemory.ui.navigation.fragmentNavigator
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
        DEBUG("ATTACHED")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(GameViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        setUpClickListeners()
        viewModel.getAttemptCount().observe(this, Observer { count ->
            count?.let { displayAttemptCount(it) }
        })
    }

    private fun displayAttemptCount(attemptCount : Int) {
        viewDataBinding.attemptCountValueTv.text = attemptCount.toString()
    }

    private fun setUpClickListeners() {
        viewDataBinding.yesButton.setOnClickListener {
            viewModel.startGame()
            fragmentNavigator.showGameFragment(activity!!)
        }

        viewDataBinding.noButton.setOnClickListener {
            val intent = Intent(this.context, HomeActivity::class.java)
            startActivity(intent)
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
        const val TAG = "GameOverFragment"
    }

}