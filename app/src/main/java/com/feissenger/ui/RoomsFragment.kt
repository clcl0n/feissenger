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
//, ConnectivityReceiver.ConnectivityReceiverListener
class RoomsFragment : Fragment() {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var binding: FragmentRoomBinding
//    private lateinit var wifiManager: WifiManager
    private lateinit var sharedPref: MySharedPreferences
    private lateinit var adapter: RoomsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        activity?.registerReceiver(ConnectivityReceiver(),
//            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        )
        sharedPref = context?.let { MySharedPreferences(it) }!!
//        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(activity!!, Injection.provideViewModelFactory(context!!))
            .get(RoomsViewModel::class.java)

        with(sharedPref) {
            viewModel.uid = get("uid").toString()
            viewModel.access = get("access").toString()
            viewModel.activeWifi = get("activeWifi").toString()
        }


        binding.model = viewModel

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)



        adapter = RoomsAdapter()
        binding.messagesList.adapter = adapter


//        viewModel.rooms.observe(this) {
//            adapter.data = it
//        }

        viewModel.activeRoom.observeForever{
            viewModel.refreshRooms()
        }

        viewModel.mutableRooms.observeForever{
            adapter.data = it
        }

        binding.activeRoom.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(
                viewModel.activeRoom.value!!
            )
            it.findNavController().navigate(action)
        }

        binding.publicRoom.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(
                "XsTDHS3C2YneVmEW5Ry7"
            )
            it.findNavController().navigate(action)
        }

        return binding.root
    }
//
//    private fun showMessage(isConnected: Boolean) {
//        if (isConnected) {
//            if (wifiManager.connectionInfo.hiddenSSID){
//                sharedPref.put("activeWifi",wifiManager.connectionInfo.bssid.removeSurrounding("\"","\""))
//                viewModel.activeWifi = wifiManager.connectionInfo.bssid.removeSurrounding("\"","\"")
//                viewModel.setActiveRoom()
//            }
//            else{
//                sharedPref.put("activeWifi",wifiManager.connectionInfo.ssid.removeSurrounding("\"","\""))
//                viewModel.activeWifi = wifiManager.connectionInfo.ssid.removeSurrounding("\"","\"")
//                viewModel.setActiveRoom()
//            }
//        } else {
//            sharedPref.put("activeWifi","")
//            viewModel.activeWifi = ""
//            viewModel.setActiveRoom()
//        }
//    }


//    /**
//     * Callback will be called when there is change
//     */
//    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        showMessage(isConnected)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref.put("fragment","rooms")
    }


}
