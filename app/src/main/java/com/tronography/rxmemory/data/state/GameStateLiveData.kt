package com.tronography.rxmemory.data.state

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @description: Broadcasts network status results as LiveData to Observers.
 * @see GameState
 */
@Singleton
class GameStateLiveData @Inject constructor() : LiveData<GameState>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    fun setGameStateLiveData(value: GameState) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<GameState>) {
        super.observe(owner, observer)
        if (pending.compareAndSet(true, false)) {
            observer.onChanged(value)
        }
    }
}