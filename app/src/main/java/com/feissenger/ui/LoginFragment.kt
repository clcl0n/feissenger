package com.feissenger.ui


import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_login.*
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.ConnectivityReceiver
import com.feissenger.data.api.WebApi
import com.feissenger.data.api.model.RegisterTokenRequest
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentLoginBinding
import com.feissenger.ui.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.view.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//, ConnectivityReceiver.ConnectivityReceiverListener
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref: MySharedPreferences
//    private lateinit var wifiManager: WifiManager

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
            inflater, R.layout.fragment_login, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(LoginViewModel::class.java)

        binding.model = viewModel

//        viewModel.user.observe(this) {
//            val navController = findNavController()
//            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
//            navGraph.startDestination = R.id.room_fragment
//            navController.graph = navGraph
//            navController.navigate(R.id.room_fragment)
//        }

//        adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.put("fragment", "login")

        val navController = findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        goto_registration_btn.setOnClickListener {
            navGraph.startDestination = R.id.login_fragment
            navController.graph = navGraph

            navController.navigate(R.id.registration_fragment)
        }

        viewModel.user.observeForever {
            val id: Int
            if (it != null) {
                id = R.id.viewPagerFragment
                with(sharedPref) {
                    put("access", it.access)
                    put("refresh", it.refresh)
                    put("uid", it.uid)
                    put("name", it.name)
                }


            } else {
                id = R.id.login_fragment
                sharedPref.clear()
                GlobalScope.launch {
                    FirebaseInstanceId.getInstance().deleteInstanceId()

                }
            }

            navGraph.startDestination = id
            navController.graph = navGraph
            navController.navigate(id)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).myToolbar.toolbar_text.text = "FEIssenger"
    }

//    /**
//     * Callback will be called when there is change
//     */
//    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        showMessage(isConnected)
//    }

//    private fun showMessage(isConnected: Boolean) {
//        if (isConnected) {
//            if (wifiManager.connectionInfo.hiddenSSID){
//                sharedPref.put("activeWifi",wifiManager.connectionInfo.bssid.removeSurrounding("\"","\""))
//            }
//            else{
//                sharedPref.put("activeWifi",wifiManager.connectionInfo.ssid.removeSurrounding("\"","\""))
//            }
//        } else {
//            sharedPref.put("activeWifi","")
//        }
//    }
}
