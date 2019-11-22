package com.feissenger.ui


import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.ConnectivityReceiver
import com.feissenger.databinding.FragmentRoomBinding
import com.feissenger.ui.adapter.RoomsAdapter
import com.feissenger.ui.viewModels.RoomsViewModel
import com.feissenger.data.util.Injection
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_room.*

class RoomsFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var binding: FragmentRoomBinding
    private lateinit var wifiManager: WifiManager
    private lateinit var toast: Toast
    private lateinit var sharedPref: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = context?.let { MySharedPreferences(it) }!!
        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(RoomsViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
            viewModel.access = get("access").toString()
        }

        binding.model = viewModel

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        activity?.registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        val adapter = RoomsAdapter()
        binding.messagesList.adapter = adapter

        viewModel.rooms.observe(this) {
            adapter.data = it
        }

        viewModel.loadRooms()

        binding.activeRoom.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(
                viewModel.activeRoom.value!!
            )
            it.findNavController().navigate(action)
        }

        binding.publicRoom.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(
                "Public Room"
            )
            it.findNavController().navigate(action)
        }

        return binding.root
    }

    private fun showMessage(isConnected: Boolean) {
        if (isConnected) {
            if (wifiManager.connectionInfo.hiddenSSID)
                viewModel.setActiveRoom(wifiManager.connectionInfo.bssid,true)
            else
                viewModel.setActiveRoom(wifiManager.connectionInfo.ssid,true)
        } else {
            viewModel.setActiveRoom("",false)
        }
    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment","rooms")

        FirebaseApp.initializeApp(context!!)
        val uid = sharedPref.get("uid")
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/msg_$uid")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("tag", "/topics/$uid")
                    toast = Toast.makeText(context, "/topics/$uid", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
    }
}
