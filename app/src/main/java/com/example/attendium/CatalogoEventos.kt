package com.example.attendium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.attendium.configs.UserSession

class CatalogoEventos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_eventos)

        println(UserSession.getIdUsuario())

        val btnIrADetalles = findViewById<Button>(R.id.agregarEventoButton)
        btnIrADetalles.setOnClickListener {
            val intent = Intent(this, CrearEvento::class.java)
            startActivity(intent)
        }
    }
}