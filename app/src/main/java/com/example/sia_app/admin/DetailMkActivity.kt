package com.example.sia_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sia_app.R

class DetailMkActivity : AppCompatActivity() {

    private lateinit var inisialMk: TextView
    private lateinit var kodeMk: TextView
    private lateinit var namaMk: TextView
    private lateinit var sksMk: TextView
    private lateinit var jenisMk: TextView
    private lateinit var editMk: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mk)

        val id = intent.getStringExtra("id")
        val kdmk = intent.getStringExtra("kdmk")
        val nama = intent.getStringExtra("nama")
        val sks = intent.getIntExtra("sks", 0)
        val jenis = intent.getStringExtra("jenis", )

        inisialMk = findViewById(R.id.inisialMk)
        kodeMk = findViewById(R.id.kodeMk)
        namaMk = findViewById(R.id.namaMk)
        sksMk = findViewById(R.id.sksMk)
        jenisMk = findViewById(R.id.jenisMk)
        editMk = findViewById(R.id.editMk)

        inisialMk.text = nama?.substring(0,1)
        kodeMk.text = kdmk
        namaMk.text = nama
        sksMk.text = "$sks SKS"
        jenisMk.text = jenis?.uppercase()

        editMk.setOnClickListener {
            val intent = Intent(this, TambahMataKuliahActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("kdmk", kdmk)
            intent.putExtra("nama", nama)
            intent.putExtra("sks", sks)
            intent.putExtra("jenis", jenis)
            startActivity(intent)
            finish()
        }

    }
}