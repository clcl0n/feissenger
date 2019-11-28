package com.feissenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.db.model.ContactItem
import com.feissenger.ui.viewModels.SharedViewModel
import kotlinx.android.synthetic.main.contact_item.view.*


class ContactListAdapter(sharedViewModel: SharedViewModel) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    val sharedViewModel = sharedViewModel

    var data = listOf<ContactItem>()
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
            item: ContactItem,
            sharedViewModel: SharedViewModel
        ) {
            (itemView.contact_name as TextView).text = item.name
            (itemView.contact_id as TextView).text = item.id

            itemView.setOnClickListener {
                sharedViewModel.setContactId(item.id)
                it.findNavController().navigate(R.id.action_contact_list_fragment_to_messagesFragment)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.contact_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}