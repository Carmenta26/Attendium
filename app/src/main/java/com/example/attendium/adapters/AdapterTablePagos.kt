package com.example.attendium.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendium.R
import com.example.attendium.data.PagoEvento

class AdapterTablePagos(private var items: MutableList<PagoEvento>) :
    RecyclerView.Adapter<AdapterTablePagos.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.textViewDate)
        var amount: TextView = itemView.findViewById(R.id.textViewAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pago, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.date.text = item.fecha
        holder.amount.text = "$"+item.cantidad.toString()
    }

    override fun getItemCount() = items.size

    fun addItem() {
        notifyItemInserted(items.count() - 1)
    }
}