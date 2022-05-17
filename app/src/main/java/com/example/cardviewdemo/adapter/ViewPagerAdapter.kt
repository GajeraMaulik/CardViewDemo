package com.example.cardviewdemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {
    private val listFragment: MutableList<Fragment> = ArrayList()
    private val listTitles: MutableList<String> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listTitles.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragment.add(fragment)
        listTitles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitles[position]
    }
}