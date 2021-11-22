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
import com.rocknhoney.nbaapp.databinding.ItemTeamBinding
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.ui.MainFragment
import com.rocknhoney.nbaapp.ui.MainFragmentDirections
import java.util.*

class TeamsListAdapter :
    RecyclerView.Adapter<TeamsListAdapter.TeamsViewHolder>(), ItemClickListener {

    class TeamsViewHolder(var view: ItemTeamBinding) : RecyclerView.ViewHolder(view.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)


    var teams: List<Team>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val view =
            DataBindingUtil.inflate<ItemTeamBinding>(inflater, R.layout.item_team, parent, false)
        view.listener = this
        return TeamsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.view.team = teams[position]
        val teamArray = MainFragment.teamFilterArrayList
        for (team in teamArray) {
            if (teams[position].name?.toLowerCase(Locale.ROOT).equals(team.name.toLowerCase(Locale.ROOT))) {
                holder.view.teamIconImageView.setImageResource(team.iconId)
            }
        }
    }

    override fun getItemCount(): Int {
        return teams.size
    }

    override fun onItemClicked(team: Any, view: View) {
        Navigation.findNavController(view)
            .navigate(MainFragmentDirections.actionMainFragmentToTeamDetailFragment(team as Team))
    }

    fun filterByName(teamsList: List<Team>, text: String) {
        val teamsCopy: ArrayList<Team> = arrayListOf()
        if (text.isEmpty()) {
            teamsCopy.addAll(teamsList)
        } else {
            for (team in teamsList) {
                if (team.fullName?.toLowerCase(Locale.ROOT)
                        ?.contains(text.toLowerCase(Locale.ROOT)) == true
                ) {
                    teamsCopy.add(team)
                }
            }
        }
        this.teams = teamsCopy
        notifyDataSetChanged()
    }

    fun filterByConference(teamsList: List<Team>, text: String) {
        val teamsCopy: ArrayList<Team> = arrayListOf()
        if (text.isEmpty()) {
            teamsCopy.addAll(teamsList)
        } else {
            for (team in teamsList) {
                if (team.conference?.toLowerCase(Locale.ROOT)
                        ?.contains(text.toLowerCase(Locale.ROOT)) == true
                ) {
                    teamsCopy.add(team)
                }
            }
        }
        this.teams = teamsCopy
        notifyDataSetChanged()
    }

}