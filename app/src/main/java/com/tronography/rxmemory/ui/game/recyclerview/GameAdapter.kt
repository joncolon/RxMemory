package com.tronography.rxmemory.ui.game.recyclerview

import DEBUG
import android.support.constraint.ConstraintLayout
import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.calculateDiff
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.tronography.rxmemory.data.model.cards.Card
import com.tronography.rxmemory.databinding.ItemCardBinding
import com.tronography.rxmemory.ui.base.BaseViewHolder
import com.tronography.rxmemory.ui.common.listeners.OnCardClickedListener
import com.tronography.rxmemory.ui.game.viewmodel.GameViewModel
import getResourceEntryName
import java.util.*
import javax.inject.Inject

class GameAdapter @Inject constructor(private val gameViewModel: GameViewModel) : RecyclerView.Adapter<BaseViewHolder>(), OnCardClickedListener {

    var isClickable: Boolean = true

    fun enableCardClick() {
        isClickable = true
        DEBUG("ADAPTER CLICKABLE : $isClickable")
    }

    fun disableCardClicks() {
        isClickable = false
        DEBUG("ADAPTER CLICKABLE : $isClickable")
    }

    override fun onCardClicked(card: Card) {
        if (isClickable) {
            DEBUG("Click Successful")
            gameViewModel.onCardClicked(card)
        }
    }

    val cards: MutableList<Card>

    init {
        this.cards = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val binding = ItemCardBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    fun updateList(newCards: List<Card>) {
        val result: DiffUtil.DiffResult =
                calculateDiff(CardDiffCallback(newCards, cards), true)

        cards.clear()
        cards.addAll(newCards)
        result.dispatchUpdatesTo(this)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.adapterPosition
        holder.itemView.setOnClickListener({
            DEBUG("view clicked : ${it.getResourceEntryName()}")
            val adapterPosition = holder.adapterPosition
            when {adapterPosition != RecyclerView.NO_POSITION -> onCardClicked(cards[adapterPosition])
            }
        })
        holder.onBind(position)
    }

    override fun getItemCount(): Int = cards.size


    inner class CardViewHolder(private val binding: ItemCardBinding) : BaseViewHolder(binding.root) {

        lateinit var card: Card

        val cardFront: CardView = binding.cardFront
        val card_front_image: ImageView = binding.cardFrontImage
        val card_back_image: ImageView = binding.cardBackImage
        val background: ConstraintLayout = binding.cardFrontContainer
        val cardLayout: ConstraintLayout = binding.cardLayout

        override fun onBind(position: Int) {
            card = cards[position]
            binding.viewModel = card
            binding.executePendingBindings()
        }
    }
}