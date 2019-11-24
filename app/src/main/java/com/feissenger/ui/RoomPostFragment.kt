package com.feissenger.ui

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.ConnectivityReceiver
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRoomBinding
import com.feissenger.databinding.FragmentRoomPostBinding
import com.feissenger.ui.adapter.RoomsAdapter
import com.feissenger.ui.viewModels.RoomPostViewModel
import com.feissenger.ui.viewModels.RoomsViewModel
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.navArgs
import com.feissenger.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_room_post.*


class RoomPostFragment : Fragment() {
    private lateinit var viewModel: RoomPostViewModel
    private lateinit var binding: FragmentRoomPostBinding
    private lateinit var sharedPref: MySharedPreferences

    val arg: RoomPostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = context?.let { MySharedPreferences(it) }!!

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room_post, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(RoomPostViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
            viewModel.access = get("access").toString()
        }

        binding.model = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment","roomsPost")

//        editTextRoomPost.requestFocus()
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInputFromWindow(view.windowToken,SHOW_IMPLICIT, SHOW_IMPLICIT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val roomName = if (arg.roomId == "XsTDHS3C2YneVmEW5Ry7")
            "Public Room"
        else
            arg.roomId
        (activity as MainActivity).myToolbar.toolbar_text.text = roomName
//        (activity as MainActivity).myToolbar.theme_icon.visibility = View.GONE
    }
}
