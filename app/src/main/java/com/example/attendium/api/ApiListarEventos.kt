package com.example.attendium.api

import com.example.attendium.data.EventoInfo
import com.google.firebase.database.DataSnapshot

class ApiListarEventos {
    fun get(snapshot: DataSnapshot ): MutableList<EventoInfo> {
        val eventos = mutableListOf<EventoInfo>()
        for (snapshot in snapshot.children) {
            val id = snapshot.key;

            if (snapshot.hasChild("invitados") && id != null) {
                val nombre = snapshot.child("nombre").value.toString()
                val fecha = snapshot.child("fecha").value.toString()
                val invitados = snapshot.child("invitados").childrenCount.toInt()
                val paquete = snapshot.child("paquete").child("titutlo")
                    .getValue(String::class.java).toString()
                val evento = EventoInfo(id, nombre, paquete, invitados, fecha, emptyList())
                eventos.add(evento)
            }
        }
        return eventos;
    }
}