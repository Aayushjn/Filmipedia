package com.aayush.filmipedia.util.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class BottomNavAdapter(fragmentManager: FragmentManager): SmartFragmentStatePagerAdapter(fragmentManager) {
    private val fragments = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) = fragments.add(fragment)

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}