package com.example.attendium

import com.example.attendium.data.PagoEvento
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Pago : AppCompatActivity() {
    private var eventoId: String = "-NxLFHEXSZfxLIkxBQgJ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        findViewById<Button>(R.id.addButton).setOnClickListener {
            agregar()
        }
    }

    fun agregar() {
        val fecha = findViewById<EditText>(R.id.dateEditText).text.toString()
        val amount = findViewById<EditText>(R.id.amountEditText).text.toString()

        val database = Firebase.database
        val evento = database.getReference("/eventos/$eventoId")
        val pagos: MutableList<PagoEvento> = mutableListOf()

//        pagos.plus(com.example.attendium.data.PagoEvento(amount.toDouble(), fecha))
        pagos.add(PagoEvento(500.00, "1010"))
        pagos.add(PagoEvento(400.00, "1010"))
        evento.child("pagos").setValue(pagos)
    }
}