package com.example.appposvendas.response

import com.example.appposvendas.dataBase.models.Service
import com.google.gson.annotations.SerializedName

class ServicesValue {
    @SerializedName("value")
    val listService: List<Service> = mutableListOf()
}