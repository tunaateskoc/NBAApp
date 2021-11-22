package com.rocknhoney.nbaapp.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rocknhoney.nbaapp.R
import com.rocknhoney.nbaapp.adapter.ItemFilterAdapter
import com.rocknhoney.nbaapp.adapter.ViewPagerAdapter
import com.rocknhoney.nbaapp.databinding.BottomSheetDialogConferenceBinding
import com.rocknhoney.nbaapp.databinding.BottomSheetDialogTeamBinding
import com.rocknhoney.nbaapp.databinding.FragmentMainBinding
import com.rocknhoney.nbaapp.model.TeamFilter
import com.rocknhoney.nbaapp.ui.player.PlayerFragment
import com.rocknhoney.nbaapp.ui.team.TeamFragment


class MainFragment : Fragment() {

    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager2

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var searchText: EditText

    private lateinit var filterIv: ImageView

    private lateinit var binding: FragmentMainBinding

    private var teamFiltered = ""

    private var selectionGrey = 0

    private var filterGrey = 0

    private var colorWhite = 0

    companion object {
        var teamFilterArrayList: ArrayList<TeamFilter> = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        initView()

        setupViewPager()

        setupSearch()

        createTeamFilterArray()

        initColors()

        setupFilterListener()

    }


    private fun initView() {
        filterIv = binding.filterIv
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        searchText = binding.searchEt
    }

