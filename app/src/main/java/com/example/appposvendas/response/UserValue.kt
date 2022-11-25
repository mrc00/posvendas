package com.example.appposvendas.response

import com.google.gson.annotations.SerializedName

data class UserValue (
    @SerializedName("value")
    var user: List<UsersResponse> = mutableListOf()
)