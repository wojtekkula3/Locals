package com.wojciechkula.locals.presentation.explore.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wojciechkula.locals.R

class ExploreListAdapter(
    private val onButtonClicked: (groupId: String) -> Unit
) : ListAdapter<ExploreItem, ExploreViewHolder>(ExploreDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_item_explore, viewGroup, false)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ExploreViewHolder, position: Int) {
        viewHolder.bind(getItem(position), onButtonClicked)
    }
}

class ExploreDiffCallback : DiffUtil.ItemCallback<ExploreItem>() {
    override fun areItemsTheSame(oldItem: ExploreItem, newItem: ExploreItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ExploreItem, newItem: ExploreItem) =
        oldItem == newItem
}
