package com.rocknhoney.nbaapp.model

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("total_pages") val totalPages: Long,
    @SerializedName("current_page") val currentPage: Long,
    @SerializedName("next_page") val nextPage: Long?,
    @SerializedName("per_page") val perPage: Long,
    @SerializedName("total_count") val totalCount: Long,
)