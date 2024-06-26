package com.example.attendium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.attendium.adapters.EventoAdapter
import com.example.attendium.api.ApiListarEventos
import com.example.attendium.configs.UserSession
import com.example.attendium.data.EventoInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CatalogoEventos : AppCompatActivity() {
    lateinit var listEventos: List<EventoInfo>
    private lateinit var adapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_eventos)
        println(UserSession.getIdUsuario())




        supportActionBar?.setHomeAsUpIndicator(R.drawable.calendar_days) // Para asegurarse que el ícono es visible
        val listView = findViewById<ListView>(R.id.listview_eventos)
        val btnAgregar = findViewById<FloatingActionButton>(R.id.agregarEventoButton)


        btnAgregar.setOnClickListener {
            val intent = Intent(this, CrearEvento::class.java)
            startActivity(intent)
        }



        // PARA LISTAR EVENTOS
            val databaseReference = FirebaseDatabase.getInstance().getReference("eventos")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listEventos =  ApiListarEventos().get(dataSnapshot)
                    println(listEventos.size)

                    adapter = EventoAdapter(this@CatalogoEventos, listEventos)
                    listView.adapter = adapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        "FirebaseDB",
                        "Error al leer desde la base de datos",
                        databaseError.toException()
                    )
                }
            })
    }


}