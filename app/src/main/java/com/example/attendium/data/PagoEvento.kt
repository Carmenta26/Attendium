package com.example.attendium.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagoEvento(
    val cantidad: Double,
    val fecha: String
): Parcelable
