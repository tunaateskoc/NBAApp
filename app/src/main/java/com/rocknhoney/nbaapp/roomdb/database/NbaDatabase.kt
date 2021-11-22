package com.rocknhoney.nbaapp.roomdb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rocknhoney.nbaapp.model.Player
import com.rocknhoney.nbaapp.model.Team
import com.rocknhoney.nbaapp.roomdb.dao.PlayerDao
import com.rocknhoney.nbaapp.roomdb.dao.TeamDao

@Database(entities = [Player::class, Team::class], version = 1)
abstract class NbaDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
}