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
import com.example.viewmodel.data.db.model.RoomItem


class RoomsAdapter : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

    var data = listOf<RoomItem>()
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


        fun bind(item: RoomItem) {
            (item.bssid as TextView).text = item.bssid
            (item.ssid as TextView).text = item.ssid
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.room_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}