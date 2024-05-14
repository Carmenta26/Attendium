package com.example.attendium

import android.content.Intent
import com.example.attendium.data.PagoEvento
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendium.adapters.AdapterTablePagos
import com.example.attendium.data.EventoInfo
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Pago : AppCompatActivity() {
    private lateinit var evento: EventoInfo
    private var total: Double = 0.00
    private var pendiente: Double = 0.00
    private var pagado: Double = 0.00
    private lateinit var fecha: EditText
    private lateinit var totalTextview: TextView
    private lateinit var pagadoTextView: TextView
    private lateinit var restanteTextView: TextView
    private lateinit var titleEveto: Toolbar
    private lateinit var cantidad: EditText
    private lateinit var regresarButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterTablePagos
    private lateinit var pagosList: MutableList<PagoEvento>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        totalTextview = findViewById(R.id.totalTextView)
        pagadoTextView = findViewById(R.id.paidTextView)
        restanteTextView = findViewById(R.id.remainingTextView)
        recyclerView = findViewById(R.id.recyclerViewPagos)
        fecha = findViewById(R.id.dateEditText)
        cantidad = findViewById(R.id.amountEditText)
        regresarButton = findViewById(R.id.regresar)
        titleEveto = findViewById<androidx.appcompat.widget.Toolbar>(R.id.eventNameTextView)
        regresarButton.setOnClickListener {
            val intent = Intent(this, CatalogoEventos::class.java)
            startActivity(intent)
        }

        evento = intent.getParcelableExtra("evento_info")!!
        titleEveto.title = "Evento - " +evento.nombre

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

        totalTextview.setText(total.toString())
        pagadoTextView.setText(pagado.toString())
        restanteTextView.setText(pendiente.toString())

        val fechaActual = Date()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaActual)
        findViewById<EditText>(R.id.dateEditText).setText(fechaFormateada)

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

        if (fechaText.isEmpty() || amountVal.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Debe completar todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val pagoEvento = PagoEvento(amountVal.toDouble(), fechaText)
        calcular()

        val amount = amountVal.toDouble()

        if (permitirAgregar(amount)) {
            val database = Firebase.database
            val ref = database.getReference("/eventos/${evento.id}")
            evento.pagos.add(PagoEvento(amount, fechaText))
            ref.child("pagos").setValue(evento.pagos)
            adapter.addItem()
            calcular()
            Toast.makeText(
                baseContext,
                "Pago al evento registrado exitosamente",
                Toast.LENGTH_SHORT
            ).show()
            findViewById<EditText>(R.id.amountEditText).setText("")
        } else {
            Toast.makeText(
                baseContext,
                "No puede pagar mas del saldo total del evento",
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
        totalTextview.setText("$"+total.toString())
        pagadoTextView.setText("$"+pagado.toString())
        restanteTextView.setText("$"+pendiente.toString())
        imprimir()
    }

    fun imprimir() {
        println("TOTAL: $$total")
        println("PAGADO: $$pagado")
        println("PENDIENTE: $$pendiente")
    }
}