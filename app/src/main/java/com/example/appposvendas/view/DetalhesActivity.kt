package com.example.appposvendas.view
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appposvendas.databinding.ActivityDetalhesBinding

class DetalhesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalhesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetalhesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)
        binding.imageViewPhone.setOnClickListener {
            val uri = Uri.parse("tel:${binding.textDddRecebido.text}${binding.textTelefoneRecebido.text}")
            val intent = Intent(Intent.ACTION_DIAL,uri)
            startActivity(intent)
        }
        binding.imageViewWhats.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=${binding.textDddRecebido.text}${binding.textTelefoneRecebido.text}"
            val intentWhats = Intent(Intent.ACTION_VIEW)
            intentWhats.setData(Uri.parse(url))
            startActivity(intentWhats)
        }

        recuperaDados()
    }

    fun recuperaDados(){
        val servico  = intent.getStringExtra("servico")
        val descricao  = intent.getStringExtra("descricao")
        val endereco  = intent.getStringExtra("endereco")
        val nome  = intent.getStringExtra("nome")
        val ddd = intent.getStringExtra("ddd")
        val telefone = intent.getStringExtra("telefone")
        val prioridade = intent.getStringExtra("prioridade")
        var status = intent.getStringExtra("status")

        binding.textTipoRecebido.text = servico
        binding.textDescricaoRecebido.text = descricao
        binding.textEnderecoRecebido.text = endereco
        binding.textNomeRecebido.text= nome
        binding.textDddRecebido.text = ddd
        binding.textTelefoneRecebido.text = telefone
        binding.textPrioridadeRecebido.text = prioridade
        when(status){
            "-3" -> "Aberto"
            "-2" -> "Pendente"
            "-1" -> "Fechado"
            "1" -> "cancelada"
        }
        binding.textStatusRecebido.text = status
    }
}