package com.tronography.rxmemory.ui.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.animation.DecelerateInterpolator
import com.tronography.rxmemory.utilities.ViewUtils
import java.util.*

class GameItemAnimator : DefaultItemAnimator() {

    private var animationOneMap: MutableMap<RecyclerView.ViewHolder, AnimatorSet> = HashMap()
    private var animationTwoMap: MutableMap<RecyclerView.ViewHolder, ObjectAnimator> = HashMap()

    private var lastAddAnimatedItem = -2

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun recordPreLayoutInformation(
            state: RecyclerView.State,
            viewHolder: RecyclerView.ViewHolder,
            changeFlags: Int, payloads: List<Any>): RecyclerView.ItemAnimator.ItemHolderInfo {

        if (changeFlags == RecyclerView.ItemAnimator.FLAG_CHANGED) {
            return MatchItemHolderInfo()
        }
        
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }

    override fun animateAdd(viewHolder: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.layoutPosition > lastAddAnimatedItem) {
            lastAddAnimatedItem++
            runEnterAnimation(viewHolder as GameAdapter.CardViewHolder)
            return false
        }

        dispatchAddFinished(viewHolder)
        return false
    }

    private fun runEnterAnimation(holder: GameAdapter.CardViewHolder) {
        val screenHeight = ViewUtils.getScreenHeight(holder.itemView.context)
        holder.itemView.translationY = screenHeight.toFloat()
        holder.itemView.animate()
                .translationY(0f)
                .setInterpolator(DecelerateInterpolator(3f))
                .setDuration(700)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        dispatchAddFinished(holder)
                    }
                })
                .start()
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder,
                               newHolder: RecyclerView.ViewHolder,
                               preInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
                               postInfo: RecyclerView.ItemAnimator.ItemHolderInfo): Boolean {
        cancelCurrentAnimationIfExists(newHolder)

        if (preInfo is MatchItemHolderInfo) {
            val holder = newHolder as GameAdapter.CardViewHolder
            animateBackgroundColor(holder)
        }

        return false
    }

    private fun cancelCurrentAnimationIfExists(item: RecyclerView.ViewHolder) {
        if (animationOneMap.containsKey(item)) {
            animationOneMap[item]?.cancel()
        }
        if (animationTwoMap.containsKey(item)) {
            animationTwoMap[item]?.cancel()
        }
    }

    private fun animateBackgroundColor(holder: GameAdapter.CardViewHolder) {
    }

    private fun dispatchChangeFinishedIfAllAnimationsEnded(holder: GameAdapter.CardViewHolder) {
        if (animationOneMap.containsKey(holder) || animationTwoMap.containsKey(holder)) {
            return
        }

        dispatchAnimationFinished(holder)
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        cancelCurrentAnimationIfExists(item)
    }

    class MatchItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo()

    companion object
}