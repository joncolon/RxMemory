package com.tronography.rxmemory.data.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import com.tronography.rxmemory.data.livedata.NetworkError
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * @description: Broadcasts network status results as LiveData to Observers.
 * @see NetworkError
 */
class LiveNetworkState @Inject constructor() : LiveData<NetworkError>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    fun setNetworkError(value: NetworkError) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<NetworkError>) =
            super.observe(owner, Observer<NetworkError> { value ->
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(value)
                }
            })
}