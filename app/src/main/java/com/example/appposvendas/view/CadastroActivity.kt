package com.example.appposvendas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appposvendas.dataBase.AppDatabase
import com.example.appposvendas.dataBase.daos.UserDao
import com.example.appposvendas.dataBase.models.Usuarios
import com.example.appposvendas.databinding.ActivityCadastroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCadastroBinding
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        this.database = AppDatabase.getInstance(this)
        this.userDao = this.database.UserDao()
    }

    override fun onStart() {
        super.onStart()
        this.binding.btnCadastro.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val result = saveUser(
                    binding.editTextTextPersonName.text.toString(),
                    binding.editTextTextPassword.text.toString(),
                    binding.editTextToken.text.toString(),
                    binding.editTextIdInterno.text.toString()
                )
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@CadastroActivity,
                        if(result)"Usúario Salvo" else "Erro ao salvar",
                        Toast.LENGTH_LONG
                    ).show()
                    if(result)
                        login()
                }
            }
        }

    }
    private suspend fun saveUser(usuario: String , senha: String,token: String,id_interno: String): Boolean{

        if(usuario.isBlank() || usuario.isEmpty())
            return false

        if(senha.isBlank() || senha.isEmpty())
            return  false
        val senhaConverifa = converteSenha(senha)

       var validaTecnico = this.userDao.getTecnico(Integer.parseInt(usuario))
        for(i in validaTecnico){
            if(i.usuario.equals(usuario)){
                return false
            }else{
                this.userDao.insert(Usuarios(usuario,senhaConverifa,token,id_interno) )
            }
        }
        return true
    }
    private fun login(){
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

    private fun converteSenha(senha: String): String {

        val codifica = MessageDigest.getInstance("MD5")
        val array: ByteArray = codifica.digest(senha.toByteArray())
        val senhaEmMd5 = StringBuffer()
        for(element in array){
            senhaEmMd5.append(Integer.toHexString((element.toInt() and 0xFF) or 0x100).substring(1,3))
        }
        return senhaEmMd5.toString()
    }

}


