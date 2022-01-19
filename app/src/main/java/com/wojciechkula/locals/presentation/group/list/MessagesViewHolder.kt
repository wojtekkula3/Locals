package com.wojciechkula.locals.presentation.group.list

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wojciechkula.locals.R
import java.text.SimpleDateFormat
import java.util.*

class MessagesViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bindSentMessage(message: MessageItem) {
        (view.findViewById(R.id.messageOutput) as TextView).text = message.message
        (view.findViewById(R.id.dateTimeOutput) as TextView).text = setTime(message.sentAt)
    }

    fun bindReceivedMessage(message: MessageItem, onAvatarClick: (authorId: String) -> Unit) {
        (view.findViewById(R.id.messageOutput) as TextView).text = message.message
        (view.findViewById(R.id.dateTimeOutput) as TextView).text = setTime(message.sentAt)
        if (!message.authorAvatar.isNullOrEmpty()) {
            (view.findViewById(R.id.avatarReceivedMessageOutput) as ImageView).load(message.authorAvatar)
        }
        (view.findViewById(R.id.avatarReceivedMessageOutput) as ImageView).setOnClickListener { onAvatarClick(message.authorId) }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setTime(messageTime: Date): String {

        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat("HH:mm")

        val today = Calendar.getInstance().time
        val todayDateFormatted = dateFormat.format(today)

        val messageDateFormatted = dateFormat.format(messageTime.time)
        val messageTimeFormatted = timeFormat.format(messageTime.time)

        return if (todayDateFormatted == messageDateFormatted) {
            messageTimeFormatted
        } else {
            "$messageTimeFormatted $messageDateFormatted"
        }
    }
}