package com.example.foodieme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.foodieme.R
import com.example.foodieme.databinding.FragmentMainBinding

import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var pagerTab: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)


        viewPager = binding.viewPager
        pagerTab = binding.pagerTab


        pagerTab.tabGravity = TabLayout.GRAVITY_FILL



        val pagerAdapter = HomePagerAdapter(parentFragmentManager)

        pagerTab.setupWithViewPager(viewPager)

        viewPager.adapter = pagerAdapter
        pagerTab.setTabIconActive(pagerTab.getTabAt(0), R.drawable.home_key)
        pagerTab.setTabIconActive(pagerTab.getTabAt(1), R.drawable.order)
        pagerTab.setTabIconActive(pagerTab.getTabAt(2), R.drawable.ic_baseline_favorite_border_24)
        pagerTab.setTabIconActive(pagerTab.getTabAt(3), R.drawable.ic_baseline_perm_identity_24)


        binding.lifecycleOwner = this

        return binding.root

    }



}




private fun TabLayout.setTabIconActive(tabAt: TabLayout.Tab?, drawable: Int) {
    tabAt?.icon = ContextCompat.getDrawable(context, drawable)
}


private class HomePagerAdapter(fa: androidx.fragment.app.FragmentManager) :
    FragmentPagerAdapter(fa) {
    private val NUM_PAGES = 4


    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> HomeScreenFragment()
            1 -> DeliveryPageFragment()
            2 -> AddToCartFragment()
            3 -> AboutFragment()
            else -> HomeScreenFragment()
        }
    }

    override fun getCount(): Int = NUM_PAGES




}
