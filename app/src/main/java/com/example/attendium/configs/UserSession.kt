package com.example.attendium.configs

object UserSession {
    private var idUsuario: String? = null

    fun setIdUsuario(id: String) {
        idUsuario = id
    }

    fun getIdUsuario(): String? {
        return idUsuario
    }
}