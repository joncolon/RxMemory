package com.tronography.rxmemory.utilities

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.v7.widget.CardView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.tronography.rxmemory.R
import com.tronography.rxmemory.injection.modules.GlideApp
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType.TOP_LEFT
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType.TOP_RIGHT
import java.util.concurrent.ExecutionException


object BindingAdapters {

    private const val CORNER_RADIUS = 2F

    /**
     * Bind Glide to an ImageView nested in a CardView.
     * The CORNER_RADIUS must match the cardCornerRadius of the parent CardView
     * to prevent distortion
     *
     * @param imageView The ImageView to bind Glide to
     * @param url The URL of the image to load
     */
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String?) {

        val topLeft = CORNER_RADIUS
        val topRight = CORNER_RADIUS

        val cornerTransformation: MultiTransformation<Bitmap> = MultiTransformation(
                RoundedCornersTransformation(ViewUtils.dpToPx(topLeft), 0, TOP_LEFT),
                RoundedCornersTransformation(ViewUtils.dpToPx(topRight), 0, TOP_RIGHT))

        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()



        url?.let {
            GlideApp.with(imageView.context)
                    .load(url)
                    .transform(Bitmap::class.java, cornerTransformation)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.gradient)
                    .into(imageView)
        }
    }

    /**
     * Bind color to a CardView's setCardBackgroundColor.
     */
    @BindingAdapter("cardBackgroundColor")
    @JvmStatic
    fun setCardBackgroundColor(cardView: CardView, @ColorInt color: Int) {
        cardView.setCardBackgroundColor(color)
    }

    private fun cachePhotos(source: String, view: ImageView) {
        val future = Glide.with(view.context)
                .load(source)
                .downloadOnly(100, 100)
        try {
            val cache = future.get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

    }

}