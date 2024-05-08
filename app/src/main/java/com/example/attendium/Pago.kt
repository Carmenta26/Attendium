package com.example.attendium

import com.example.attendium.data.PagoEvento
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.attendium.data.EventoInfo
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Pago : AppCompatActivity() {
    private lateinit var evento: EventoInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        evento = intent.getParcelableExtra<EventoInfo>("evento_info")!!
        findViewById<Button>(R.id.addButton).setOnClickListener {
            agregar()
        }
        println(evento.pagos.toString())
    }

    fun agregar() {
        val fecha = findViewById<EditText>(R.id.dateEditText).text.toString()
        val amountVal = findViewById<EditText>(R.id.amountEditText).text.toString()


        if (fecha.isEmpty() || amountVal.isEmpty()) {
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
            evento.pagos.add(PagoEvento(amount, fecha))
            ref.child("pagos").setValue(evento.pagos)
            Toast.makeText(
                baseContext,
                "Pago al evento registrado exitosamente",
                Toast.LENGTH_SHORT
            ).show()
            println(evento.pagos.toString())
        } else {
            Toast.makeText(
                baseContext,
                "No puede pagar mas del salto total del evento",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun permitirAgregar(agregar: Double): Boolean {
        var pagado: Double = 0.0

        for (pago in evento.pagos) {
            pagado += pago.cantidad
        }

        return (pagado + agregar) <= (evento.costoPersona * evento.personas)
    }
}