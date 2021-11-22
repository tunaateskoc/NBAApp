package com.rocknhoney.nbaapp.roomdb.repository.player

import androidx.lifecycle.LiveData
import com.rocknhoney.nbaapp.api.RetrofitAPI
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.model.PlayersRes
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.roomdb.dao.PlayerDao
import com.rocknhoney.nbaapp.util.ResultWrapper
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val retrofitAPI: RetrofitAPI
) : PlayerRepositoryInterface {
    override fun getPlayers(): LiveData<List<Player>> {
        return playerDao.getPlayers()
    }

    override fun getPlayersByTeam(team: Team): LiveData<List<Player>> {
        return playerDao.getPlayersByTeam(team)
    }

    override suspend fun insertPlayers(vararg players: Player) {
        playerDao.insertPlayers(*players)
    }

    override suspend fun deleteAllPlayers() {
        playerDao.deleteAllPlayers()
    }

    override suspend fun getPlayersFromAPI(pageNumber: Int): ResultWrapper<PlayersRes> {
        return try {
            val response = retrofitAPI.getPlayers(pageNumber, 20)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let ResultWrapper.success(it)
                } ?: ResultWrapper.error("Error", null)
            } else {
                ResultWrapper.error("Error", null)
            }
        } catch (e: Exception) {
            ResultWrapper.error("No data", null)
        }
    }
}