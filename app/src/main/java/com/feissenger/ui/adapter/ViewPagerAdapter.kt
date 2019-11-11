package com.feissenger.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.feissenger.ui.ContactListFragment
import com.feissenger.ui.RoomsFragment

class ViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return RoomsFragment()
            1 -> return ContactListFragment()
        }
        return RoomsFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Room Tab"
            1 -> "Contacts Tab"
            else -> {
                return "Room Tab"
            }
        }
    }

}