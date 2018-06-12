package com.tronography.rxmemory.ui.pokedex.recyclerview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.animation.DecelerateInterpolator
import com.tronography.rxmemory.utilities.ViewUtils


class PokedexItemAnimator : DefaultItemAnimator() {

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun animateAdd(viewHolder: RecyclerView.ViewHolder): Boolean {
        runEnterAnimation(viewHolder as PokedexAdapter.EntryViewHolder, viewHolder.adapterPosition)
        return true
    }

    private fun runEnterAnimation(holder: PokedexAdapter.EntryViewHolder, adapterPosition: Int) {
        val screenHeight = ViewUtils.getScreenHeight(holder.itemView.context)
        holder.itemView.translationY = screenHeight.toFloat()

        val startDelay = (adapterPosition * 10).toLong()
        holder.itemView.animate()
                .translationY(0f)
                .setInterpolator(DECELERATE_INTERPOLATOR)
                .setDuration(ENTER_DURATION)
                .setStartDelay(startDelay)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        dispatchAddFinished(holder)
                    }
                })
                .start()
    }

    companion object {
        private val DECELERATE_INTERPOLATOR = DecelerateInterpolator(3f)
        private const val ENTER_DURATION: Long = 500
    }
}