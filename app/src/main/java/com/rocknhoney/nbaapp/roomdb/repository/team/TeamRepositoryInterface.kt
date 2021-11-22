package com.rocknhoney.nbaapp.roomdb.repository.team

import androidx.lifecycle.LiveData
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.model.TeamsRes
import com.rocknhoney.nbaapp.util.ResultWrapper

interface TeamRepositoryInterface {
    fun getTeams(): LiveData<List<Team>>

    suspend fun insertTeams(vararg teams: Team): List<Long>

    suspend fun deleteAllTeams()

    suspend fun getTeamsFromAPI(): ResultWrapper<TeamsRes>
}