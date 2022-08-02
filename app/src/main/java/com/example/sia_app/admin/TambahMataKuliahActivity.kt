package com.example.sia_app.admin

import android.app.Dialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.sia_app.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class TambahMataKuliahActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var kodeMk: EditText
    private lateinit var namaMk: EditText
    private lateinit var sksMk: EditText
    private lateinit var spinJenis: Spinner
    private lateinit var simpanMk: Button
    private lateinit var dialog : Dialog

    private val listJenisMk = arrayOf("Teori", "Praktik")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_mata_kuliah)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        val id = intent.getStringExtra("id")
        val kdmk = intent.getStringExtra("kdmk")
        val nama = intent.getStringExtra("nama")
        val sks = intent.getIntExtra("sks", 0)
        val jenis = intent.getStringExtra("jenis", )

        kodeMk = findViewById(R.id.kodeMk)
        namaMk = findViewById(R.id.namaMk)
        sksMk = findViewById(R.id.sksMk)
        spinJenis = findViewById(R.id.spinJenis)
        simpanMk = findViewById(R.id.simpanMk)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listJenisMk)
        spinJenis.adapter = adapter

        var position = 1
        if(jenis.equals("Teori")){
            position = 0
        }

        spinJenis.setSelection(position, true)

        if(id != null){
            kodeMk.setText(kdmk)
            namaMk.setText(nama)
            sksMk.setText(sks.toString())
            simpanMk.text = "Update Mata Kuliah"
        }

        simpanMk.setOnClickListener {

            try {
                val keyInput = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                keyInput.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                Log.e("Error", "Something went wrong!")
            }

            dialog = Dialog(this)
            dialog.setContentView(R.layout.loading_dialog)
            dialog.show()

            if(kodeMk.text.isNotEmpty() && namaMk.text.isNotEmpty() && sksMk.text.isNotEmpty()){
                if(id != null){
                    updateMk(id)
                } else {
                    checkMk()
                }
            } else {
                Toast.makeText(this, "Tidak boleh ada data yang kosong!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun checkMk(){

        kodeMk.clearFocus()
        namaMk.clearFocus()
        sksMk.clearFocus()

        db.collection("users").whereEqualTo("kdMk", kodeMk.text.toString()).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                saveMk()
            } else {
                dialog.dismiss()
                kodeMk.requestFocus()
                kodeMk.error = "Kode Mata Kuliah telah terdaftar sebelumnya"
                Toast.makeText(this, "Kode Mata Kuliah telah terdaftar sebelumnya!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveMk(){

        val dataDosen = hashMapOf<String, Any>(
            "kdMk" to kodeMk.text.toString(),
            "nama" to namaMk.text.toString(),
            "sks" to sksMk.text.toString().toInt(),
            "jenis" to spinJenis.selectedItem
        )

        db.collection("matakuliah").add(dataDosen).addOnSuccessListener {
            dialog.dismiss()
            onBackPressed()
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error adding document", e)
        }
    }

    private fun updateMk(id: String){
        db.collection("matakuliah").document(id).update(mapOf(
            "nama" to namaMk.text.toString(),
            "sks" to sksMk.text.toString().toInt(),
            "jenis" to spinJenis.selectedItem
        )).addOnCompleteListener {
            dialog.dismiss()
            onBackPressed()
        }
    }
}