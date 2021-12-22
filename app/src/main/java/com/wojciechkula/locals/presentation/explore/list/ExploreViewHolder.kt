package com.wojciechkula.locals.presentation.explore.list

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wojciechkula.locals.R

class ExploreViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(group: ExploreItem, onButtonClicked: (groupId: String) -> Unit) {

        var hobbies: String = ""
        for (hobby in group.hobbies) {
            hobbies = "$hobbies$hobby, "
        }
//        (view.findViewById(R.id.avatarImage) as ImageView).drawable =
        (view.findViewById(R.id.groupNameLabel) as TextView).text = group.name
        (view.findViewById(R.id.hobbiesListLabel) as TextView).text = hobbies
        (view.findViewById(R.id.kmLabel) as TextView).text =
            if (group.distance < 1000.0) "<1km" else "${(group.distance / 1000).toInt()}km"
        (view.findViewById(R.id.groupSizeLabel) as TextView).text = group.size.toString()
        (view.findViewById(R.id.joinButton) as Button).setOnClickListener { onButtonClicked(group.id) }
    }
}