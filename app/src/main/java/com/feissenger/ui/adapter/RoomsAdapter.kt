package com.feissenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.db.model.RoomItem
import com.feissenger.ui.viewModels.SharedViewModel
import kotlinx.android.synthetic.main.room_item.view.*


class RoomsAdapter(private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

    var data = listOf<RoomItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, sharedViewModel)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: RoomItem,
            sharedViewModel: SharedViewModel
        ) {
            (itemView.ssid as TextView).text = item.id.ssid

            itemView.setOnClickListener {
                sharedViewModel.setRoomId(item.id.ssid)
                it.findNavController().navigate(R.id.action_viewPagerFragment_to_roomMessagesFragment)
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