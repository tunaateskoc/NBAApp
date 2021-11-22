package com.rocknhoney.nbaapp.model

import com.google.gson.annotations.SerializedName

data class PlayersRes(
    @SerializedName("data") val players: List<Player>,
    @SerializedName("meta") val meta: Meta
)
