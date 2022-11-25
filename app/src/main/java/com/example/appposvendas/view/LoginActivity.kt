package com.example.appposvendas.view


import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import com.example.appposvendas.R
import com.example.appposvendas.api.Endpoint
import com.example.appposvendas.api.NetworkCheck
import com.example.appposvendas.dataBase.AppDatabase
import com.example.appposvendas.dataBase.daos.UserDao
import com.example.appposvendas.databinding.ActivityLoginBinding
import com.example.appposvendas.preferences.Preference
import com.example.appposvendas.requests.LoginRequest
import com.example.appposvendas.response.LoginResponse
import com.example.appposvendas.response.UserValue
import com.example.appposvendas.retrofit.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        this.database = AppDatabase.getInstance(this)
        this.userDao = this.database.UserDao()

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onStart() {
        super.onStart()
        //METODO BOTÃO DE LOGIN
        this.binding.btnLogin.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {

            val result = validausuario(
                binding.textEditUsuario.text.toString(),
                binding.textEditSenha.text.toString()
            )
                if(result && validacao()){
                    home()
                }else{
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,"Usuario Não Encontrado",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        this.binding.textCadastro.setOnClickListener{
            val cadastro = Intent(this,CadastroActivity::class.java)
            startActivity(cadastro)
        }
    }
    private val networkCheck by lazy {
        NetworkCheck(getSystemService(ConnectivityManager::class.java))
    }
    //VALIDA SE O TOKEN SALVO SAP
    fun validacao():Boolean{
        var resultado = false
        val pass = Preference(applicationContext)
        val TokenPas = pass.setPrefe("TokenPas")
        val usuario = this.userDao.validaUser(binding.textEditUsuario.text.toString())
        for(i in usuario){
            if (TokenPas != null) {
                if(TokenPas == i.tokenpas)
                    resultado = true
            }else{
                resultado = false
            }
        }
        return resultado
    }
//VALIDA USUARIO E SENHA
    private suspend fun validausuario(usuario: String, senha: String): Boolean
    {
        if(usuario.isEmpty() || usuario.isEmpty())
            return false
        if(senha.isEmpty() || senha.isBlank())
            return false
        val senhaconvertida = converteSenha(senha)
        if(this.userDao.getUsuario(usuario,senhaconvertida).isEmpty()){
            return false
        }else {
            val prefe = Preference(applicationContext)
            prefe.getPrefe("usuario",binding.textEditUsuario.text.toString())
            prefe.getPrefe("senha",binding.textEditSenha.text.toString())
            buscaTecnico()
            val base = getString(R.string.base)
            requestLogin(
                base,
                binding.textEditUsuario.text.toString(),
                binding.textEditSenha.text.toString()
            )
        }
        return true
    }
    private fun buscaTecnico(){

        CoroutineScope(Dispatchers.IO).launch {
            val prefe = Preference(applicationContext)
            val user: String? = prefe.setPrefe("usuario")
            val tec = userDao.getTecnico(Integer.parseInt(user))
            for(i in tec){
                prefe.getPrefe("tecnico", i.TechnicianCode.toString())
            }
        }

    }

    private fun home(){
        val home = Intent(this,HomeActivity::class.java)
        startActivity(home)
    }
    //CRIPTOFRAFA A SENHA
    private fun converteSenha(senha: String): String {

        val codifica = MessageDigest.getInstance("MD5")
        val array: ByteArray = codifica.digest(senha.toByteArray())
        val senhaEmMd5 = StringBuffer()
        for(element in array){
            senhaEmMd5.append(Integer.toHexString((element.toInt() and 0xFF) or 0x100).substring(1,3))
        }
        return senhaEmMd5.toString()
    }

    //ENVIA REQUISIÇÃO DE LOGIM PARA API
    private fun requestLogin(base: String, login: String,senha: String){
        val userLogin = LoginRequest(
            base,
            login,
            senha
        )
        networkCheck.performActionConnectec {
            val service = RetrofitService.buildService(Endpoint::class.java)
            service.login(userLogin).enqueue(object: Callback<LoginResponse> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val getToken = Preference(applicationContext)
                    if(response.isSuccessful){
                        //SE O LOGIN FOR REALIZADO SALVA O TOKEN DA SESSION
                        Log.i("ok","login realizado"+ response.body())
                        getToken.getPrefe("Token",response.headers().get("Set-Cookie").toString())
                        CoroutineScope(Dispatchers.Main).launch {
                            getuser()
                        }
                    }else{
                        Log.i("ok","Falha no Login"+ response.message())
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.i("ok","Falha no Login"+ t.message, t.cause)
                }
            }
            )
        }
        }

        //FUNÇÃO QUE FAZ O GET DO USUÁRIO
    @SuppressLint("SuspiciousIndentation")
    private fun getuser(){
        val gettoken = Preference(applicationContext)
        val token = gettoken.setPrefe("Token")
        val usuario = gettoken.setPrefe("usuario")
        val user = RetrofitService.buildService(Endpoint::class.java)
            user.getUsers(token.toString(),"UserCode eq '$usuario'").enqueue(object : Callback<UserValue> {
            @Suppress("SENSELESS_COMPARISON")
            override fun onResponse(call: Call<UserValue>, response: Response<UserValue>) {
                if(response.isSuccessful){
                    Log.i("Ok","Usuario"+response.body()?.user)
                }
                //SALVA O TOKEN QUE ESTA NO SAP E VERIFICA SE É USUARIO WEB/APP
                if(response.body()?.user?.get(0)?.TokenPas != null && response.body()?.user?.get(0)?.check.equals("S")){
                    val prefe = Preference(applicationContext)
                    prefe.getPrefe("TokenPas",response.body()?.user?.get(0)?.TokenPas.toString())
                    Log.i("Ok","Usuario validado")
                }else{
                    gettoken.getPrefe("TokenPas","")
                    Log.i("Ok","Usuario não validado"  +response.code())
                }
            }
            override fun onFailure(call: Call<UserValue>, t: Throwable) {
                Log.i("ok","Falha no Get"+ t.message, t.cause)
            }

        })

    }
    }




