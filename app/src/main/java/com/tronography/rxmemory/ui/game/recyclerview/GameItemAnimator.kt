package com.tronography.rxmemory.ui.game.recyclerview

import DEBUG
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.tronography.rxmemory.R
import com.tronography.rxmemory.utilities.ViewUtils
import java.util.*


class GameItemAnimator : DefaultItemAnimator() {

    private var cardFlipAnimationMap: MutableMap<RecyclerView.ViewHolder, AnimatorSet> = HashMap()
    private var spinEntryAnimationMap: MutableMap<RecyclerView.ViewHolder, ObjectAnimator> = HashMap()


    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun recordPreLayoutInformation(
            state: RecyclerView.State,
            viewHolder: RecyclerView.ViewHolder,
            changeFlags: Int, payloads: List<Any>): RecyclerView.ItemAnimator.ItemHolderInfo {

        if (changeFlags == RecyclerView.ItemAnimator.FLAG_CHANGED) {
            return CardItemHolderInfo()
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }


    private fun animateFlipRevealCardFront(holder: GameAdapter.CardViewHolder) {
        val animatorSet = AnimatorSet()

        val startSpin = ObjectAnimator.ofFloat(holder.cardLayout, ROTATION_Y, 0.0f, 90f)
        startSpin.duration = 300
        startSpin.interpolator = ACCELERATE_INTERPOLATOR

        val rotateImageLeft = ObjectAnimator.ofFloat(holder.card_front_image, ROTATION_Y, 90f, 180f)
        rotateImageLeft.duration = IMMEDIATE

        val finishSpin = ObjectAnimator.ofFloat(holder.cardLayout, ROTATION_Y, 90f, 180f)
        finishSpin.duration = 300
        finishSpin.interpolator = DECCELERATE_INTERPOLATOR

        startSpin.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                when (holder.card.isFlipped) {
                    true -> {
                        holder.card_front_image.visibility = View.VISIBLE
                        holder.card_back_image.visibility = View.GONE
                        holder.background.background = holder.background.context.getDrawable(R.drawable.grass_bg)
                    }
                    else -> {
                        holder.background.background = holder.background.context.getDrawable(R.drawable.card_back_background)
                        holder.card_back_image.background = holder.card_back_image.context.getDrawable(R.drawable.ic_pokeball_card_back)
                        holder.card_front_image.visibility = View.GONE
                        holder.card_back_image.visibility = View.VISIBLE

                    }
                }
            }
        })

        finishSpin.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                cardFlipAnimationMap.remove(holder)
                dispatchChangeFinishedIfAllAnimationsEnded(holder)
            }
        })

        animatorSet.play(finishSpin).with(rotateImageLeft).after(startSpin)
        animatorSet.start()
        cardFlipAnimationMap[holder] = animatorSet
    }

    private fun animateFlipRevealCardBack(holder: GameAdapter.CardViewHolder) {
        val animatorSet = AnimatorSet()

        val startSpin = ObjectAnimator.ofFloat(holder.cardLayout, ROTATION_Y, 180f, 90f)
        startSpin.duration = SPIN_DURATION
        startSpin.interpolator = ACCELERATE_INTERPOLATOR

        val finishSpin = ObjectAnimator.ofFloat(holder.cardLayout, ROTATION_Y, 90f, 0.0f)
        finishSpin.duration = SPIN_DURATION
        finishSpin.interpolator = DECCELERATE_INTERPOLATOR

        startSpin.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                when (holder.card.isFlipped) {
                    true -> {
                        holder.card_front_image.visibility = View.VISIBLE
                        holder.card_back_image.visibility = View.GONE
                        holder.background.background = holder.background.context.getDrawable(R.drawable.grass_bg)

                    }
                    else -> {
                        holder.background.background = holder.background.context.getDrawable(R.drawable.card_back_background)
                        holder.card_back_image.background = holder.card_back_image.context.getDrawable(R.drawable.ic_pokeball_card_back)
                        holder.card_front_image.visibility = View.GONE
                        holder.card_back_image.visibility = View.VISIBLE
                    }
                }
            }
        })

        finishSpin.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                cardFlipAnimationMap.remove(holder)
                dispatchChangeFinishedIfAllAnimationsEnded(holder)
            }
        })

        animatorSet.play(finishSpin).after(startSpin)

        animatorSet.start()
        cardFlipAnimationMap[holder] = animatorSet
    }

    private fun animateSpinEntry(holder: GameAdapter.CardViewHolder) {
        val animatorSet = AnimatorSet()

        val rotationAnim = ObjectAnimator.ofFloat(holder.cardFront, "rotation", 0f, 360f)
        rotationAnim.duration = ROTATION_DURATION
        rotationAnim.interpolator = ACCELERATE_INTERPOLATOR

        val bounceAnimX = ObjectAnimator.ofFloat(holder.cardFront, "scaleX", 0.2f, 1f)
        bounceAnimX.duration = BOUNCE_DURATION
        bounceAnimX.interpolator = OVERSHOOT_INTERPOLATOR

        val bounceAnimY = ObjectAnimator.ofFloat(holder.cardFront, "scaleY", 0.2f, 1f)
        bounceAnimY.duration = BOUNCE_DURATION
        bounceAnimY.interpolator = OVERSHOOT_INTERPOLATOR
        bounceAnimY.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                spinEntryAnimationMap.remove(holder)
                dispatchChangeFinishedIfAllAnimationsEnded(holder)
            }
        })

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim)
        animatorSet.start()

        cardFlipAnimationMap[holder] = animatorSet
    }

    override fun animateAdd(viewHolder: RecyclerView.ViewHolder): Boolean {
        runEnterAnimation(viewHolder as GameAdapter.CardViewHolder, viewHolder.layoutPosition)
        return true
    }


    private fun runEnterAnimation(holder: GameAdapter.CardViewHolder, layoutPosition: Int) {
        val screenHeight = ViewUtils.getScreenHeight(holder.itemView.context)
        holder.itemView.translationY = screenHeight.toFloat()

        val START_DELAY = (layoutPosition * 20).toLong()
        DEBUG(START_DELAY.toString())
        holder.itemView.animate()
                .translationY(0f)
                .setInterpolator(DecelerateInterpolator(3f))
                .setDuration(ENTER_DURATION)
                .setStartDelay(START_DELAY)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        dispatchAddFinished(holder)
                        DEBUG("Anim finished on position - ${holder.adapterPosition} ")
                    }
                })
                .start()
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder,
                               newHolder: RecyclerView.ViewHolder,
                               preInfo: RecyclerView.ItemAnimator.ItemHolderInfo,
                               postInfo: RecyclerView.ItemAnimator.ItemHolderInfo): Boolean {
        cancelCurrentAnimationIfExists(newHolder)

        if (preInfo is CardItemHolderInfo) {
            val holder = newHolder as GameAdapter.CardViewHolder
            when (holder.card.isFlipped) {
                true -> animateFlipRevealCardFront(holder)
                false -> animateFlipRevealCardBack(holder)
            }

            when (holder.card.isMatched) {
                true -> animateSpinEntry(holder)
            }
        }

        return false
    }

    private fun cancelCurrentAnimationIfExists(item: RecyclerView.ViewHolder) {
        if (cardFlipAnimationMap.containsKey(item)) {
            cardFlipAnimationMap[item]?.cancel()
        }
        if (spinEntryAnimationMap.containsKey(item)) {
            spinEntryAnimationMap[item]?.cancel()
        }
    }

    private fun dispatchChangeFinishedIfAllAnimationsEnded(holder: GameAdapter.CardViewHolder) {
        if (cardFlipAnimationMap.containsKey(holder) || spinEntryAnimationMap.containsKey(holder)) {
            return
        }

        dispatchAnimationFinished(holder)
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        cancelCurrentAnimationIfExists(item)
    }

    companion object {
        private val DECCELERATE_INTERPOLATOR = DecelerateInterpolator()
        private val ACCELERATE_INTERPOLATOR = AccelerateInterpolator()
        private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)

        private val ROTATION_Y = "rotationY"

        private val IMMEDIATE: Long = 0
        private val SPIN_DURATION: Long = 300
        private val ROTATION_DURATION: Long = 300
        private val BOUNCE_DURATION: Long = 400
        val ENTER_DURATION: Long = 700
    }

    class CardItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo()

}