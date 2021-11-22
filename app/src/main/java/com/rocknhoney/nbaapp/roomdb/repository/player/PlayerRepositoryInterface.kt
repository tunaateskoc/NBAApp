package com.rocknhoney.nbaapp.roomdb.repository.player

import androidx.lifecycle.LiveData
import com.rocknhoney.nbaapp.util.ResultWrapper
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.model.PlayersRes
import com.rocknhoney.nbaapp.model.Team

interface PlayerRepositoryInterface {

    fun getPlayers(): LiveData<List<Player>>

    fun getPlayersByTeam(team: Team): LiveData<List<Player>>

    suspend fun insertPlayers(vararg players: Player)

    suspend fun deleteAllPlayers()

    suspend fun getPlayersFromAPI(pageNumber: Int): ResultWrapper<PlayersRes>

}