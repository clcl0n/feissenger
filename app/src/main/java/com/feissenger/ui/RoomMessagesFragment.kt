package com.feissenger.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.db.model.RoomItem
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRoomMessageBinding
import com.feissenger.ui.adapter.RoomMessagesAdapter
import com.feissenger.ui.viewModels.RoomMessagesViewModel
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.themes.DarkTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.giphy.sdk.ui.views.buttons.GPHGiphyButtonStyle
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_room_message.*

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
        viewModel = ViewModelProvider(activity!!, Injection.provideViewModelFactory(activity?.applicationContext!!))
            .get(RoomMessagesViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
        }

        viewModel.roomid = arg.roomId

        binding.roomMessage = viewModel


        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, true)

        val adapter = RoomMessagesAdapter()
        binding.messagesList.adapter = adapter
        viewModel.showFab.observeForever{
            if(fab != null){
                if(it)
                    fab.show()
                else
                    fab.hide()
            }
        }
        viewModel.messages.observeForever {
            adapter.data = it
            binding.messagesList.scrollToPosition(adapter.itemCount -1)
        }

        val contentView = binding.messagesList
        contentView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            binding.messagesList.scrollToPosition(adapter.itemCount -1)
        }


        binding.fab.setOnClickListener { view ->
            val action = RoomMessagesFragmentDirections.actionRoomMessagesFragmentToRoomPost(arg.roomId)
            view.findNavController().navigate(action)
        }
        if(sharedPref.get("activeWifi").toString() == viewModel.roomid || viewModel.roomid == "XsTDHS3C2YneVmEW5Ry7"){
            viewModel.showFab.postValue(true)

        }
        else{
            viewModel.showFab.postValue(false)
        }
        viewModel.loadRoomMessages()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPref.put("fragment","roomMessages")
        sharedPref.put("roomId",arg.roomId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val roomName = if (arg.roomId == "XsTDHS3C2YneVmEW5Ry7")
            "Public Room"
        else
            arg.roomId
        (activity as MainActivity).myToolbar.toolbar_text.text = roomName
        (activity as MainActivity).myToolbar.theme_icon.visibility = View.VISIBLE
    }
}