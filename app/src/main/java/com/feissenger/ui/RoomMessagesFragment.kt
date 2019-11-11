package com.feissenger.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRoomMessageBinding
import com.feissenger.ui.adapter.RoomMessagesAdapter
import com.feissenger.ui.viewModels.RoomMessagesViewModel
import com.feissenger.ui.viewModels.SharedViewModel

class RoomMessagesFragment : Fragment() {
    private lateinit var viewModel: RoomMessagesViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentRoomMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room_message, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(RoomMessagesViewModel::class.java)

//        sharedViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
//            .get(SharedViewModel::class.java)

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.uid = sharedViewModel.user.value!!.uid
        viewModel.access = sharedViewModel.user.value!!.access
        viewModel.roomid = sharedViewModel.roomid.value!!

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

//        binding.model.setUid("1")
//        binding.model.setContact(sharedViewModel.contactId.value!!)

//        viewModel.setUid("1")
//        viewModel.setContact(sharedViewModel.contactId.value!!)

        return binding.root
    }
}