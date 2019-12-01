package com.feissenger.ui


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
import com.feissenger.R

import com.feissenger.ui.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_view_pager.*


class ViewPagerFragment : Fragment() {

    private lateinit var sharedPref: MySharedPreferences
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var wifiManager: WifiManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = MySharedPreferences(context!!)
        connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if(connectivityManager.activeNetwork != null){
            if(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork).hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                if (wifiManager.connectionInfo.hiddenSSID){
                    sharedPref.put("activeWifi",wifiManager.connectionInfo.bssid.removeSurrounding("\"","\""))
                }
                else{
                    sharedPref.put("activeWifi",wifiManager.connectionInfo.ssid.removeSurrounding("\"","\""))
                }
            }
            else{
                sharedPref.put("activeWifi","")
            }
        }else{
            sharedPref.put("activeWifi","")
        }

       return inflater.inflate(R.layout.fragment_view_pager,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter = ViewPagerAdapter(childFragmentManager)
        view_pager.adapter = fragmentAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).myToolbar.toolbar_text.text = sharedPref.get("name").toString()
        (activity as MainActivity).myToolbar.theme_icon.visibility = View.VISIBLE
        (activity as MainActivity).myToolbar.logout_icon.visibility = View.VISIBLE
    }
}
