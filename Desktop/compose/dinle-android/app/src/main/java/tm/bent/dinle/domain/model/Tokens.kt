package com.mono.music.domain.models

import com.google.gson.annotations.SerializedName


data class Token(
    val access: String,
    val refresh: String,
    @SerializedName("logged_in_first_time")
    val firstTime: Boolean,
)