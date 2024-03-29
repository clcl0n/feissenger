package com.feissenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.db.model.RoomItem
import com.feissenger.ui.ViewPagerFragmentDirections
import kotlinx.android.synthetic.main.room_item.view.*


class RoomsAdapter : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

    var data = listOf<RoomItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var category: String = "A"

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        if (position == 0) {
            category = item.id.ssid.first().toUpperCase().toString()
            holder.bind(item, category, View.VISIBLE)
        } else {
            val prevItem = data[position - 1]
            if (item.id.ssid.first().toUpperCase() != prevItem.id.ssid.first().toUpperCase()) {
                category = item.id.ssid.first().toUpperCase().toString()
                holder.bind(item, category, View.VISIBLE)
            } else {
                holder.bind(item, category, View.GONE)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: RoomItem,
            category: String,
            visibility: Int
        ) {

            val head = (itemView.room_group_layout as FrameLayout)
            head.room_group.text = category
            head.visibility = visibility

            (itemView.ssid as TextView).text = item.id.ssid
            itemView.ssid.setOnClickListener {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(item.id.ssid)
                it.findNavController().navigate(action)
            }
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