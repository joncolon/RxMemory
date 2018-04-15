package com.tronography.rxmemory.ui

import DEBUG
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tronography.rxmemory.data.model.Card
import com.tronography.rxmemory.databinding.CardBinding
import com.tronography.rxmemory.ui.base.BaseViewHolder
import com.tronography.rxmemory.ui.common.OnCardClickedListener
import java.util.*
import javax.inject.Inject

class GameAdapter @Inject constructor(val mainActivityViewModel: MainActivityViewModel) : RecyclerView.Adapter<BaseViewHolder>(), OnCardClickedListener {

    override fun onCardClicked(card : Card) {
        mainActivityViewModel
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
            val adapterPosition = holder.adapterPosition
            when {adapterPosition != RecyclerView.NO_POSITION -> onCardClicked(cards[position])
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

        override fun onBind(position: Int) {
            card = cards[position]
            DEBUG("onBind: ${card.cardId}")
            binding.viewModel = card
            binding.executePendingBindings()
        }
    }
}