    private fun setupViewPager() {
        viewPagerAdapter = activity?.let {
            ViewPagerAdapter(
                it
            )
        }!!
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Players"
                1 -> tab.text = "Teams"
            }
        }.attach()

        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                searchText.setText("")
                filterIv.imageTintList =
                    ColorStateList.valueOf(filterGrey)
                for (team in teamFilterArrayList){
                    team.isSelected = false
                }
            }
        })
        viewPager.isUserInputEnabled = false
    }

    private fun setupSearch() {
        searchText.addTextChangedListener {
            it?.let {
                val userInputSearchText = it.toString()
                val fragments = activity?.supportFragmentManager?.fragments
                if (tabLayout.selectedTabPosition == 0) {
                    val playerFragment = fragments?.get(1) as PlayerFragment
                    playerFragment.onSearchTextChanged(userInputSearchText)
                } else if (tabLayout.selectedTabPosition == 1 && fragments?.size == 3) {
                    val teamFragment = fragments[2] as TeamFragment
                    teamFragment.onSearchTextChanged(userInputSearchText)
                }
            }
        }
    }

    private fun setupFilterListener() {
        filterIv.setOnClickListener {
            if (tabLayout.selectedTabPosition == 0) {
                showFilterByTeamDialog()
            } else if (tabLayout.selectedTabPosition == 1) {
                showFilterByConferenceDialog()
            }
        }
    }

    private fun initColors() {
        selectionGrey = ContextCompat.getColor(requireContext(), R.color.light_grey)

        colorWhite = ContextCompat.getColor(requireContext(), R.color.white)

        filterGrey = ContextCompat.getColor(requireContext(), R.color.grey)

        filterIv.imageTintList = ColorStateList.valueOf(filterGrey)
    }


    private fun showFilterByConferenceDialog() {
        val myDrawerView = layoutInflater.inflate(R.layout.bottom_sheet_dialog_conference, null)
        val binding = BottomSheetDialogConferenceBinding.bind(myDrawerView)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(myDrawerView)
        bottomSheetDialog.show()

        val eastChoice = binding.conferenceEast
        val westChoice = binding.conferenceWest

        if(teamFiltered == "East"){
            eastChoice.setBackgroundColor(selectionGrey)
        }else if(teamFiltered == "West"){
            westChoice.setBackgroundColor(selectionGrey)
        }

        eastChoice.setOnClickListener {
            val fragments = activity?.supportFragmentManager?.fragments
            val teamFragment = fragments?.get(2) as TeamFragment
            if (teamFiltered == "East") {
                teamFragment.onFilterClicked("")
                filterIv.imageTintList = ColorStateList.valueOf(filterGrey)
                teamFiltered = ""
            } else {
                filterIv.imageTintList = ColorStateList.valueOf(colorWhite)
                teamFragment.onFilterClicked("East")
                teamFiltered = "East"
            }

            bottomSheetDialog.dismiss()
        }

        westChoice.setOnClickListener {
            val fragments = activity?.supportFragmentManager?.fragments
            val teamFragment = fragments?.get(2) as TeamFragment

            if (teamFiltered == "West") {
                teamFragment.onFilterClicked("")
                filterIv.imageTintList = ColorStateList.valueOf(filterGrey)
                teamFiltered = ""
            } else {
                filterIv.imageTintList = ColorStateList.valueOf(colorWhite)
                teamFragment.onFilterClicked("West")
                teamFiltered = "West"
            }
            bottomSheetDialog.dismiss()
        }

    }

    private fun showFilterByTeamDialog() {
        val myDrawerView = layoutInflater.inflate(R.layout.bottom_sheet_dialog_team, null)
        val binding = BottomSheetDialogTeamBinding.bind(myDrawerView)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(myDrawerView)
        bottomSheetDialog.show()

        val teamFilterGridView = binding.bottomSheetTeamGridView


        val adapter = ItemFilterAdapter(requireContext(), 0, teamFilterArrayList)
        teamFilterGridView.adapter = adapter

        teamFilterGridView.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val fragments = activity?.supportFragmentManager?.fragments
                val playerFragment = fragments?.get(1) as PlayerFragment
                if(teamFilterArrayList[position].isSelected){
                    filterIv.imageTintList = ColorStateList.valueOf(filterGrey)
                    playerFragment.onFilterClicked("")
                    teamFilterArrayList[position].isSelected = false
                }else{
                    for (team in teamFilterArrayList){
                        team.isSelected = false
                    }
                    filterIv.imageTintList = ColorStateList.valueOf(colorWhite)
                    teamFilterArrayList[position].isSelected = true
                    playerFragment.onFilterClicked(teamFilterArrayList[position].name)
                }
                bottomSheetDialog.dismiss()
            }

    }

    private fun createTeamFilterArray() {
        teamFilterArrayList.clear()

        teamFilterArrayList.add(TeamFilter("Bulls", R.drawable.bulls))
        teamFilterArrayList.add(TeamFilter("Bucks", R.drawable.bucks))
        teamFilterArrayList.add(TeamFilter("Celtics", R.drawable.celtics))
        teamFilterArrayList.add(TeamFilter("Warriors", R.drawable.warriors))
        teamFilterArrayList.add(TeamFilter("Hawks", R.drawable.hawks))
        teamFilterArrayList.add(TeamFilter("76ers", R.drawable.phili))
        teamFilterArrayList.add(TeamFilter("Hornets", R.drawable.hornets))
        teamFilterArrayList.add(TeamFilter("Grizzlies", R.drawable.memphis))
        teamFilterArrayList.add(TeamFilter("Wizards", R.drawable.wizards))
        teamFilterArrayList.add(TeamFilter("Lakers", R.drawable.lakers))
        teamFilterArrayList.add(TeamFilter("Pelicans", R.drawable.pelicans))
        teamFilterArrayList.add(TeamFilter("TimberWolves", R.drawable.timberwolves))
        teamFilterArrayList.add(TeamFilter("Knicks", R.drawable.knicks))
        teamFilterArrayList.add(TeamFilter("Raptors", R.drawable.raptors))
        teamFilterArrayList.add(TeamFilter("Heat", R.drawable.heat))
        teamFilterArrayList.add(TeamFilter("Pacers", R.drawable.pacers))
        teamFilterArrayList.add(TeamFilter("Jazz", R.drawable.jazz))
        teamFilterArrayList.add(TeamFilter("Nuggets", R.drawable.nuggets))
        teamFilterArrayList.add(TeamFilter("Cavaliers", R.drawable.cavaliers))
        teamFilterArrayList.add(TeamFilter("Spurs", R.drawable.spurs))
        teamFilterArrayList.add(TeamFilter("Nets", R.drawable.nets))
        teamFilterArrayList.add(TeamFilter("TrailBlazers", R.drawable.trailblazers))
        teamFilterArrayList.add(TeamFilter("Suns", R.drawable.suns))
        teamFilterArrayList.add(TeamFilter("Kings", R.drawable.kings))
        teamFilterArrayList.add(TeamFilter("Pistons", R.drawable.pistons))
        teamFilterArrayList.add(TeamFilter("Magic", R.drawable.magic))
        teamFilterArrayList.add(TeamFilter("Rockets", R.drawable.rockets))
        teamFilterArrayList.add(TeamFilter("Mavericks", R.drawable.mavericks))
        teamFilterArrayList.add(TeamFilter("Thunder", R.drawable.thunder))
        teamFilterArrayList.add(TeamFilter("Clippers", R.drawable.clippers))

    }

}