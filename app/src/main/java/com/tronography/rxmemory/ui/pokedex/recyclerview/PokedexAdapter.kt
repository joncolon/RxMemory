package com.tronography.rxmemory.ui.pokedex.recyclerview

import DEBUG
import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.calculateDiff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import com.tronography.rxmemory.databinding.ItemPokedexEntryBinding
import com.tronography.rxmemory.ui.base.BaseViewHolder
import com.tronography.rxmemory.ui.common.listeners.OnPokemonClickListener
import com.tronography.rxmemory.ui.pokedex.viewmodel.PokedexViewModel
import getResourceEntryName
import java.util.*
import javax.inject.Inject

class PokedexAdapter @Inject constructor(private val pokedexViewModel: PokedexViewModel)
    : RecyclerView.Adapter<BaseViewHolder>(), OnPokemonClickListener {

    val pokedexEntries: MutableList<Pokemon>

    init {
        this.pokedexEntries = ArrayList()
    }

    override fun onPokemonClicked(pokemon: Pokemon) {
        DEBUG("Click Successful")
        pokedexViewModel.onPokemonClicked(pokemon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemPokedexEntryBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return EntryViewHolder(binding)
    }

    fun updateList(newPokemon: List<Pokemon>) {
        val result: DiffUtil.DiffResult =
                calculateDiff(PokemonDiffCallback(newPokemon, pokedexEntries), true)

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


    inner class EntryViewHolder(private val binding: ItemPokedexEntryBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val pokemon = pokedexEntries[position]
            binding.viewModel = pokemon
            binding.executePendingBindings()
        }
    }
}