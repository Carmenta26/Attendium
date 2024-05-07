package com.example.attendium.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento(
    val nombre: String,
    val fecha: String,
    val paquete: Paquete,
    val invitadosList: List<Invitado> = emptyList()
): Parcelable
