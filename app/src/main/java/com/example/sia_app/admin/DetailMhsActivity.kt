package com.example.sia_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sia_app.R

class DetailMhsActivity : AppCompatActivity() {

    private lateinit var inisialMhs: TextView
    private lateinit var nimMhs: TextView
    private lateinit var namaMhs: TextView
    private lateinit var emailMhs: TextView
    private lateinit var teleponMhs: TextView
    private lateinit var alamatMhs: TextView
    private lateinit var editMhs: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mhs)

        val id = intent.getStringExtra("id")
        val nim = intent.getStringExtra("nim")
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")
        val telepon = intent.getStringExtra("telepon")
        val alamat = intent.getStringExtra("alamat")

        inisialMhs = findViewById(R.id.inisialMhs)
        nimMhs = findViewById(R.id.nimMhs)
        namaMhs = findViewById(R.id.namaMhs)
        emailMhs = findViewById(R.id.emailMhs)
        teleponMhs = findViewById(R.id.teleponMhs)
        alamatMhs = findViewById(R.id.alamatMhs)
        editMhs = findViewById(R.id.editMhs)

        inisialMhs.text = nama?.substring(0,1)
        nimMhs.text = nim
        namaMhs.text = nama
        emailMhs.text = email
        teleponMhs.text = telepon
        alamatMhs.text = alamat

        editMhs.setOnClickListener {
            val intent = Intent(this, TambahMahasiswaActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("nim", nim)
            intent.putExtra("nama", nama)
            intent.putExtra("email", email)
            intent.putExtra("telepon", telepon)
            intent.putExtra("alamat", alamat)
            startActivity(intent)
            finish()
        }
    }
}