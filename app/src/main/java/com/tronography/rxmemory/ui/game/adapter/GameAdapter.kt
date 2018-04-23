package com.tronography.rxmemory.ui.game.adapter

import DEBUG
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.databinding.CardBinding
import com.tronography.rxmemory.ui.base.BaseViewHolder
import com.tronography.rxmemory.ui.game.GameViewModel
import com.tronography.rxmemory.ui.game.listeners.OnCardClickedListener
import java.util.*
import javax.inject.Inject

class GameAdapter @Inject constructor(private val gameViewModel: GameViewModel) : RecyclerView.Adapter<BaseViewHolder>(), OnCardClickedListener {

    var isClickable: Boolean = true

    fun enableCardClick() {
        isClickable = true
    }

    fun disableCardClicks() {
        isClickable = false
    }

    override fun onCardClicked(card: Card) {
        DEBUG("ADAPTER CLICKABLE : $isClickable")
        if (isClickable) {
            gameViewModel.selectCard(card)
        }
    }

    val cards: MutableList<Card>

    init {
        this.cards = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.adapterPosition
        holder.itemView.setOnClickListener({
            DEBUG("VIEW $it CLICKED")
            val adapterPosition = holder.adapterPosition
            when {adapterPosition != RecyclerView.NO_POSITION -> onCardClicked(cards[adapterPosition])
            }
        })
        holder.onBind(position)
    }

    override fun getItemCount(): Int = cards.size

    fun addItems(newMatches: List<Card>) {
        cards.addAll(newMatches)
    }

    fun clearItems() {
        cards.clear()
    }

    inner class CardViewHolder(private val binding: CardBinding) :
            BaseViewHolder(binding.root) {

        lateinit var card: Card
        var cardFront: CardView = binding.cardFront
        var card_front_image: ImageView = binding.cardFrontImage
        var card_back_image: ImageView = binding.cardBackImage
        var background: ConstraintLayout = binding.cardFrontContainer
        var cardLayout: ConstraintLayout = binding.cardLayout

        override fun onBind(position: Int) {
            card = cards[position]
            binding.viewModel = card
            binding.executePendingBindings()
        }
    }
}