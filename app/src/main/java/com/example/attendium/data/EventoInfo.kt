package com.example.attendium.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventoInfo(
    val id: String,
    val nombre: String,
    val paquete: String,
    var personas: Int,
    var fecha: String,
    var costoPersona: Double,
    var pagos: MutableList<PagoEvento>
): Parcelable
