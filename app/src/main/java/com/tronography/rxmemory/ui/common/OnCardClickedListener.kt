package com.tronography.rxmemory.ui.common

import com.tronography.rxmemory.data.model.Card


interface OnCardClickedListener {

    fun onCardClicked(card : Card)
}