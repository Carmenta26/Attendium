package com.example.attendium

import com.example.attendium.data.PagoEvento
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendium.adapters.AdapterTablePagos
import com.example.attendium.data.EventoInfo
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Pago : AppCompatActivity() {
    private lateinit var evento: EventoInfo
    private var total: Double = 0.00
    private var pendiente: Double = 0.00
    private var pagado: Double = 0.00
    private lateinit var fecha : EditText
    private lateinit var cantidad:EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterTablePagos
    private lateinit var pagosList: MutableList<PagoEvento>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        recyclerView = findViewById(R.id.recyclerViewPagos)
        fecha = findViewById(R.id.dateEditText)
        cantidad = findViewById(R.id.amountEditText)


        // Inicializar pagosList
        if (evento.pagos != null) {
            pagosList = evento.pagos
        } else {
            pagosList = mutableListOf()
            Log.e("PagoActivity", "No pagos")
        }
        adapter = AdapterTablePagos(pagosList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)




        findViewById<Button>(R.id.addButton).setOnClickListener {
            agregar()
        }
        total = evento.costoPersona * evento.personas
        calcular()
        println(evento.pagos.toString())
    }

    fun agregar() {
        val fechaText = fecha.text.toString()
        val amountVal = cantidad.text.toString()
        //pagosList.add(PagoEvento(amountVal.toDouble(),fechaText))
        val pagoEvento = PagoEvento(amountVal.toDouble(), fechaText)
        adapter.addItem(pagoEvento)
        calcular()

        fecha.text.clear()
        cantidad.text.clear()

        if (fechaText.isEmpty() || amountVal.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Debe completar todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val amount = amountVal.toDouble()

        if (permitirAgregar(amount)) {
            val database = Firebase.database
            val ref = database.getReference("/eventos/${evento.id}")
            evento.pagos.add(PagoEvento(amount, fechaText))
            ref.child("pagos").setValue(evento.pagos)
            Toast.makeText(
                baseContext,
                "Pago al evento registrado exitosamente",
                Toast.LENGTH_SHORT
            ).show()
            calcular()
            println(evento.pagos.toString())
            findViewById<EditText>(R.id.dateEditText).setText("")
            findViewById<EditText>(R.id.amountEditText).setText("")
        } else {
            Toast.makeText(
                baseContext,
                "No puede pagar mas del salto total del evento",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun permitirAgregar(agregar: Double): Boolean {
        return (pagado + agregar) <= total
    }

    fun calcular() {
        pagado = 0.00
        for (pago in evento.pagos) {
            pagado += pago.cantidad
        }
        pendiente = total - pagado
        imprimir()
    }

    fun imprimir() {
        println("TOTAL: " + total)
        println("PAGADO: " + pagado)
        println("PENDIENTE: " + pendiente)
    }
}