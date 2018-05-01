package com.tronography.rxmemory.ui.game.listeners

import com.tronography.rxmemory.data.model.cards.Card


interface OnCardClickedListener {

    fun onCardClicked(card : Card)

}