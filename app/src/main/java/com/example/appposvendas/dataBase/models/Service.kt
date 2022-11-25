package com.example.appposvendas.dataBase.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity
data class Service (
    @PrimaryKey(autoGenerate = true) var uid : Int,
    @SerializedName( "ServiceCallID") var ServiceCallID: Int?,
    @SerializedName( "Subject") var Subject: String?,
    @SerializedName( "CustomerCode") var CustomerCode: String?,
    @SerializedName( "CustomerName") var CustomerName: String?,
    @SerializedName( "ManufacturerSerialNum") var ManufacturerSerialNum: String?,
    @SerializedName( "ItemCode") var ItemCode: String?,
    @SerializedName( "ItemDescription") var ItemDescription: String?,
    @SerializedName( "Status") var Status: Int?,
    @SerializedName( "PeriodIndicator") var PeriodIndicator: String?,
    @SerializedName( "TechnicianCode") var TechnicianCode: Int?,
    @SerializedName( "BPPhone1") var BPPhone: String?,
    @SerializedName( "BPPhone2") var BPPhone2: String?,
    @SerializedName( "BPeMail") var BPeMail: String?,
    @SerializedName( "BPShipToAddress") var BPShipToAddress: String?,
    @SerializedName("StartDate") var StartDate: String?
        ){
    constructor(): this(0,0,"","","","","","",0,"",0,"","","","","")
    }
