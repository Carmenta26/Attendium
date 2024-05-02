package com.example.attendium.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.attendium.data.Paquete
import com.example.attendium.R


class AdapterPackage(private val context: Context, private val paquetes: List<Paquete>, private val onPaqueteSelected: (Paquete) -> Unit) : BaseAdapter() {
    override fun getCount(): Int = paquetes.size

    override fun getItem(position: Int): Any = paquetes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        Log.d("AdapterPackage", "getView llamado para posición: $position")
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.paquete_evento, parent, false)


        val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
        val featureContainer = view.findViewById<LinearLayout>(R.id.featureContainer)
        val selectButton = view.findViewById<Button>(R.id.buttonSelect)

        val paquete = getItem(position) as Paquete
        titleTextView.text = paquete.titulo

        // Limpiar contenedor para evitar duplicación de vistas en reciclaje
        featureContainer.removeAllViews()

        // Inflar y agregar vistas para cada característica del paquete
        paquete.caracteristicas.forEach { feature ->
            val featureView = LayoutInflater.from(context).inflate(R.layout.feature_item_layout, featureContainer, false)
            val featureTextView = featureView.findViewById<TextView>(R.id.textViewFeature)
            featureTextView.text = feature
            featureContainer.addView(featureView)
        }

        selectButton.text = "Seleccionar - ${paquete.precio}"

        selectButton.setOnClickListener{

            val paquete = getItem(position) as Paquete
            onPaqueteSelected(paquete)
            Toast.makeText(context, "Seleccionado: ${paquete.titulo}", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}

