package com.example.attendium.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Paquete(
    val titulo: String,
    val caracteristicas: List<String>,
    val precio: Int
) : Parcelable
