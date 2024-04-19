package com.example.attendium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signInButton = findViewById<AppCompatButton>(R.id.signInAppCompatButton)

        signInButton.setOnClickListener {
            // Acción al hacer clic en el botón
            val intent = Intent(this, CatalogoEventos::class.java)
            startActivity(intent)
        }
    }
}