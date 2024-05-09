package com.example.attendium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.attendium.adapters.AdapterPackage
import com.example.attendium.configs.UserSession
import com.example.attendium.data.Evento
import com.example.attendium.data.Paquete
import com.example.attendium.databinding.ActivityCrearEventoBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrearEvento : AppCompatActivity() {
    private lateinit var binding: ActivityCrearEventoBinding
    private var paqueteSeleccionado: Paquete? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializando el binding
        binding = ActivityCrearEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lista de paquetes
        val paquetes = listOf(
            Paquete(
                "Paquete Básico",
                listOf("Permiso de alcoholes", "Comida de 2 tiempos", "Decoración incluida"),
                100
            ), Paquete(
                "Paquete Premium", listOf(
                    "Permiso de alcoholes extendido", "Comida de 3 tiempos", "Decoración incluida"
                ), 200
            ), Paquete(
                "Paquete VIP", listOf(
                    "Barra libre", "Chef personal", "Seguridad privada", "Entretenimiento musical"
                ), 500
            )
            // Puedes añadir más paquetes aquí
        )


        val fechaActual = Date()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaActual)
        findViewById<EditText>(R.id.eventDate).setText(fechaFormateada)

        Log.d("CrearEvento", "Número de paquetes: ${paquetes.size}")
        paquetes.forEach { paquete ->
            Log.d(
                "CrearEvento",
                "Paquete: ${paquete.titulo}, Características: ${paquete.caracteristicas.joinToString()}, Precio: ${paquete.precio}"
            )
        }

        val adapter = AdapterPackage(this, paquetes) { selectedPackage ->
            paqueteSeleccionado = selectedPackage
        }
        binding.gridViewPaquetes.adapter = adapter

        binding.seleccionar.setOnClickListener {
            val nombreEvento = binding.eventName.text.toString()
            val fechaEvento = binding.eventDate.text.toString()
            paqueteSeleccionado?.let { paquete ->
                val userId = UserSession.getIdUsuario()
                if (userId != null) {
                    val evento = Evento(userId, nombreEvento, fechaEvento, paquete)
                    val intent = Intent(this@CrearEvento, PreSaveEvento::class.java)
                    intent.putExtra("evento", evento)
                    startActivity(intent)

                    println(evento)

                    // Aquí manejas el envío o procesamiento de los datos
                    Log.d(
                        "CrearEvento",
                        "Evento: $nombreEvento, Fecha: $fechaEvento, Paquete: ${paquete.titulo}"
                    )
                }

            } ?: Toast.makeText(this, "Por favor, seleccione un paquete", Toast.LENGTH_SHORT).show()
        }
    }
}