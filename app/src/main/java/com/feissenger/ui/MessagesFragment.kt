package com.example.viewmodel.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewmodel.ui.adapter.MessagesAdapter
import com.example.viewmodel.ui.viewModels.MessagesViewModel
import com.feissenger.R
import com.feissenger.databinding.FragmentMessageBinding
import com.opinyour.android.app.data.utils.Injection
import android.R.attr.bottom
import android.view.ViewTreeObserver
import android.graphics.Rect


class MessagesFragment : Fragment() {
    private lateinit var messagesViewModel: MessagesViewModel
    private lateinit var binding: FragmentMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_message, container, false
        )
        binding.lifecycleOwner = this
        messagesViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(MessagesViewModel::class.java)

        binding.model = messagesViewModel

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = MessagesAdapter()
        binding.messagesList.adapter = adapter
        messagesViewModel.messages.observe(this) {
            adapter.data = it
            binding.messagesList.scrollToPosition(adapter.itemCount - 1)
        }

        val contentView = binding.messagesList
        contentView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            binding.messagesList.scrollToPosition(adapter.itemCount - 1)
        }

        return binding.root
    }
}