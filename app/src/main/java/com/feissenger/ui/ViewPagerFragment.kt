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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = MySharedPreferences(context!!)

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
