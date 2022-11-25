package com.example.appposvendas.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("SessionId")
    var session: String,
    @SerializedName("Version")
    var versao: String,
    @SerializedName("SessionTimeout")
    var time: Int
)
