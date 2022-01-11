package com.wojciechkula.locals.presentation.creategroup.list

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wojciechkula.locals.R

class HobbyViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(hobby: HobbyItem, onButtonClicked: (groupId: String) -> Unit) {
        (view.findViewById(R.id.hobbyName) as TextView).text = hobby.name
        (view.findViewById(R.id.item) as ConstraintLayout).setOnClickListener { onButtonClicked(hobby.name) }
    }
}