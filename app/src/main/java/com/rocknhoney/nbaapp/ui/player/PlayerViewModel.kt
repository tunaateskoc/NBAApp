package com.rocknhoney.nbaapp.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocknhoney.nbaapp.model.Meta
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.roomdb.repository.player.PlayerRepositoryInterface
import com.rocknhoney.nbaapp.util.ResultWrapper
import com.rocknhoney.nbaapp.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepositoryInterface
) : ViewModel() {

    val players = repository.getPlayers()

    val status = MutableLiveData<Status>()

    val meta = MutableLiveData<Meta>()

    private suspend fun insertPlayers(newPlayers: List<Player>) {
        viewModelScope.launch {
            repository.insertPlayers(*newPlayers.toTypedArray())
        }
    }


    private fun changeStatusAccordingToDB() {
        if(!players.value.isNullOrEmpty()){
            status.value = Status.SUCCESS
        }else{
            status.value = Status.ERROR
        }
    }

    suspend fun getPlayersFromAPI(pageNumber: Int) {
        viewModelScope.launch {
            if(pageNumber == 0){
                status.value = Status.LOADING
            }else{
                status.value = Status.PAGING
            }
            val response = repository.getPlayersFromAPI(pageNumber)
            if (response.status == Status.SUCCESS) {
                response.data?.let { PlayerRes ->
                    if(PlayerRes.players != repository.getPlayers().value){
                        storePlayerDataInDatabase(PlayerRes.players)
                        meta.value = PlayerRes.meta
                    }
                }
            }else{
                changeStatusAccordingToDB()
            }
        }
    }

    private suspend fun storePlayerDataInDatabase(playersDataFromAPI: List<Player>) {
        insertPlayers(playersDataFromAPI)
    }
}