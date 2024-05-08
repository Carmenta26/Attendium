package com.example.attendium.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.attendium.Pago
import com.example.attendium.R
import com.example.attendium.data.EventoInfo

class EventoAdapter(private val context: Context, private val eventos: List<EventoInfo>) : BaseAdapter() {

    override fun getCount(): Int = eventos.size

    override fun getItem(position: Int): Any = eventos[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.evento_item, parent, false)
        val evento = eventos[position]

        view.findViewById<TextView>(R.id.nombreEvento1).text = evento.nombre
        view.findViewById<TextView>(R.id.paquete).text = evento.paquete
        view.findViewById<TextView>(R.id.fecha).text = evento.fecha
        view.findViewById<TextView>(R.id.invitados).text = "Invitados: ${evento.personas}"


        view.findViewById<Button>(R.id.botonEvento1).setOnClickListener {
            val intent = Intent(context, Pago::class.java)
            intent.putExtra("evento_info", evento)  // 'evento' es el objeto EventoInfo actual
            context.startActivity(intent)
        }

        return view
    }
}
