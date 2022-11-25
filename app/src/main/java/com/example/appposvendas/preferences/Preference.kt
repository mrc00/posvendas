package com.example.appposvendas.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.appposvendas.R

class Preference (context: Context){
    private val prefs : SharedPreferences = context.getSharedPreferences(context.getString(R.string.chaves),Context.MODE_PRIVATE)

    fun getPrefe(chave: String, valor: String){
        val editor = prefs.edit()
        editor.putString(chave,valor)
        editor.apply()
    }

    fun setPrefe(chave: String): String?{
        return prefs.getString(chave,null)
    }
}