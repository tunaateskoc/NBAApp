package com.rocknhoney.nbaapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rocknhoney.nbaapp.R
import com.rocknhoney.nbaapp.databinding.FragmentTeamDetailBinding
import com.rocknhoney.nbaapp.ui.MainFragment
import com.rocknhoney.nbaapp.util.Util
import java.util.*

class TeamDetailFragment : Fragment() {

    private lateinit var teamDetailBinding: FragmentTeamDetailBinding

    private val args by navArgs<TeamDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamDetailBinding = FragmentTeamDetailBinding.bind(view)

        initView()
    }

    private fun initView() {
        val team = args.team

        Util.setSpannableString(
            "Detail of ",
            team.name.toString(),
            teamDetailBinding.teamDetailHeader
        )
        Util.setSpannableString(
            "Abbreviation: ",
            team.abbreviation.toString(),
            teamDetailBinding.teamDetailAbbreviation
        )
        Util.setSpannableString("City: ", team.city.toString(), teamDetailBinding.teamDetailCity)
        Util.setSpannableString(
            "Conference: ",
            team.conference.toString(),
            teamDetailBinding.teamDetailConference
        )
        Util.setSpannableString(
            "Division: ",
            team.division.toString(),
            teamDetailBinding.teamDetailDivision
        )
        Util.setSpannableString(
            "Full Name: ",
            team.fullName.toString(),
            teamDetailBinding.teamDetailFullName
        )

        val teamArray = MainFragment.teamFilterArrayList
        for (teamItem in teamArray) {
            if (team.name?.toLowerCase(Locale.ROOT).equals(teamItem.name.toLowerCase(Locale.ROOT))) {
                teamDetailBinding.teamDetailIcon.setImageResource(teamItem.iconId)
            }
        }
    }
}