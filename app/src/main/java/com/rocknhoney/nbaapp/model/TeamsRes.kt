package com.rocknhoney.nbaapp.model

import com.google.gson.annotations.SerializedName

data class TeamsRes(
    @SerializedName("data") val teams: List<Team>,
    @SerializedName("meta") val meta: Meta,
)