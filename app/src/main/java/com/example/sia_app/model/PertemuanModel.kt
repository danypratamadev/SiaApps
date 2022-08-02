package com.example.sia_app.model

import java.util.*
import kotlin.collections.ArrayList

data class PertemuanModel(val id: String, val nama: String, var kodepresensi: Int, val durasipresensi: Int, val presensidibuat: Date, val beritaacara: String, val beritadibuat: Date, val list_mhs: ArrayList<String>, val list_nilaitugas: ArrayList<Int>) {
}