package com.example.appposvendas.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appposvendas.adapter.ServiceAdapter
import com.example.appposvendas.dataBase.AppDatabase
import com.example.appposvendas.dataBase.daos.UserDao
import com.example.appposvendas.databinding.FragmentTicketsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("UNREACHABLE_CODE")
class TicketsFragment : Fragment() {
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!
    private lateinit var userDao: UserDao
    private lateinit var database: AppDatabase

    override fun onCreateView(
       // this.database = AppDatabase.getInstance(this)
       // this.userDao = this.database.UserDao()
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
        buscaService()
        this.database = AppDatabase.getInstance(requireContext().applicationContext)
        this.userDao = this.database.UserDao()
    }

    private fun buscaService(){
        CoroutineScope(Dispatchers.IO).launch {
            val listaServicos = userDao.buscaService()
            withContext(Dispatchers.Main){
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                binding.recyclerView.setHasFixedSize(true)
                binding.recyclerView.adapter = ServiceAdapter(listaServicos)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaService()
    }
}