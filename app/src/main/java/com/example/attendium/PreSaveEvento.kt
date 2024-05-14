package com.example.attendium

import ApiCrearEvento
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar


import androidx.core.content.ContextCompat
import com.example.attendium.data.Evento
import com.example.attendium.data.Invitado
import kotlin.properties.Delegates



class PreSaveEvento : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var containerInvitados: LinearLayout
    private lateinit var crearEventoButton: Button

    // Lista para almacenar los invitados
    private val listaInvitados = mutableListOf<Invitado>()
    private lateinit var evento: Evento
    private var precioFinal by Delegates.notNull<Int>()
    private lateinit var textViewNumeroPersonas: TextView
    private lateinit var textViewPrecioTotal: TextView
    private lateinit var textViewTitle: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_save_evento)

        // Inicializamos las variables usando findViewById
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        editTextCorreo = findViewById(R.id.editTextCorreo)
        containerInvitados = findViewById(R.id.containerInvitados)
        crearEventoButton = findViewById(R.id.crearEvento)
        textViewNumeroPersonas = findViewById(R.id.textViewNumeroPersonas)
        textViewPrecioTotal = findViewById(R.id.textViewPrecioTotal)
        textViewTitle = findViewById<androidx.appcompat.widget.Toolbar>(R.id.titleEvento)

        // Recuperar el objeto Evento
        evento = intent.getParcelableExtra("evento")!!
        textViewTitle.title = "Evento - ${evento.nombre}"

        val buttonAgregar = findViewById<Button>(R.id.buttonAgregar)
        buttonAgregar.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val telefono = editTextTelefono.text.toString()
            val correo = editTextCorreo.text.toString()
            val eventoRecuperado = evento

            println(evento.nombre)
            if (nombre.isNotBlank() && telefono.isNotBlank() && correo.isNotBlank()) {
                // Crear un nuevo invitado y añadirlo a la lista

                if(validarCorreo(correo)){
                    val nuevoInvitado = Invitado(nombre, telefono, correo)
                    listaInvitados.add(nuevoInvitado)
                    agregarInvitadoAUI(nuevoInvitado)

                    actualizarInformacionEvento()

                    precioFinal = listaInvitados.size * eventoRecuperado.paquete.precio
                    println("Este es el nuevo precio del evento " + precioFinal)

                    // Limpiar campos después de agregar
                    editTextNombre.text.clear()
                    editTextTelefono.text.clear()
                    editTextCorreo.text.clear()
                } else {
                    Toast.makeText(this, "El formato del correo no es correcto.", Toast.LENGTH_SHORT)
                        .show()
                }


            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        crearEventoButton.setOnClickListener {
            crearEvento()
        }
    }

    fun crearEvento() {
        if (listaInvitados.isEmpty()) {
            Toast.makeText(this, "Por favor, añada al menos un invitado.", Toast.LENGTH_LONG).show()
        } else {
            evento.invitados = listaInvitados
            val intent = Intent(this, CatalogoEventos::class.java)
            startActivity(intent)
            val api = ApiCrearEvento()
            api.crear(evento)
        }
    }

    private fun actualizarInformacionEvento() {
        val numeroPersonas = listaInvitados.size
        val precioTotal = numeroPersonas * evento.paquete.precio

        textViewNumeroPersonas.text = "$numeroPersonas personas"
        textViewPrecioTotal.text = " $${precioTotal} MXN"
    }

    private fun agregarInvitadoAUI(invitado: Invitado) {
        // Inflar el layout desde XML
        val inflater = LayoutInflater.from(this)
        val invitadoView = inflater.inflate(R.layout.item_invitado, containerInvitados, false)

        // Configurar los TextViews con la información del invitado
        val textViewNombre: TextView = invitadoView.findViewById(R.id.textViewNombre)
        textViewNombre.text = invitado.nombre

        val textViewTelefono: TextView = invitadoView.findViewById(R.id.textViewTelefono)
        textViewTelefono.text = invitado.telefono

        val textViewCorreo: TextView = invitadoView.findViewById(R.id.textViewCorreo)
        textViewCorreo.text = invitado.correo

        // Configurar el botón para eliminar el invitado de la UI
        val buttonEliminar: ImageButton = invitadoView.findViewById(R.id.buttonEliminar)
        buttonEliminar.setOnClickListener {
            containerInvitados.removeView(invitadoView)  // Eliminar la vista del layout
            listaInvitados.remove(invitado)  // Suponiendo que tengas una lista de invitados
            actualizarInformacionEvento()  // Actualizar la información necesaria
        }

        // Añadir la vista inflada al contenedor en la UI
        containerInvitados.addView(invitadoView)
    }




    fun validarCorreo(correo: String): Boolean {
        val patronCorreo = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return patronCorreo.matches(correo)
    }


}


