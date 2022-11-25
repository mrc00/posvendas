package com.example.appposvendas.dataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appposvendas.dataBase.models.Service
import com.example.appposvendas.dataBase.models.Usuarios
import com.example.appposvendas.response.UserValue

@Dao
interface UserDao {
    @Insert
    suspend fun insert(usuarios: Usuarios)

    @Query("select * from usuarios where usuario = :u_user and senha = :l_senha")
    suspend fun getUsuario(u_user: String, l_senha: String):List<Usuarios>

    @Query("select * from usuarios where usuario = :code")
    fun validaUser(code: String):List<Usuarios>

    @Insert
    fun insertService(service: Service)

    @Query("select * from service")
    suspend fun buscaService():List<Service>

    @Query("delete from service")
    fun deleteService()

    @Query("select * from usuarios where usuario = :user")
    suspend fun getTecnico(user:Int):List<Usuarios>
}