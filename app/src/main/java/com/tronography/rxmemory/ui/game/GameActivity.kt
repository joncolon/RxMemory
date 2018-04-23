package com.tronography.rxmemory.ui.game

import DEBUG
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tronography.rxmemory.BR
import com.tronography.rxmemory.R
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.databinding.ActivityGameBinding
import com.tronography.rxmemory.ui.base.BaseActivity
import com.tronography.rxmemory.utilities.DaggerViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import toast
import javax.inject.Inject

class GameActivity : BaseActivity<ActivityGameBinding, GameViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val disposables = CompositeDisposable()

    override val bindingVariable: Int
        get() = BR.viewModel


    override val layoutId: Int
        get() = R.layout.activity_game

    override val viewModel: GameViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showGameFragment()
        subscribeToGameState()
    }

    private fun showGameFragment() {
        supportFragmentManager
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.fragment_container, GameFragment.newInstance(), GameFragment.TAG)
                .commit()
    }

    private fun clearDisposables() {
        DEBUG("Clearing Disposables")
        disposables.dispose()
        DEBUG("Disposables cleared : ${disposables.isDisposed}")
    }

    private fun subscribeToGameState(): Disposable? {
        return viewModel.getGameState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { gameState ->
                    DEBUG("GAME STATE : ${gameState.name}")
                    when (gameState.name) {
                        GameState.GAME_OVER.name -> {
                            this.toast("GAME OVER")
                            disableBlueIndicator()
                        }

                        GameState.IN_PROGRESS.name -> {
                            enableBlueIndicator()
                        }
                    }
                }
    }

    private fun enableBlueIndicator() {
        viewDataBinding.blueIndicator.setImageResource(R.drawable.gradient_blue_glow)
    }

    private fun disableBlueIndicator() {
        viewDataBinding.blueIndicator.setImageResource(R.drawable.gradient_blue_dim)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposables()
    }
}
