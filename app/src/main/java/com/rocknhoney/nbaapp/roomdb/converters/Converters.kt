package com.rocknhoney.nbaapp.roomdb.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rocknhoney.nbaapp.model.Team

class Converters {
    private val gson = Gson()
    @TypeConverter
    fun stringToTeam(data: String?): Team {
        if (data == null) {
            return Team(0,"","","","","","")
        }

        val listType = object : TypeToken<Team>() {

        }.type

        return gson.fromJson<Team>(data, listType)
    }
    @TypeConverter
    fun teamToString(team: Team): String {
        return gson.toJson(team)
    }

    @TypeConverter
    fun stringToList(data: String?): ArrayList<String> {
        if (data == null) {
            return ArrayList()
        }

        val listType = object : TypeToken<ArrayList<String>>() {

        }.type

        return gson.fromJson<ArrayList<String>>(data, listType)
    }
    @TypeConverter
    fun listToString(list: ArrayList<String?>?): String {
        return gson.toJson(list)
    }

}