package com.tronography.rxmemory.game

import com.tronography.rxmemory.data.model.Card


class Deck {

    fun createDeck(cards: List<Card>) : List<Card> {
        val deck = mutableListOf<Card>()
        cards.forEach{ card -> deck.add(card) ; deck.add(card)}
        deck.shuffle()
        return deck
    }
}