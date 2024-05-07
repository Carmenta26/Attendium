package com.example.attendium.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Invitado(
    val nombre: String,
    val telefono: String,
    val correo: String
): Parcelable