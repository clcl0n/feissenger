package com.feissenger.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.data.db.model.MessageItem
import com.feissenger.R
import kotlinx.android.synthetic.main.message_item.view.*
import android.widget.LinearLayout
import com.feissenger.data.db.model.RoomMessageItem
import kotlinx.android.synthetic.main.room_message_item.view.*

class RoomMessagesAdapter : RecyclerView.Adapter<RoomMessagesAdapter.ViewHolder>() {

    var data = listOf<RoomMessageItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: RoomMessageItem) {
            (itemView.room_message_user_name as TextView).text = item.id.uid
            (itemView.room_message_time as TextView).text = item.id.time
            (itemView.room_message_text as TextView).text = item.message
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.room_message_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}