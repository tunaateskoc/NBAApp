package com.rocknhoney.nbaapp.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.roomdb.converters.Converters

@TypeConverters(Converters::class)
@Dao
interface PlayerDao {

    @Query("SELECT * FROM players")
    fun getPlayers(): LiveData<List<Player>>

    @Query("SELECT * FROM players WHERE team =:team")
    fun getPlayersByTeam(team: Team): LiveData<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(vararg players: Player)


    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()
}