package com.example.sia_app.model

data class MKModel(val id: String, val kdMk: String, val nama: String, val sksMk: Int, val jenis: String) {
    override fun toString(): String {
        return "$nama ($kdMk / $sksMk SKS)"
    }
}