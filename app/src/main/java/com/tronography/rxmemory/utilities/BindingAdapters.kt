package com.tronography.rxmemory.utilities

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tronography.rxmemory.injection.modules.GlideApp
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType.TOP_LEFT
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType.TOP_RIGHT


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