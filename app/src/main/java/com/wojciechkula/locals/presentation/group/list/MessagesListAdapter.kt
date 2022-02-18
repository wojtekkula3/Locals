package com.wojciechkula.locals.presentation.group.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wojciechkula.locals.R

class MessagesListAdapter(
    private val currentUserId: String,
    private val onAvatarClick: (userId: String) -> Unit
) : ListAdapter<MessageItem, MessagesViewHolder>(MessagesDiffCallback()) {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view: View = if (viewType == VIEW_TYPE_SENT) {
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_sent_message, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_received_message, parent, false)
        }
        return MessagesViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.currentList[position].authorId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onBindViewHolder(viewHolder: MessagesViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            viewHolder.bindSentMessage(getItem(position))
        } else {
            viewHolder.bindReceivedMessage(getItem(position), onAvatarClick)
        }
    }

}

class MessagesDiffCallback : DiffUtil.ItemCallback<MessageItem>() {
    override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem) =
        oldItem == newItem
}