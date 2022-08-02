package com.example.sia_app.admin

import android.app.Dialog
import android.content.ContentValues
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

class TambahMahasiswaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var nimMhs: EditText
    private lateinit var namaMhs: EditText
    private lateinit var emailMhs: EditText
    private lateinit var teleponMhs: EditText
    private lateinit var alamatMhs: EditText
    private lateinit var simpanMhs: Button
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_mahasiswa)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        val id = intent.getStringExtra("id")
        val nim = intent.getStringExtra("nim")
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")
        val telepon = intent.getStringExtra("telepon")
        val alamat = intent.getStringExtra("alamat")

        nimMhs = findViewById(R.id.nimMhs)
        namaMhs = findViewById(R.id.namaMhs)
        emailMhs = findViewById(R.id.emailMhs)
        teleponMhs = findViewById(R.id.teleponMhs)
        alamatMhs = findViewById(R.id.alamatMhs)
        simpanMhs = findViewById(R.id.simpanMhs)

        if(id != null){
            nimMhs.setText(nim)
            namaMhs.setText(nama)
            emailMhs.setText(email)
            teleponMhs.setText(telepon)
            alamatMhs.setText(alamat)
            emailMhs.isEnabled = false
            simpanMhs.text = "Update Mahasiswa"
        }

        simpanMhs.setOnClickListener {

            try {
                val keyInput = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                keyInput.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                Log.e("Error", "Something went wrong!")
            }

            dialog = Dialog(this)
            dialog.setContentView(R.layout.loading_dialog)
            dialog.show()

            if(nimMhs.text.isNotEmpty() && namaMhs.text.isNotEmpty() && emailMhs.text.isNotEmpty() &&
                teleponMhs.text.isNotEmpty() && alamatMhs.text.isNotEmpty()){
                if(id != null){
                    updateMhs(id)
                } else {
                    checkMhs()
                }
            } else {
                Toast.makeText(this, "Tidak boleh ada data yang kosong!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun checkMhs(){

        nimMhs.clearFocus()
        namaMhs.clearFocus()
        emailMhs.clearFocus()
        teleponMhs.clearFocus()
        alamatMhs.clearFocus()

        db.collection("users").whereEqualTo("nim", nimMhs.text.toString()).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                db.collection("users").whereEqualTo("email", emailMhs.text.toString()).get().addOnSuccessListener { result ->
                    if(result.isEmpty){
                        saveMhs()
                    } else {
                        dialog.dismiss()
                        emailMhs.requestFocus()
                        emailMhs.error = "Email telah terdaftar sebelumnya"
                        Toast.makeText(this, "Email telah terdaftar sebelumnya!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                dialog.dismiss()
                nimMhs.requestFocus()
                nimMhs.error = "NIM telah terdaftar sebelumnya"
                Toast.makeText(this, "NIM telah terdaftar sebelumnya!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveMhs(){

        val dataDosen = hashMapOf<String, Any>(
            "nim" to nimMhs.text.toString(),
            "nama" to namaMhs.text.toString(),
            "email" to emailMhs.text.toString(),
            "telepon" to teleponMhs.text.toString(),
            "alamat" to alamatMhs.text.toString(),
            "nik" to "-",
            "tempatlahir" to "-",
            "tgllahir" to FieldValue.serverTimestamp(),
            "goldarah" to "-",
            "asalsekolah" to "-",
            "nikayah" to "-",
            "namaayah" to "-",
            "pekerjaanayah" to "-",
            "alamatayah" to "-",
            "nikibu" to "-",
            "namaibu" to "-",
            "pekerjaanibu" to "-",
            "alamatibu" to "-",
            "updateprofil" to false,
            "role" to 30,
            "password" to nimMhs.text.toString(),
            "setupAuth" to false,
        )

        db.collection("users").add(dataDosen).addOnSuccessListener {
            dialog.dismiss()
            onBackPressed()
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error adding document", e)
        }
    }

    private fun updateMhs(id: String){
        db.collection("users").document(id).update(mapOf(
            "nim" to nimMhs.text.toString(),
            "nama" to namaMhs.text.toString(),
            "telepon" to teleponMhs.text.toString(),
            "alamat" to alamatMhs.text.toString(),
        )).addOnCompleteListener {
            dialog.dismiss()
            onBackPressed()
        }
    }
}