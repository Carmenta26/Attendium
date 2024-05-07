package com.example.attendium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.attendium.data.Evento
import com.example.attendium.data.Invitado



class PreSaveEvento : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var containerInvitados: LinearLayout

    // Lista para almacenar los invitados
    private val listaInvitados = mutableListOf<Invitado>()
    private lateinit var evento: Evento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_save_evento)

        // Inicializamos las variables usando findViewById
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        editTextCorreo = findViewById(R.id.editTextCorreo)
        containerInvitados = findViewById(R.id.containerInvitados)




        // Recuperar el objeto Evento
        evento = intent.getParcelableExtra("evento")!!


        val buttonAgregar = findViewById<Button>(R.id.buttonAgregar)
        buttonAgregar.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val telefono = editTextTelefono.text.toString()
            val correo = editTextCorreo.text.toString()
            val eventoRecuperado = evento

            println(evento.nombre)
            if (nombre.isNotBlank() && telefono.isNotBlank() && correo.isNotBlank()) {
                // Crear un nuevo invitado y añadirlo a la lista
                val nuevoInvitado = Invitado(nombre, telefono, correo)
                listaInvitados.add(nuevoInvitado)
                agregarInvitadoAUI(nuevoInvitado)

                // Limpiar campos después de agregar
                editTextNombre.text.clear()
                editTextTelefono.text.clear()
                editTextCorreo.text.clear()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun agregarInvitadoAUI(invitado: Invitado) {
        val invitadoView = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val textViewNombre = TextView(this).apply {
            text = invitado.nombre
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        val textViewTelefono = TextView(this).apply {
            text = invitado.telefono
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        val textViewCorreo = TextView(this).apply {
            text = invitado.correo
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        // Añadir los TextViews al LinearLayout
        invitadoView.addView(textViewNombre)
        invitadoView.addView(textViewTelefono)
        invitadoView.addView(textViewCorreo)

        // Añadir el LinearLayout al contenedor principal
        containerInvitados.addView(invitadoView)
    }
}