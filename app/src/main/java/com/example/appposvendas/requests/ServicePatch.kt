package com.example.appposvendas.requests

import com.google.gson.annotations.SerializedName

data class ServicePatch (
    @SerializedName("Status")
    val status: Int
        )