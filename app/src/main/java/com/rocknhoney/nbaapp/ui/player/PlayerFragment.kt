package com.rocknhoney.nbaapp.ui.player

import android.os.Bundle
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
import com.rocknhoney.nbaapp.adapter.PlayersListAdapter
import com.rocknhoney.nbaapp.adapter.SearchTextChangeListener
import com.rocknhoney.nbaapp.databinding.FragmentPlayerBinding
import com.rocknhoney.nbaapp.model.Meta
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.util.ConnectionLiveData
import com.rocknhoney.nbaapp.util.Status
import com.rocknhoney.nbaapp.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment @Inject constructor(
    var connectionLiveData: ConnectionLiveData
) : Fragment(), SearchTextChangeListener, FilterClickedListener {

    private lateinit var viewModel: PlayerViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var pagingProgressBar: ProgressBar

    private lateinit var errorText: TextView

    private lateinit var errorImage: ImageView

    private lateinit var playerBinding: FragmentPlayerBinding

    private lateinit var recyclerView: RecyclerView

    private var playersList: ArrayList<Player> = ArrayList()

    private lateinit var playersListAdapter: PlayersListAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var currentMeta: Meta

    private var pageNumber = 1

    private var isInternetConnected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerBinding = FragmentPlayerBinding.bind(view)

        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        initView()

        initRecyclerView()

        initObservers()

        getDataFromAPI(0)

        setupListeners()

    }

    private fun initRecyclerView() {
        playersListAdapter = PlayersListAdapter()
        playersListAdapter.players = playersList
        layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = playersListAdapter
        playersListAdapter.notifyDataSetChanged()
    }

    private fun initView() {
        swipeRefreshLayout = playerBinding.swipeRefreshLayout
        loadingProgressBar = playerBinding.wrapperView.progressBar
        errorText = playerBinding.wrapperView.errorText
        errorImage = playerBinding.wrapperView.errorImage
        recyclerView = playerBinding.playersRecyclerView
        pagingProgressBar = playerBinding.pagingProgressBar
    }

    private fun setupListeners() {
        initRecyclerViewListener()
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            if (isInternetConnected) {
                pageNumber = 0
                getDataFromAPI(pageNumber)
                recyclerView.scrollToPosition(0)
            }
        }
    }

    private fun initObservers() {
        viewModel.status.observe(requireActivity(), Observer { players ->
            players.let { status ->
                if (status == Status.SUCCESS) {
                    recyclerView.visibility = View.VISIBLE
                    loadingProgressBar.visibility = View.GONE
                    pagingProgressBar.visibility = View.GONE
                    errorText.visibility = View.GONE
                    errorImage.visibility = View.GONE
                } else if (status == Status.LOADING) {
                    recyclerView.visibility = View.GONE
                    loadingProgressBar.visibility = View.VISIBLE
                    pagingProgressBar.visibility = View.GONE
                    errorText.visibility = View.GONE
                    errorImage.visibility = View.GONE
                } else if (status == Status.ERROR) {
                    recyclerView.visibility = View.GONE
                    loadingProgressBar.visibility = View.GONE
                    pagingProgressBar.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                    errorImage.visibility = View.VISIBLE
                    if (isInternetConnected) {
                        errorText.text = Util.NO_DATA
                        errorImage.setImageResource(R.drawable.no_data)
                    } else {
                        errorText.text = Util.NO_NETWORK
                        errorImage.setImageResource(R.drawable.no_internet)
                    }
                } else if (status == Status.PAGING) {
                    recyclerView.visibility = View.VISIBLE
                    pagingProgressBar.visibility = View.VISIBLE
                    loadingProgressBar.visibility = View.GONE
                    errorText.visibility = View.GONE
                    errorImage.visibility = View.GONE
                }
            }
        })
        viewModel.players.observe(requireActivity(), Observer { players ->
            players?.let {
                playersList.clear()
                playersList.addAll(players)
                pageNumber++
                playersListAdapter.notifyDataSetChanged()
                if(playersList.isNotEmpty())
                    viewModel.status.value = Status.SUCCESS
            }
        })
        viewModel.meta.observe(requireActivity(), Observer {
            currentMeta = it
        })
        connectionLiveData.observe(requireActivity(), Observer { isConnected ->
            isInternetConnected = isConnected
            determineStateOfPage()
        })
    }

    private fun getDataFromAPI(pageNumber: Int) {
        lifecycleScope.launch {
            viewModel.getPlayersFromAPI(pageNumber)
            delay(1000)
        }
    }

    private fun initRecyclerViewListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                swipeRefreshLayout.isEnabled =
                    layoutManager.findFirstCompletelyVisibleItemPosition() == 0
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val total = playersListAdapter.itemCount

                if (loadingProgressBar.visibility != View.VISIBLE) {
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        if (::currentMeta.isInitialized && currentMeta.nextPage != null) {
                            getDataFromAPI(pageNumber)
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun onSearchTextChanged(searchText: String) {
        if (playersList.isNotEmpty())
            playersListAdapter.filterByName(playersList, searchText)
    }

    override fun onFilterClicked(teamName: String) {
        playersListAdapter.filterByTeam(playersList, teamName)
    }

    private fun determineStateOfPage() {
        if (!isInternetConnected && playersList.isEmpty()) {
            viewModel.status.value = Status.ERROR
        }
    }
}