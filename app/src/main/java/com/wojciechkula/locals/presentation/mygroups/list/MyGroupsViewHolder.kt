package com.wojciechkula.locals.presentation.mygroups.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.wojciechkula.locals.R
import java.util.*

class MyGroupsViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(group: MyGroupsItem, onButtonClicked: (selectedGroup: MyGroupsItem) -> Unit) {
//        (view.findViewById(R.id.avatarImage) as ImageView).drawable =
        (view.findViewById(R.id.groupNameOutput) as TextView).text = group.name
        (view.findViewById(R.id.authorAndMessageOutput) as TextView).text = group.author + ": " + group.message
        (view.findViewById(R.id.messageDateOutput) as TextView).text = setTime(group.sentAt)
        (view.findViewById(R.id.groupCardView) as MaterialCardView).setOnClickListener { onButtonClicked(group) }
    }

    private fun setTime(messageTime: Date): String {

        val calendar = Calendar.getInstance()
        calendar.time = messageTime

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)

        val oneDayEarlier = Date().time - (24 * 60 * 60 * 1000)
        return if (oneDayEarlier < messageTime.time) {
            String.format("%02d:%02d", hour, minute)
        } else {
            String.format("%02d.%02d", day, month + 1)
        }
    }
}