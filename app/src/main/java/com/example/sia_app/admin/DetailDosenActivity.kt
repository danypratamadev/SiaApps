package com.example.sia_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sia_app.R
import com.example.sia_app.model.DosenModel

class DetailDosenActivity : AppCompatActivity() {

    private lateinit var inisialDosen: TextView
    private lateinit var nikDosen: TextView
    private lateinit var namaDosen: TextView
    private lateinit var emailDosen: TextView
    private lateinit var teleponDosen: TextView
    private lateinit var alamatDosen: TextView
    private lateinit var editDosen: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dosen)

        val id = intent.getStringExtra("id")
        val nik = intent.getStringExtra("nik")
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")
        val telepon = intent.getStringExtra("telepon")
        val alamat = intent.getStringExtra("alamat")

        inisialDosen = findViewById(R.id.inisialDosen)
        nikDosen = findViewById(R.id.nikDosen)
        namaDosen = findViewById(R.id.namaDosen)
        emailDosen = findViewById(R.id.emailDosen)
        teleponDosen = findViewById(R.id.teleponDosen)
        alamatDosen = findViewById(R.id.alamatDosen)
        editDosen = findViewById(R.id.editDosen)

        inisialDosen.text = nama?.substring(0,1)
        nikDosen.text = nik
        namaDosen.text = nama
        emailDosen.text = email
        teleponDosen.text = telepon
        alamatDosen.text = alamat

        editDosen.setOnClickListener {
            val intent = Intent(this, TambahDosenActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("nik", nik)
            intent.putExtra("nama", nama)
            intent.putExtra("email", email)
            intent.putExtra("telepon", telepon)
            intent.putExtra("alamat", alamat)
            startActivity(intent)
            finish()
        }

    }
}