package com.example.sia_app.model
import com.google.type.DateTime

data class JadwalModel(val id: String, val idMk: String, val idDosen: String, val hari: String, val jam: String, val ruangan: String, var kuota: Int, var tarif: Int, var check: Boolean) {
}