package com.feissenger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.databinding.FragmentContactListBinding
import com.feissenger.ui.adapter.ContactListAdapter
import com.feissenger.ui.viewModels.ContactListViewModel
import com.feissenger.data.util.Injection


class ContactListFragment : Fragment(){

    private lateinit var viewModel: ContactListViewModel
    private lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_contact_list, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(ContactListViewModel::class.java)

        binding.model = viewModel

        binding.contactList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = ContactListAdapter()
        binding.contactList.adapter = adapter
        viewModel.contactList.observe(this) {
            adapter.data = it
        }

        viewModel.loadContacts()

        return binding.root
    }
}
