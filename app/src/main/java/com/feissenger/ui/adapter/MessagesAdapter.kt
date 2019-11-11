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

class   MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    var data = listOf<MessageItem>()
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
        fun bind(item: MessageItem) {
            (itemView.message_bubble as TextView).text = item.message
            (itemView.message_time as TextView).text = item.id.time

            itemView.message_bubble.setOnClickListener {
                val currentVisibility = itemView.message_time.visibility
                if(currentVisibility == View.GONE)
                    itemView.message_time.visibility = View.VISIBLE
                else
                    itemView.message_time.visibility = View.GONE
            }

            if(item.id.uid == "1"){
                itemView.message_bubble.setBackgroundResource(R.drawable.rounded_border_sender)
                (itemView.message_bubble.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.END
                (itemView.message_time.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.END
                itemView.view_left.visibility = View.VISIBLE
                itemView.view_right.visibility = View.GONE
            }
            else{
                itemView.message_bubble.setBackgroundResource(R.drawable.rounded_border_recipient)
                (itemView.message_bubble.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.START
                (itemView.message_time.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.START
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