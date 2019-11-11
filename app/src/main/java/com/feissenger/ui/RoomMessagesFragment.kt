package com.feissenger.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import com.feissenger.ui.adapter.MessagesAdapter
import com.feissenger.ui.viewModels.MessagesViewModel
import com.feissenger.R
import com.feissenger.databinding.FragmentMessageBinding
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.DarkTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.giphy.sdk.ui.views.buttons.GPHGiphyButtonStyle
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRoomMessageBinding
import com.feissenger.ui.adapter.RoomMessagesAdapter
import com.feissenger.ui.viewModels.RoomMessagesViewModel
import com.feissenger.ui.viewModels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_message.*

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

        viewModel.user = "1"
        viewModel.contact = "2"

        binding.model = viewModel

        viewModel.loadMessages()

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

//        binding.model.setUser("1")
//        binding.model.setContact(sharedViewModel.contactId.value!!)

//        viewModel.setUser("1")
//        viewModel.setContact(sharedViewModel.contactId.value!!)

        return binding.root
    }
}