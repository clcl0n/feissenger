package com.feissenger.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.databinding.FragmentRoomBinding
import com.feissenger.ui.adapter.RoomsAdapter
import com.feissenger.ui.viewModels.RoomsViewModel
import com.feissenger.data.util.Injection
import com.reddit.indicatorfastscroll.FastScrollItemIndicator

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

        viewModel.rooms.observeForever{
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
        viewModel.loadRooms()

        binding.roomFastscroller.apply {
            setupWithRecyclerView(
                binding.messagesList,
                {
                        position ->
                    val item = adapter.data[position]
                    FastScrollItemIndicator.Text(
                        item.id.ssid.substring(0, 1).toUpperCase()
                    )
                }
            )
        }

        binding.roomFastscrollerThumb.setupWithFastScroller(binding.roomFastscroller)

        return binding.root
    }

    override fun onResume() {
        viewModel.loadRooms()
        super.onResume()
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
        viewModel.loadRooms()

        sharedPref.put("fragment","rooms")
    }


}
