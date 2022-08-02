package com.example.sia_app.admin

import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sia_app.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class TambahDosenActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var nikDosen: EditText
    private lateinit var namaDosen: EditText
    private lateinit var emailDosen: EditText
    private lateinit var teleponDosen: EditText
    private lateinit var alamatDosen: EditText
    private lateinit var simpanDosen: Button
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_dosen)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        val id = intent.getStringExtra("id")
        val nik = intent.getStringExtra("nik")
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")
        val telepon = intent.getStringExtra("telepon")
        val alamat = intent.getStringExtra("alamat")

        nikDosen = findViewById(R.id.nikDosen)
        namaDosen = findViewById(R.id.namaDosen)
        emailDosen = findViewById(R.id.emailDosen)
        teleponDosen = findViewById(R.id.teleponDosen)
        alamatDosen = findViewById(R.id.alamatDosen)
        simpanDosen = findViewById(R.id.simpanDosen)

        if(id != null){
            nikDosen.setText(nik)
            namaDosen.setText(nama)
            emailDosen.setText(email)
            teleponDosen.setText(telepon)
            alamatDosen.setText(alamat)
            emailDosen.isEnabled = false
            simpanDosen.text = "Update Dosen"
        }

        simpanDosen.setOnClickListener {

            try {
                val keyInput = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                keyInput.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                Log.e("Error", "Something went wrong!")
            }

            dialog = Dialog(this)
            dialog.setContentView(R.layout.loading_dialog)
            dialog.show()

            if(nikDosen.text.isNotEmpty() && namaDosen.text.isNotEmpty() && emailDosen.text.isNotEmpty() &&
                teleponDosen.text.isNotEmpty() && alamatDosen.text.isNotEmpty()){
                if(id != null){
                    updateDosen(id)
                } else {
                    checkUser()
                }
            } else {
                Toast.makeText(this, "Tidak boleh ada data yang kosong!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun checkUser(){

        nikDosen.clearFocus()
        namaDosen.clearFocus()
        emailDosen.clearFocus()
        teleponDosen.clearFocus()
        alamatDosen.clearFocus()

        db.collection("users").whereEqualTo("nik", nikDosen.text.toString()).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                db.collection("users").whereEqualTo("email", emailDosen.text.toString()).get().addOnSuccessListener { result ->
                    if(result.isEmpty){
                        saveDosen()
                    } else {
                        dialog.dismiss()
                        emailDosen.requestFocus()
                        emailDosen.error = "Email telah terdaftar sebelumnya"
                        Toast.makeText(this, "Email telah terdaftar sebelumnya!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                dialog.dismiss()
                nikDosen.requestFocus()
                nikDosen.error = "NIK telah terdaftar sebelumnya"
                Toast.makeText(this, "NIK telah terdaftar sebelumnya!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDosen(){

        val dataDosen = hashMapOf<String, Any>(
            "nik" to nikDosen.text.toString(),
            "nama" to namaDosen.text.toString(),
            "email" to emailDosen.text.toString(),
            "telepon" to teleponDosen.text.toString(),
            "alamat" to alamatDosen.text.toString(),
            "nikktp" to "-",
            "tempatlahir" to "-",
            "tgllahir" to FieldValue.serverTimestamp(),
            "goldarah" to "-",
            "updateprofil" to false,
            "role" to 20,
            "password" to nikDosen.text.toString(),
            "setupAuth" to false,
        )

        db.collection("users").add(dataDosen).addOnSuccessListener {
            dialog.dismiss()
            onBackPressed()
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
    }

    private fun updateDosen(id: String){
        db.collection("users").document(id).update(mapOf(
            "nik" to nikDosen.text.toString(),
            "nama" to namaDosen.text.toString(),
            "telepon" to teleponDosen.text.toString(),
            "alamat" to alamatDosen.text.toString(),
        )).addOnCompleteListener {
            dialog.dismiss()
            onBackPressed()
        }
    }
}