package com.example.attendium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.attendium.api.ApiListarEventos
import com.example.attendium.configs.UserSession
import com.example.attendium.data.EventoInfo
import com.example.attendium.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signInAppCompatButton.setOnClickListener {
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()

            when {
                mEmail.isEmpty() || mPassword.isEmpty() -> {
                    Toast.makeText(
                        baseContext, "Correo o contraseña incorrecta.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    SignIn(mEmail, mPassword)
                }
            }
        }


        val createAccout = findViewById<AppCompatButton>(R.id.createAccount)

        createAccout.setOnClickListener {
            val intent = Intent(this, CrearCuenta::class.java)
            startActivity(intent)


        }
    }

    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = task.getResult().user?.uid

                    if (userId != null) {
                        UserSession.setIdUsuario(userId)
                    }
                    reaload()
                } else {
                    Toast.makeText(baseContext, "No se pudo iniciar sesión", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

    private fun reaload() {
        val intent = Intent(this, CatalogoEventos::class.java)
        startActivity(intent)
    }
}