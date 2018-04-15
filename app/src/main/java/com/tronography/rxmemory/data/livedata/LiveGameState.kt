package com.tronography.rxmemory.data.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import com.tronography.rxmemory.data.livedata.GameState
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * @description: Broadcasts Game State as LiveData to Observers.
 * @see com.tronography.rxmemory.data.livedata.GameState
 */
class LiveGameState @Inject constructor() : LiveData<GameState>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    fun setGameState(value: GameState) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<GameState>) =
            super.observe(owner, Observer<GameState> { value ->
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(value)
                }
            })
}