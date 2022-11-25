package com.example.appposvendas.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("CompanyDB")
    var dase: String,
    @SerializedName("UserName")
    var usuario: String,
    @SerializedName("Password")
    var senha: String
    )
