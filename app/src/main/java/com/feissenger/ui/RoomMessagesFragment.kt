package com.feissenger.ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRoomMessageBinding
import com.feissenger.ui.adapter.RoomMessagesAdapter
import com.feissenger.ui.viewModels.RoomMessagesViewModel
import com.google.android.material.snackbar.Snackbar

class RoomMessagesFragment : Fragment() {
    private lateinit var viewModel: RoomMessagesViewModel
    private lateinit var binding: FragmentRoomMessageBinding
    private lateinit var sharedPref: MySharedPreferences

    val arg: RoomMessagesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = context?.let { MySharedPreferences(it) }!!
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room_message, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(RoomMessagesViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
            viewModel.access = get("access").toString()
        }

        viewModel.roomid = arg.roomId

        binding.model = viewModel

        viewModel.loadRoomMessages()

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, true)

        val adapter = RoomMessagesAdapter()
        binding.messagesList.adapter = adapter
        viewModel.messages.observe(this) {
            adapter.data = it
            binding.messagesList.scrollToPosition(0)
        }

        val contentView = binding.messagesList
        contentView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            binding.messagesList.scrollToPosition(0)
        }


        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            val action = RoomMessagesFragmentDirections.actionRoomMessagesFragmentToRoomPost()
            view.findNavController().navigate(action)
        }

//        binding.model.setUid("1")
//        binding.model.setContact(sharedViewModel.contactId.value!!)

//        viewModel.setUid("1")
//        viewModel.setContact(sharedViewModel.contactId.value!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment","roomMessages")
        sharedPref.put("roomId",arg.roomId)
    }
}