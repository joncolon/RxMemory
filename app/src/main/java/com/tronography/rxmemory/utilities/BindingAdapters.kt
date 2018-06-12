package com.tronography.rxmemory.utilities

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tronography.rxmemory.injection.modules.GlideApp


object BindingAdapters {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String?) {
        loadImage(url, imageView)
    }

    private fun loadImage(url: String?, imageView: ImageView) {
        url?.let {
            GlideApp.with(imageView.context)
                    .load(url)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView)
        }
    }

}