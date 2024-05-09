package com.example.attendium.api

import com.example.attendium.configs.UserSession
import com.example.attendium.data.EventoInfo
import com.example.attendium.data.PagoEvento
import com.google.firebase.database.DataSnapshot

class ApiListarEventos {
    fun get(snapshot: DataSnapshot): MutableList<EventoInfo> {
        val eventos = mutableListOf<EventoInfo>()
        val userId = UserSession.getIdUsuario()

        if (userId != null) {
            for (snapshot in snapshot.children) {
                val id = snapshot.key;

                if (snapshot.hasChild("invitados") && id != null) {
                    val eventoUserId = snapshot.child("userId").value.toString()
                    val nombre = snapshot.child("nombre").value.toString()
                    val fecha = snapshot.child("fecha").value.toString()
                    val invitados = snapshot.child("invitados").childrenCount.toInt()
                    val paquete = snapshot.child("paquete").child("titulo")
                        .getValue(String::class.java).toString()
                    val precioPersona =
                        snapshot.child("paquete").child("precio").value.toString().toDouble()

                    val pagos = mutableListOf<PagoEvento>()

                    if (snapshot.hasChild("pagos")) {
                        for (pago in snapshot.child("pagos").children) {
                            val cantidad: Double =
                                pago.child("cantidad").value.toString().toDouble()
                            val pagoFecha: String = pago.child("fecha").value.toString()
                            pagos.add(PagoEvento(cantidad, pagoFecha))
                        }
                    }

                    val evento =
                        EventoInfo(id, nombre, paquete, invitados, fecha, precioPersona, pagos)

                    if(eventoUserId == userId){
                        eventos.add(evento)
                    }

                }
            }
        }
        return eventos;
    }
}