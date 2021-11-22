package com.rocknhoney.nbaapp.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rocknhoney.nbaapp.model.Team

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams")
    fun getTeams(): LiveData<List<Team>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(vararg teams: Team): List<Long>

    @Query("DELETE FROM teams")
    suspend fun deleteAllTeams()
}