package com.example.attendium.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventoInfo(
    val nombre: String,
    val paquete: String,
    var personas: Int,
    var fecha: String,
): Parcelable
