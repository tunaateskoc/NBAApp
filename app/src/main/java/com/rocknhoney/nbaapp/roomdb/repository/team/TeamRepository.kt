package com.rocknhoney.nbaapp.roomdb.repository.team

import androidx.lifecycle.LiveData
import com.rocknhoney.nbaapp.api.RetrofitAPI
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.model.TeamsRes
import com.rocknhoney.nbaapp.roomdb.dao.TeamDao
import com.rocknhoney.nbaapp.util.ResultWrapper
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val teamDao: TeamDao,
    private val retrofitAPI: RetrofitAPI
) : TeamRepositoryInterface {

    override fun getTeams(): LiveData<List<Team>> {
        return teamDao.getTeams()
    }

    override suspend fun insertTeams(vararg teams: Team): List<Long> {
        return teamDao.insertTeams(*teams)
    }

    override suspend fun deleteAllTeams() {
        teamDao.deleteAllTeams()
    }

    override suspend fun getTeamsFromAPI(): ResultWrapper<TeamsRes> {
        return try {
            val response = retrofitAPI.getTeams(0)
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