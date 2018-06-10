package com.tronography.rxmemory.utilities

import DEBUG
import ERROR
import android.app.Application
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.tronography.rxmemory.injection.modules.GlideApp
import javax.inject.Inject


class GlideUtils @Inject constructor(val application: Application) {

    fun preloadImages(url: String) {

        GlideApp.with(application)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        ERROR("PRELOAD IMAGE FAILED : $e")
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                        DEBUG("PRELOAD IMAGE SUCCESS :\n resource = $resource \n dataSource = $dataSource")
                        return false
                    }

                })
                .preload()
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

    companion object {
        private const val CORNER_RADIUS = 2F
    }
}