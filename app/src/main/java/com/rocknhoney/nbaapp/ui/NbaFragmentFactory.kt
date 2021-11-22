package com.rocknhoney.nbaapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.rocknhoney.nbaapp.api.RetrofitAPI
import com.rocknhoney.nbaapp.ui.detail.PlayerDetailFragment
import com.rocknhoney.nbaapp.ui.player.PlayerFragment
import com.rocknhoney.nbaapp.ui.team.TeamFragment
import com.rocknhoney.nbaapp.util.ConnectionLiveData
import javax.inject.Inject

class NbaFragmentFactory @Inject constructor(var connectionLiveData: ConnectionLiveData): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            PlayerFragment::class.java.name -> PlayerFragment(connectionLiveData)
            TeamFragment::class.java.name -> TeamFragment(connectionLiveData)
            PlayerDetailFragment::class.java.name -> PlayerDetailFragment()
            else -> return super.instantiate(classLoader, className)
        }
    }
}