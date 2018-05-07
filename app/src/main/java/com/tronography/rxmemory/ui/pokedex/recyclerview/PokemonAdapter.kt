package com.tronography.rxmemory.ui.pokedex.recyclerview

import DEBUG
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import com.tronography.rxmemory.databinding.ItemPokedexEntryBinding
import com.tronography.rxmemory.ui.base.BaseViewHolder
import com.tronography.rxmemory.ui.pokedex.listeners.OnPokemonClickListener
import com.tronography.rxmemory.ui.pokedex.viewmodel.PokedexViewModel
import getResourceEntryName
import java.util.*
import javax.inject.Inject

class PokemonAdapter @Inject constructor(private val pokedexViewModel: PokedexViewModel)
    : RecyclerView.Adapter<BaseViewHolder>(), OnPokemonClickListener {

    val pokedexEntries: MutableList<Pokemon>

    init {
        this.pokedexEntries = ArrayList()
    }

    var isClickable: Boolean = true

    fun enableCardClick() {
        isClickable = true
        DEBUG("ADAPTER CLICKABLE : $isClickable")
    }

    fun disableCardClicks() {
        isClickable = false
        DEBUG("ADAPTER CLICKABLE : $isClickable")
    }

    override fun onPokemonClicked(pokemon: Pokemon) {
        if (isClickable) {
            DEBUG("Click Successful")
            pokedexViewModel.onPokemonClicked(pokemon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemPokedexEntryBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return PokedexViewHolder(binding)
    }

    fun updateList(newPokemon: List<Pokemon>) {
        val result: DiffUtil.DiffResult =
                DiffUtil.calculateDiff(PokemonDiffCallback(newPokemon, pokedexEntries), true)

        pokedexEntries.clear()
        pokedexEntries.addAll(newPokemon)
        result.dispatchUpdatesTo(this)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.adapterPosition
        holder.itemView.setOnClickListener({
            DEBUG("view clicked : ${it.getResourceEntryName()}")
            val adapterPosition = holder.adapterPosition
            when {adapterPosition != RecyclerView.NO_POSITION -> onPokemonClicked(pokedexEntries[adapterPosition])
            }
        })
        holder.onBind(position)
    }

    override fun getItemCount(): Int = pokedexEntries.size


    inner class PokedexViewHolder(private val binding: ItemPokedexEntryBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val pokemon = pokedexEntries[position]
            binding.viewModel = pokemon
            binding.executePendingBindings()
        }
    }
}