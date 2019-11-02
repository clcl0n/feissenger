package com.example.viewmodel.ui.adapter

import android.opengl.Visibility
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewmodel.data.db.model.MessageItem
import com.feissenger.R
import kotlinx.android.synthetic.main.message_item.view.*
import android.widget.FrameLayout
import androidx.core.view.marginEnd


class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    var data = listOf<MessageItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(item: MessageItem, position: Int) {
            (itemView.message_bubble as TextView).text = item.text
            if (position % 2 == 0){
                itemView.message_bubble.setBackgroundResource(R.drawable.rounded_border_red)
                (itemView.message_bubble.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.END
                itemView.view_left.visibility = View.VISIBLE
                itemView.view_right.visibility = View.GONE
            }
            else{
                itemView.message_bubble.setBackgroundResource(R.drawable.rounded_border_blue)
                (itemView.message_bubble.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.START
                itemView.view_left.visibility = View.GONE
                itemView.view_right.visibility = View.VISIBLE
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.message_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}