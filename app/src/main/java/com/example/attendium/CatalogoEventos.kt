package com.example.attendium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.attendium.configs.UserSession
import com.example.attendium.data.Evento
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CatalogoEventos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_eventos)

        println(UserSession.getIdUsuario())

        val btnIrADetalles = findViewById<Button>(R.id.agregarEventoButton)

        btnIrADetalles.setOnClickListener {
            listar()
//            val intent = Intent(this, CrearEvento::class.java)
//            startActivity(intent)
        }
    }

    fun listar() {
        val db = FirebaseDatabase.getInstance()
        val dbRef = db.getReference(Evento::class.java.simpleName)

        val eventos = ArrayList<Evento>()


        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val evento = snapshot.getValue(Evento::class.java)
                evento?.let {
                    eventos.add(it)
                    println("EALE")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })

//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("eventos")
//
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (snapshot in dataSnapshot.children) {
//                    val data = snapshot.getValue(Evento::class.java)
//                    println(data)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                println("Error: ${error.message}")
//            }
//        })
    }
}