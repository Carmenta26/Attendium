package com.example.attendium

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import com.example.attendium.adapters.AdapterPackage
import com.example.attendium.configs.UserSession
import com.example.attendium.data.Evento
import com.example.attendium.data.Paquete
import com.example.attendium.databinding.ActivityCrearEventoBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CrearEvento : AppCompatActivity() {
    private lateinit var binding: ActivityCrearEventoBinding
    private var paqueteSeleccionado: Paquete? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var eventDateEditText: EditText
    private lateinit var calendar: Calendar


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
                     "Chef personal", "Seguridad privada", "Entretenimiento musical"
                ), 500
            ),
            Paquete(
                "Paquete VIP PLUS", listOf(
                    "Barra libre", "Comida de 3 tiempos", "Entretenimiento musical",
                ), 1000)
            // Puedes añadir más paquetes aquí
        )


        eventDateEditText = findViewById(R.id.eventDate)
        calendar = Calendar.getInstance()


        // Configurar el OnTouchListener para detectar el clic en el EditText
        eventDateEditText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePickerDialog()
            }
            true
        }

        // Obtener la fecha actual y mostrarla si el EditText no se ha pulsado
        val currentDate = Calendar.getInstance()
        if (eventDateEditText.text.isEmpty()) {
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentDate.time)
            eventDateEditText.setText(formattedDate)
        }

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


    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear un DatePickerDialog y mostrarlo
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // Mostrar la fecha seleccionada en el EditText
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                eventDateEditText.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

}