package com.feissenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.db.model.ContactItem
import com.feissenger.ui.ViewPagerFragmentDirections
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contact_list.view.*


class ContactListAdapter : RecyclerView.Adapter<ContactListAdapter.ViewHolder>(){

    var data = listOf<ContactItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var category: String = "A"


    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        if(position == 0){
            category = item.name.first().toUpperCase().toString()
            holder.bind(item, category, View.VISIBLE)
        }
        else{
            val prevItem = data[position-1]
            if(item.name.first().toUpperCase() != prevItem.name.first().toUpperCase()){
                category = item.name.first().toUpperCase().toString()
                holder.bind(item, category, View.VISIBLE)
            }else{
                holder.bind(item, category, View.GONE)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            item: ContactItem,
            category: String,
            visibility: Int
        ) {

            val head = (itemView.contact_group as TextView)
            head.text = category
            head.visibility = visibility

            (itemView.contact_name as TextView).text = item.name
        itemView.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToMessagesFragment(item.id.contactId, item.name)
            it.findNavController().navigate(action)
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