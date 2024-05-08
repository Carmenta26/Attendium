package com.example.attendium.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento(
    var userId: String,
    val nombre: String,
    val fecha: String,
    val paquete: Paquete,
    var invitados: List<Invitado> = emptyList()
): Parcelable
