package com.feissenger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.databinding.FragmentContactListBinding
import com.feissenger.ui.adapter.ContactListAdapter
import com.feissenger.ui.viewModels.ContactListViewModel
import com.feissenger.data.util.Injection
import com.feissenger.ui.viewModels.SharedViewModel


class ContactListFragment : Fragment(){

    private lateinit var viewModel: ContactListViewModel
    private lateinit var sharedViewModel: SharedViewModel
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

//        sharedViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
//            .get(SharedViewModel::class.java)

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.uid = sharedViewModel.user.value!!.uid
        viewModel.access = sharedViewModel.user.value!!.access

        binding.model = viewModel

        binding.contactList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = ContactListAdapter(sharedViewModel)
        binding.contactList.adapter = adapter
        viewModel.contactList.observe(this) {
            adapter.data = it
        }

        viewModel.loadContacts()

        return binding.root
    }
}
