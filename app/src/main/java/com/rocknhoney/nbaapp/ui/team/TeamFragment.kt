package com.rocknhoney.nbaapp.ui.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rocknhoney.nbaapp.R
import com.rocknhoney.nbaapp.adapter.FilterClickedListener
import com.rocknhoney.nbaapp.adapter.SearchTextChangeListener
import com.rocknhoney.nbaapp.adapter.TeamsListAdapter
import com.rocknhoney.nbaapp.databinding.FragmentTeamBinding
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.util.ConnectionLiveData
import com.rocknhoney.nbaapp.util.Status
import com.rocknhoney.nbaapp.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamFragment constructor(
    var connectionLiveData: ConnectionLiveData
) : Fragment(), SearchTextChangeListener, FilterClickedListener {

    private lateinit var viewModel: TeamViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var progressBar: ProgressBar

    private lateinit var errorText: TextView

    private lateinit var errorImage: ImageView

    private lateinit var teamBinding: FragmentTeamBinding

    private lateinit var recyclerView: RecyclerView

    private var teamsList: ArrayList<Team> = ArrayList()

    private lateinit var teamsListAdapter: TeamsListAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private var isInternetConnected = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TeamViewModel::class.java)

        teamBinding = FragmentTeamBinding.bind(view)

        initView()

        setupListeners()

        initRecyclerView()

        initObservers()

        getDataFromAPI()

    }

    private fun initView() {
        swipeRefreshLayout = teamBinding.swipeRefreshLayout
        progressBar = teamBinding.wrapperView.progressBar
        errorText = teamBinding.wrapperView.errorText
        errorImage = teamBinding.wrapperView.errorImage
        recyclerView = teamBinding.teamsRecyclerView
    }

    private fun setupListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            if(isInternetConnected)
                getDataFromAPI()
        }
    }

    private fun initRecyclerView() {
        teamsListAdapter = TeamsListAdapter()
        teamsListAdapter.teams = teamsList
        layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = teamsListAdapter
        teamsListAdapter.notifyDataSetChanged()
    }

    private fun initObservers() {
        viewModel.status.observe(requireActivity(), Observer { characters ->
            characters.let { status ->
                if (status == Status.SUCCESS) {
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.GONE
                    errorImage.visibility = View.GONE
                } else if (status == Status.LOADING) {
                    recyclerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    errorText.visibility = View.GONE
                    errorImage.visibility = View.GONE
                } else if (status == Status.ERROR) {
                    recyclerView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                    errorImage.visibility = View.VISIBLE
                    if(isInternetConnected){
                        errorText.text = Util.NO_DATA
                        errorImage.setImageResource(R.drawable.no_data)
                    }else{
                        errorText.text = Util.NO_NETWORK
                        errorImage.setImageResource(R.drawable.no_internet)
                    }
                }
            }
        })
        viewModel.teamsList.observe(requireActivity(), Observer { teams ->
            teams?.let {
                teamsList.clear()
                teamsList.addAll(teams)
                teamsListAdapter.notifyDataSetChanged()
                if(teamsList.isNotEmpty())
                    viewModel.status.value = Status.SUCCESS
            }
        })

        connectionLiveData.observe(requireActivity(), Observer { isConnected ->
            isInternetConnected = isConnected
            determineStateOfPage()
        })
    }

    private fun getDataFromAPI() {
        lifecycleScope.launch {
            viewModel.getTeamsFromAPI()
            delay(1000)
        }
    }

    override fun onSearchTextChanged(searchText: String) {
        teamsListAdapter.filterByName(teamsList, searchText)
    }

    override fun onFilterClicked(conferenceName: String) {
        teamsListAdapter.filterByConference(teamsList, conferenceName)
    }


    private fun determineStateOfPage(){
        if (!isInternetConnected && teamsList.isEmpty()) {
            viewModel.status.value = Status.ERROR
        }
    }
}