package com.wojciechkula.locals.presentation.explore.list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.wojciechkula.locals.R


class ExploreViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(
        group: ExploreItem,
        onButtonClicked: (groupId: String) -> Unit,
        onCardClicked: (groupId: ExploreItem) -> Unit
    ) {
        val context = view.context
        var bitmap: Bitmap?

        var hobbies: String = ""
        for (hobby in group.hobbies) {
            hobbies = "$hobbies$hobby, "
        }
        if (!group.avatar.isNullOrEmpty()) {
            (view.findViewById(R.id.exploreGroupItemImageView) as ImageView).load(group.avatar)
            val loader = ImageLoader(context)
            val req = ImageRequest.Builder(context)
                .data(group.avatar)
                .target { result ->
                    bitmap = (result as BitmapDrawable).bitmap
                    group.avatarBitmap = bitmap
                }
                .build()
            loader.enqueue(req)
        }
        (view.findViewById(R.id.groupNameLabel) as TextView).text = group.name
        (view.findViewById(R.id.hobbiesListLabel) as TextView).text = hobbies
        (view.findViewById(R.id.kmLabel) as TextView).text =
            if (group.distance < 1000.0) "<1km" else "${(group.distance / 1000).toInt()}km"
        (view.findViewById(R.id.groupSizeLabel) as TextView).text = group.size.toString()
        (view.findViewById(R.id.joinButton) as Button).setOnClickListener { onButtonClicked(group.id) }
        (view.findViewById(R.id.mainCard) as CardView).setOnClickListener { onCardClicked(group) }
    }
}