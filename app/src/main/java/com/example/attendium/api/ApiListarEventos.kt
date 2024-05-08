package com.example.attendium.api

import com.example.attendium.data.EventoInfo
import com.google.firebase.database.DataSnapshot

class ApiListarEventos {
    fun get(snapshot: DataSnapshot ) :List<EventoInfo> {
        val eventos = mutableListOf<EventoInfo>()
        for (snapshot in snapshot.children) {
            if (snapshot.hasChild("invitados")) {
                val nombre = snapshot.child("nombre").value.toString()
                val fecha = snapshot.child("fecha").value.toString()
                val invitados = snapshot.child("invitados").childrenCount.toInt()
                val paquete = snapshot.child("paquete").child("titutlo")
                    .getValue(String::class.java).toString()
                val evento = EventoInfo(nombre, paquete, invitados, fecha)
                eventos.add(evento)
            }
        }
        return eventos
    }
}