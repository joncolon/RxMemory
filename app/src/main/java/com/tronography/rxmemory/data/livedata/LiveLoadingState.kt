package com.tronography.rxmemory.data.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import com.tronography.rxmemory.data.livedata.LoadingState
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * @description: Broadcasts Loading state results as LiveData to Observers.
 * @see LoadingState
 */
class LiveLoadingState @Inject constructor() : LiveData<LoadingState>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    fun setLoadingState(value: LoadingState) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<LoadingState>) =
            super.observe(owner, Observer<LoadingState> { value ->
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(value)
                }
            })
}