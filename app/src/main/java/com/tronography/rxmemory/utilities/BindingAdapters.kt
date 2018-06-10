package com.tronography.rxmemory.utilities

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tronography.rxmemory.injection.modules.GlideApp


object BindingAdapters {

    private const val CORNER_RADIUS = 2F

    /**
     * Bind Glide to an ImageView nested in a CardView.
     *
     * @param imageView The ImageView to bind Glide to
     * @param url The URL of the card_front_image to load
     */
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String?) {
        loadImage(url, imageView)
    }

    fun loadImage(url: String?, imageView: ImageView) {
        url?.let {
            GlideApp.with(imageView.context)
                    .load(url)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView)
        }
    }

}