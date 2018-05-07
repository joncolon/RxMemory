package com.tronography.rxmemory.ui.game.adapter

import DEBUG
import android.support.constraint.ConstraintLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.tronography.rxmemory.data.model.cards.Card
import com.tronography.rxmemory.databinding.CardBinding
import com.tronography.rxmemory.ui.base.BaseViewHolder
import com.tronography.rxmemory.ui.game.viewmodel.GameViewModel
import com.tronography.rxmemory.ui.game.listeners.OnCardClickedListener
import com.tronography.rxmemory.utilities.DiffCallback
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
        val binding = CardBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.adapterPosition
        holder.itemView.setOnClickListener({
            DEBUG("view clicked : ${it.getResourceEntryName()}")
            val adapterPosition = holder.adapterPosition
            when {adapterPosition != RecyclerView.NO_POSITION -> onCardClicked(cards[adapterPosition])
            }
        })
        holder.onBind(position)

    }

    fun updateList(newCards: List<Card>) {
        DEBUG("ADAPTER UPDATED...")
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(DiffCallback(newCards, cards), true)
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

    override fun getItemId(position: Int): Long {
        return cards[position].cardId
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