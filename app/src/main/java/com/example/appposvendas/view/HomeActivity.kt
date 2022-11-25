package com.example.appposvendas.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TableLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appposvendas.R
import com.example.appposvendas.adapter.ServiceAdapter
import com.example.appposvendas.api.Endpoint
import com.example.appposvendas.dataBase.AppDatabase
import com.example.appposvendas.dataBase.daos.UserDao
import com.example.appposvendas.dataBase.models.Service
import com.example.appposvendas.databinding.ActivityHomeBinding
import com.example.appposvendas.preferences.Preference
import com.example.appposvendas.requests.ServicePatch
import com.example.appposvendas.response.ServicesValue
import com.example.appposvendas.retrofit.RetrofitService
import com.example.appposvendas.view.ui.TicketsEmAndamentoFragment
import com.example.appposvendas.view.ui.TicketsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.database = AppDatabase.getInstance(this)
        this.userDao = this.database.UserDao()
        //buscaService()
        configTableLayout()
    }

    fun configTableLayout(){
        val adapter = ViewPagaAdapter(this)
        binding.viewPage.adapter = adapter
        adapter.addFragmentes(TicketsFragment(),"Tickets")
        adapter.addFragmentes(TicketsEmAndamentoFragment(),"Tickets Em Andamento")

        binding.viewPage.offscreenPageLimit = adapter.itemCount

        val mediator = TabLayoutMediator(binding.table,binding.viewPage
        ){ tab: TabLayout.Tab, position: Int->
            tab.text = adapter.getTitle(position)
        }
        mediator.attach()
    }

    private fun getService() {
            val prefe = Preference(applicationContext)
            val token = prefe.setPrefe("Token")
            val tecnico = prefe.setPrefe("tecnico")
            val service = RetrofitService.buildService(Endpoint::class.java)

            service.getService(token.toString(),"TechnicianCode eq $tecnico and Status eq -3")
                .enqueue(object : Callback<ServicesValue> {
                override fun onResponse(call: Call<ServicesValue>, response: Response<ServicesValue>) {
                   if (response.isSuccessful) {
                        val serviceList = response.body()?.listService
                        CoroutineScope(Dispatchers.IO).launch {
                        val verificaService = userDao.buscaService()
                        if (serviceList != null) {
                            if (verificaService.size != serviceList.size) {
                                    for (i in serviceList) {
                                        val service = Service()
                                        service.ServiceCallID = i.ServiceCallID
                                        service.Subject = i.Subject
                                        service.CustomerCode = i.CustomerCode
                                        service.CustomerName = i.CustomerName
                                        service.ManufacturerSerialNum = i.ManufacturerSerialNum
                                        service.ItemCode = i.ItemCode
                                        service.ItemDescription = i.ItemDescription
                                        service.Status = i.Status
                                        service.PeriodIndicator = i.PeriodIndicator
                                        service.TechnicianCode = i.TechnicianCode
                                        service.BPPhone = i.BPPhone
                                        service.BPPhone2 = i.BPPhone2
                                        service.BPeMail = i.BPeMail
                                        service.BPShipToAddress = i.BPShipToAddress
                                        service.StartDate = i.StartDate
                                        userDao.insertService(service)
                                    }
                              //  buscaService()

                                }
                            }

                        }
                        Log.i("OK", "Login Realizado" + response.body()?.listService?.size)
                    }
                }
                override fun onFailure(call: Call<ServicesValue>, t: Throwable) {
                    Log.i("Erro", "Falha na busca" + t.message, t.cause)
                }
            })

    }
//    private fun buscaService(){
//        CoroutineScope(Dispatchers.IO).launch {
//            val listaServicos = userDao.buscaService()
//            withContext(Dispatchers.Main){
//                binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//                binding.recyclerView.setHasFixedSize(true)
//                binding.recyclerView.adapter = ServiceAdapter(listaServicos,this@HomeActivity)
//            }
//        }
//   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.menu_superior, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
          when(item.itemId){
            R.id.reflesh -> {
                getService()
                //patchService()
             }
         }
        return super.onOptionsItemSelected(item)
    }

    //FUNÇÃO PARA ATUALIZAR O STATUS
    private fun patchService(){
        val getToken = Preference(applicationContext)
        val token = getToken.setPrefe("Token")
        val service = ServicePatch(-3)
        CoroutineScope(Dispatchers.IO).launch {
            val id = userDao.buscaService()
            for(i in id){
                val servicePatch = RetrofitService.buildService(Endpoint::class.java)
                servicePatch.atualizaStatus(service,token.toString(),i.ServiceCallID!!).enqueue(object :Callback<JsonObject>{
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if(response.isSuccessful){
                            Log.i("OK", "Sucesso" + response.code())
                        }
                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.i("Erro", "Falha"  +t.message)
                        Log.i("Erro", "Falha"  +t.cause)
                    }
                })
            }
        }

    }



}