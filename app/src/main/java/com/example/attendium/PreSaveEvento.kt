package com.example.attendium

import ApiCrearEvento
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var textViewTitle: TextView
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
        textViewTitle = findViewById(R.id.titleEvento)

        // Recuperar el objeto Evento
        evento = intent.getParcelableExtra("evento")!!
        textViewTitle.setText("Evento - ${evento.nombre}")

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
        val invitadoView = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 8, 16, 8)
        }

        val textViewNombre = TextView(this).apply {
            text = invitado.nombre
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        val textViewTelefono = TextView(this).apply {
            text = invitado.telefono
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        val textViewCorreo = TextView(this).apply {
            text = invitado.correo
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }

        val buttonRemove = Button(this).apply {
            text = "-"
            textSize = 30f // Establece el tamaño del texto
            setTypeface(typeface, Typeface.BOLD) // Establece el texto en negrita
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            ) // Establece el color del texto a blanco

            // Configurar el fondo del botón a negro
            setBackgroundColor(ContextCompat.getColor(context, R.color.black))

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                // Aquí puedes añadir márgenes si es necesario
                setMargins(16, 16, 16, 16)
            }

            setOnClickListener {
                // Acción del botón, por ejemplo, remover una vista
                // Suponiendo que estas variables estén correctamente definidas y sean accesibles
                containerInvitados.removeView(invitadoView)
                listaInvitados.remove(invitado)
                actualizarInformacionEvento()  // Actualiza la información del evento tras eliminar un invitado
            }
        }


        // Añadir los TextViews y el botón al LinearLayout
        invitadoView.addView(textViewNombre)
        invitadoView.addView(textViewTelefono)
        invitadoView.addView(textViewCorreo)
        invitadoView.addView(buttonRemove)

        // Añadir el LinearLayout al contenedor principal
        containerInvitados.addView(invitadoView)
    }

    fun validarCorreo(correo: String): Boolean {
        val patronCorreo = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return patronCorreo.matches(correo)
    }


}


