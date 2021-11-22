package com.rocknhoney.nbaapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rocknhoney.nbaapp.ui.player.PlayerFragment
import com.rocknhoney.nbaapp.ui.team.TeamFragment

class ViewPagerAdapter(context: FragmentActivity) : FragmentStateAdapter(context) {

    private val fragmentFactory = context.supportFragmentManager.fragmentFactory

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                fragmentFactory.instantiate(
                    ClassLoader.getSystemClassLoader(),
                    PlayerFragment::class.java.name
                )
            }
            1 -> {
                fragmentFactory.instantiate(
                    ClassLoader.getSystemClassLoader(),
                    TeamFragment::class.java.name
                )
            }
            else -> {
                Fragment()
            }
        }
    }
}