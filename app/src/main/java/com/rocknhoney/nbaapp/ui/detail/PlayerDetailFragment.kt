package com.rocknhoney.nbaapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rocknhoney.nbaapp.R
import com.rocknhoney.nbaapp.databinding.FragmentPlayerDetailBinding
import com.rocknhoney.nbaapp.util.Util

class PlayerDetailFragment : Fragment() {

    private lateinit var playerDetailBinding: FragmentPlayerDetailBinding

    private val args by navArgs<PlayerDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerDetailBinding = FragmentPlayerDetailBinding.bind(view)

        initView()
    }

    private fun initView() {
        val player = args.player

        Util.setSpannableString(
            "Detail of ",
            player.firstName + " " + player.lastName,
            playerDetailBinding.playerDetailHeader
        )
        Util.setSpannableString(
            "Full name ",
            player.firstName + " " + player.lastName,
            playerDetailBinding.playerDetailFullName
        )
        Util.setSpannableString(
            "Position: ",
            player.position.toString(),
            playerDetailBinding.playerPosition
        )
        Util.setSpannableString(
            "Team Name: ",
            player.team?.fullName.toString(),
            playerDetailBinding.playerDetailTeamFullName
        )
        Util.setSpannableString(
            "Conference: ",
            player.team?.conference.toString(),
            playerDetailBinding.playerConference
        )
    }
}