package com.rocknhoney.nbaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rocknhoney.nbaapp.R
import com.rocknhoney.nbaapp.databinding.ItemPlayerBinding
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.ui.MainFragmentDirections
import java.util.*

class PlayersListAdapter :
    RecyclerView.Adapter<PlayersListAdapter.PlayersViewHolder>(), ItemClickListener {

    class PlayersViewHolder(var view: ItemPlayerBinding) : RecyclerView.ViewHolder(view.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)


    var players: List<Player>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val view = DataBindingUtil.inflate<ItemPlayerBinding>(
            inflater,
            R.layout.item_player, parent, false
        )
        view.listener = this
        return PlayersViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.view.player = players[position]
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onItemClicked(player: Any, view: View) {
        Navigation.findNavController(view)
            .navigate(MainFragmentDirections.actionMainFragmentToPlayerDetailFragment(player as Player))
    }

    fun filterByName(playerList: List<Player>, text: String) {
        val playersCopy: ArrayList<Player> = arrayListOf()
        if (text.isEmpty()) {
            playersCopy.addAll(playerList)
        } else {
            for (player in playerList) {
                if (player.firstName?.toLowerCase(Locale.ROOT)
                        ?.contains(text.toLowerCase(Locale.ROOT)) == true ||
                    player.lastName?.toLowerCase(Locale.ROOT)
                        ?.contains(text.toLowerCase(Locale.ROOT)) == true
                ) {
                    playersCopy.add(player)
                }
            }
        }
        this.players = playersCopy
        notifyDataSetChanged()
    }

    fun filterByTeam(playerList: List<Player>, text: String) {
        val playersCopy: ArrayList<Player> = arrayListOf()
        if (text.isEmpty()) {
            playersCopy.addAll(playerList)
        } else {
            for (player in playerList) {
                if (player.team?.name?.toLowerCase(Locale.ROOT)
                        ?.equals(text.toLowerCase(Locale.ROOT)) == true
                ) {
                    playersCopy.add(player)
                }
            }
        }
        this.players = playersCopy
        notifyDataSetChanged()
    }
}