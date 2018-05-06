package com.tronography.rxmemory.data.state

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * @description: Broadcasts network status results as LiveData to Observers.
 * @see NetworkState
 */
class NetworkStateLiveData @Inject constructor() : LiveData<NetworkState>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    fun setNetworkState(value: NetworkState) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<NetworkState>) =
            super.observe(owner, Observer<NetworkState> { value ->
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(value)
                }
            })
}