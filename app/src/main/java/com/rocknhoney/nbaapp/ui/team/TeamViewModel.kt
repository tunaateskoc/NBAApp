package com.rocknhoney.nbaapp.ui.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.FtsOptions.Order
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.roomdb.repository.team.TeamRepositoryInterface
import com.rocknhoney.nbaapp.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject


@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: TeamRepositoryInterface
) : ViewModel() {

    val teamsList = repository.getTeams()

    val status = MutableLiveData<Status>()

    private suspend fun insertTeams(newTeams: List<Team>) {
        viewModelScope.launch {
            repository.insertTeams(*newTeams.toTypedArray())
        }
    }

    private fun changeStatusAccordingToDB(){
         if(!teamsList.value.isNullOrEmpty()){
             status.value = Status.SUCCESS
         }else{
             status.value = Status.ERROR
         }
    }

    suspend fun getTeamsFromAPI() {
        status.value = Status.LOADING
        val response = repository.getTeamsFromAPI()
        if (response.status == Status.SUCCESS) {
            response.data?.let { TeamRes ->
                if (TeamRes.teams != repository.getTeams().value) {
                    storePlayerDataInDatabase(TeamRes.teams)
                }
            }
        }else{
            changeStatusAccordingToDB()
        }
    }

    private suspend fun storePlayerDataInDatabase(teamsDataFromAPI: List<Team>) {
        insertTeams(teamsDataFromAPI)
    }
}