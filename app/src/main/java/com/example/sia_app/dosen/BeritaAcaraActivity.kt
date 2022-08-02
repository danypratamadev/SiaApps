package com.example.sia_app.dosen

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sia_app.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.app.Activity

import android.content.Intent




class BeritaAcaraActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var isiBerita: EditText
    private lateinit var simpanBerita: Button

    private var idJadwal: String = "-"
    private var idPertemuan: String ="-"

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berita_acara)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        idJadwal = intent.getStringExtra("idJadwal").toString()
        idPertemuan = intent.getStringExtra("idPertemuan").toString()

        isiBerita = findViewById(R.id.isiBerita)
        simpanBerita = findViewById(R.id.simpanBerita)

        simpanBerita.setOnClickListener {
            if(isiBerita.text.isNotEmpty()){
                saveBeritaAcara()
            } else {
                Toast.makeText(this, "Silakan isi berita acara terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveBeritaAcara(){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataBerita = hashMapOf<String, Any>(
            "beritaacara" to isiBerita.text.toString(),
            "beritadibuat" to FieldValue.serverTimestamp(),
        )

        db.collection("jadwal").document(idJadwal).collection("pertemuan").document(idPertemuan).update(dataBerita).addOnCompleteListener {
            dialog.dismiss()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

    }

}