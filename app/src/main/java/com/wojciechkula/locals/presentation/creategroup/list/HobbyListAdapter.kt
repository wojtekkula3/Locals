package com.wojciechkula.locals.presentation.creategroup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wojciechkula.locals.R

class HobbyListAdapter(
    private val onButtonClicked: (groupId: String) -> Unit
) : ListAdapter<HobbyItem, HobbyViewHolder>(HobbyDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HobbyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_item_create_group, viewGroup, false)
        return HobbyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: HobbyViewHolder, position: Int) {
        viewHolder.bind(getItem(position), onButtonClicked)
    }
}

class HobbyDiffCallback : DiffUtil.ItemCallback<HobbyItem>() {
    override fun areItemsTheSame(oldItem: HobbyItem, newItem: HobbyItem) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: HobbyItem, newItem: HobbyItem) =
        oldItem == newItem
}
