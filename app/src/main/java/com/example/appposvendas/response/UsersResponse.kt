package com.example.appposvendas.response

import com.google.gson.annotations.SerializedName

data class UsersResponse (
    @SerializedName("InternalKey")
    var ID: Int,
    @SerializedName("UserCode")
    var userCode: String,
    @SerializedName("UserName")
    var nome: String,
    @SerializedName("eMail")
    var email: String,
    @SerializedName("MobilePhoneNumber")
    var celular: String,
    @SerializedName("U_EASY_TOKENPAS")
    var TokenPas: String,
    @SerializedName("U_EASY_BWUWB")
    var check: String
)