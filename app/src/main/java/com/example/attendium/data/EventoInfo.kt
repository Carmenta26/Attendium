package com.example.attendium.data

data class EventoInfo(
    val id: String,
    val nombre: String,
    val paquete: String,
    var personas: Int,
    var fecha: String,
    var pagos: List<PagoEvento>
)
