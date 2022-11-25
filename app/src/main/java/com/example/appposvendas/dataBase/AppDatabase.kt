package com.example.appposvendas.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appposvendas.dataBase.daos.UserDao
import com.example.appposvendas.dataBase.models.Service
import com.example.appposvendas.dataBase.models.Usuarios


@Database(entities = [Usuarios::class,Service::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object{
        private const val DATABASE_NAME : String = "APP_POS_VENDAS"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }
        private fun buildDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
    }

}