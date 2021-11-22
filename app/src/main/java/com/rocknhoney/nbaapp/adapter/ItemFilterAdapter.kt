package com.rocknhoney.nbaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rocknhoney.nbaapp.R
import com.rocknhoney.nbaapp.databinding.GridItemBinding
import com.rocknhoney.nbaapp.model.TeamFilter


class ItemFilterAdapter(context: Context, resource: Int, objects: ArrayList<TeamFilter>) :
    ArrayAdapter<TeamFilter>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        }
        val binding = GridItemBinding.bind(itemView!!)
        val teamFilter: TeamFilter? = getItem(position)
        val teamImage = binding.gridItemIv
        teamFilter?.let {
            teamImage.setImageResource(it.iconId)
            if (teamFilter.isSelected) {
                binding.gridItemIv.setBackgroundResource(R.color.light_grey)
            }
        }
        return itemView
    }
}