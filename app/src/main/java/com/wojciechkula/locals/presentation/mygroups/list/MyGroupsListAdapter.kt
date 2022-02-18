package com.wojciechkula.locals.presentation.mygroups.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wojciechkula.locals.R

class MyGroupsListAdapter(
    private val onButtonClicked: (selectedGroup: MyGroupsItem) -> Unit
) : ListAdapter<MyGroupsItem, MyGroupsViewHolder>(MyGroupsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_my_groups, parent, false)
        return MyGroupsViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyGroupsViewHolder, position: Int) {
        viewHolder.bind(getItem(position), onButtonClicked)
    }

}

class MyGroupsDiffCallback : DiffUtil.ItemCallback<MyGroupsItem>() {
    override fun areItemsTheSame(oldItem: MyGroupsItem, newItem: MyGroupsItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MyGroupsItem, newItem: MyGroupsItem) =
        oldItem == newItem
}