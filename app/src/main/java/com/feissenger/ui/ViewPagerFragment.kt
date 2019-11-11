package com.feissenger.ui


import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R

import com.feissenger.ui.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_view_pager.*


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



       return inflater.inflate(R.layout.fragment_view_pager,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = ViewPagerAdapter(childFragmentManager)
        view_pager.adapter = fragmentAdapter
        tab_layout.setupWithViewPager(view_pager)
    }
}
