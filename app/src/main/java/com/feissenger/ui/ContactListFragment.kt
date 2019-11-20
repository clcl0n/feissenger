package com.feissenger.ui

import android.app.LauncherActivity
import android.content.Context
import android.content.SharedPreferences
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
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.db.model.ContactItem
import com.feissenger.databinding.FragmentContactListBinding
import com.feissenger.ui.adapter.ContactListAdapter
import com.feissenger.ui.viewModels.ContactListViewModel
import com.feissenger.data.util.Injection
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.fragment_contact_list.*


class ContactListFragment : Fragment(){

    private lateinit var viewModel: ContactListViewModel
    private lateinit var binding: FragmentContactListBinding
    private lateinit var sharedPref: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = context?.let { MySharedPreferences(it) }!!
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_contact_list, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(ContactListViewModel::class.java)

//        sharedViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
//            .get(SharedViewModel::class.java)

//        sharedViewModel = activity?.run {
//            ViewModelProviders.of(this)[SharedViewModel::class.java]
//        } ?: throw Exception("Invalid Activity")
//
//        viewModel.uid = sharedViewModel.user.value!!.uid
//        viewModel.access = sharedViewModel.user.value!!.access

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
            viewModel.access = get("access").toString()
        }

        binding.model = viewModel

        binding.contactList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)



        val adapter = ContactListAdapter()
        binding.contactList.adapter = adapter
        viewModel.contactList.observe(this) {
            adapter.data = it
        }

        binding.fastscroller.apply {
            setupWithRecyclerView(
                binding.contactList,
                {
                    position ->
                    val item = adapter.data[position]
                    FastScrollItemIndicator.Text(
                        item.name.substring(0, 1).toUpperCase()
                    )
                }
            )
        }

        binding.fastscrollerThumb.setupWithFastScroller(binding.fastscroller)

        viewModel.loadContacts()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment","contacts")
    }
}
