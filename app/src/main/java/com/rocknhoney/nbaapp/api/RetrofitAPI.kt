package com.rocknhoney.nbaapp.api

import com.rocknhoney.nbaapp.model.PlayersRes
import com.rocknhoney.nbaapp.model.TeamsRes
import com.rocknhoney.nbaapp.util.Util
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitAPI {

    @Headers(
        Util.HEADER_HOST+": "+ Util.API_HOST,
        Util.HEADER_KEY+": "+ Util.API_KEY
    )
    @GET("players")
    suspend fun getPlayers(
        @Query("page") pageNumber: Int,
        @Query("per_page") perPage: Int
    ): Response<PlayersRes>

    @Headers(
        Util.HEADER_HOST+": "+ Util.API_HOST,
        Util.HEADER_KEY+": "+ Util.API_KEY
    )
    @GET("teams")
    suspend fun getTeams(
        @Query("page") pageNumber: Int,
    ): Response<TeamsRes>
}