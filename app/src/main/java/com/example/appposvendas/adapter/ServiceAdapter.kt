package com.example.appposvendas.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appposvendas.R
import com.example.appposvendas.dataBase.models.Service
import com.example.appposvendas.view.DetalhesActivity
import java.text.SimpleDateFormat

class ServiceAdapter(
    private val myService: List<Service>,

): RecyclerView.Adapter<ServiceAdapter.MyViewHolder>(){
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.recyclerservice, parent, false)
            return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val service = myService[position]
        holder.bind(myService[position])
        holder.phone.setOnClickListener {
            val uri = Uri.parse("tel:${service.BPPhone2}${service.BPPhone}")
            val intent = Intent(Intent.ACTION_DIAL,uri)
            it.context.startActivity(intent)
        }
        holder.whats.setOnClickListener{
            val url = "https://api.whatsapp.com/send?phone=${service.BPPhone2}${service.BPPhone}"
            val intentWhats = Intent(Intent.ACTION_VIEW)
            intentWhats.setData(Uri.parse(url))
            it.context.startActivity(intentWhats)
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(context,DetalhesActivity::class.java)
            intent.putExtra("servico",service.Subject)
            intent.putExtra("descricao",service.ItemDescription)
            intent.putExtra("endereco",service.BPShipToAddress)
            intent.putExtra("nome",service.CustomerName)
            intent.putExtra("ddd",service.BPPhone2)
            intent.putExtra("telefone",service.BPPhone)
            intent.putExtra("prioridade",service.PeriodIndicator)
            intent.putExtra("status",service.Status)
            it.context.startActivity(intent)
        }

    }
    override fun getItemCount() = myService.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(services: Service) {
            with(services) {
                val parse = SimpleDateFormat("yyyy-MM-dd")
                val formater = SimpleDateFormat("dd-MM-yyyy")
                val data = formater.format(parse.parse(StartDate!!)!!)
                itemView.findViewById<TextView>(R.id.text_data).text = data
                itemView.findViewById<TextView>(R.id.text_data).setTextColor(Color.BLACK)
                itemView.findViewById<TextView>(R.id.text_tipo_servico).text = Subject
                itemView.findViewById<TextView>(R.id.text_tipo_servico).setTextColor(Color.BLACK)
                itemView.findViewById<TextView>(R.id.text_tipo_servico).setTypeface(Typeface.DEFAULT,BOLD)
                itemView.findViewById<TextView>(R.id.text_descricao).text = ItemDescription
                itemView.findViewById<TextView>(R.id.text_endereco).text = BPShipToAddress
                itemView.findViewById<TextView>(R.id.text_nome).text = CustomerName
                itemView.findViewById<TextView>(R.id.text_ddd).text = "($BPPhone2) "
                itemView.findViewById<TextView>(R.id.text_telefone).text = BPPhone
                if (PeriodIndicator == "Padrão") {
                    itemView.findViewById<TextView>(R.id.text_prioridade).text = PeriodIndicator
                    itemView.findViewById<TextView>(R.id.text_prioridade).setTextColor(Color.GREEN)
                }
                if (Status == -3) {
                    itemView.findViewById<TextView>(R.id.text_status).text = "Aberto"
                }
            }
        }
        val phone: ImageView = itemView.findViewById(R.id.phone)
        val whats: ImageView = itemView.findViewById(R.id.imag_whats)
    }

}


