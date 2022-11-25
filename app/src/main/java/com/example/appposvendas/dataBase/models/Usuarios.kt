package com.example.appposvendas.dataBase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuarios(
    @ColumnInfo(name = "usuario") val usuario: String?,
    @ColumnInfo(name = "senha") val senha: String?,
    @ColumnInfo(name = "U_EASY_TOKENPAS") val tokenpas:String?,
    @ColumnInfo(name = "TechnicianCode")val TechnicianCode: String?,
){
    @PrimaryKey(autoGenerate = true)
    var uid : Int = 0
}
