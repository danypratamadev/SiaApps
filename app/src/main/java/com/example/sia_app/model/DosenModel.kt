package com.example.sia_app.model

data class DosenModel(val id: String, val nikDosen: String, val namaDosen: String, val emailDosen: String, val teleponDosen: String, val alamatDosen: String) {
    override fun toString(): String {
        return namaDosen
    }
}