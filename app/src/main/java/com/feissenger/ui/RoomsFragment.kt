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
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.ConnectivityReceiver
import com.feissenger.databinding.FragmentRoomBinding
import com.feissenger.ui.adapter.RoomsAdapter
import com.feissenger.ui.viewModels.RoomsViewModel
import com.feissenger.data.util.Injection
import com.feissenger.ui.viewModels.SharedViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class RoomsFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentRoomBinding
    private lateinit var wifiManager: WifiManager
    private lateinit var toast: Toast
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_room, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(RoomsViewModel::class.java)


        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.uid = sharedViewModel.user.value!!.uid
        viewModel.access = sharedViewModel.user.value!!.access

        binding.model = viewModel

        binding.messagesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        activity?.registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        val adapter = RoomsAdapter(sharedViewModel)
        binding.messagesList.adapter = adapter

        viewModel.rooms.observe(this) {
            adapter.data = it
        }

        viewModel.loadRooms()

        return binding.root
    }

    private fun showMessage(isConnected: Boolean) {
        if (isConnected){
            if(wifiManager.connectionInfo.hiddenSSID)
                viewModel.setActiveRoom(wifiManager.connectionInfo.bssid)
            else
                viewModel.setActiveRoom(wifiManager.connectionInfo.ssid)

            toast = Toast.makeText(context,wifiManager.connectionInfo.ssid,Toast.LENGTH_SHORT)
            toast.show()
        }
        else{
            toast = Toast.makeText(context,"Data or No connection",Toast.LENGTH_SHORT)
            toast.show()
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

        FirebaseApp.initializeApp(context!!)
        val uid = sharedPref.getString("uid","")
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$uid")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.i("tag", "/topics/$uid")
                toast = Toast.makeText(context, "/topics/$uid", Toast.LENGTH_SHORT)
                toast.show()
            }
    }
}
