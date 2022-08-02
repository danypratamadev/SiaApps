package com.example.sia_app.dosen

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.sia_app.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class EditAccountDosenActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var nikDosen: EditText
    private lateinit var namaDosen: EditText
    private lateinit var emailDosen: EditText
    private lateinit var teleponDosen: EditText
    private lateinit var alamatDosen: EditText
    private lateinit var simpanProfil: Button
    private lateinit var nikKtpDosen: EditText
    private lateinit var tmpLahirDosen: EditText
    private lateinit var tglLahirLinear: LinearLayout
    private lateinit var tglLahirDosen: TextView
    private lateinit var darahDosen: EditText

    private var cal = Calendar.getInstance()
    private var idUser: String = "-"
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account_dosen)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        idUser = intent.getStringExtra("id").toString()
        val nik = intent.getStringExtra("nik")
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")
        val telepon = intent.getStringExtra("telepon")
        val alamat = intent.getStringExtra("alamat")
        val nikKtp = intent.getStringExtra("nikKtpUser")
        val tempatLahir = intent.getStringExtra("tempatLahirUser")
        val tglLahir = intent.getStringExtra("tglLahirUser")
        val golDarah = intent.getStringExtra("golDarahUser")
        val updateProfil = intent.getBooleanExtra("updateProfil", false)

        nikDosen = findViewById(R.id.nikDosen)
        namaDosen = findViewById(R.id.namaDosen)
        emailDosen = findViewById(R.id.emailDosen)
        teleponDosen = findViewById(R.id.teleponDosen)
        alamatDosen = findViewById(R.id.alamatDosen)
        nikKtpDosen = findViewById(R.id.nikKtpDosen)
        tmpLahirDosen = findViewById(R.id.tmpLahirDosen)
        tglLahirLinear = findViewById(R.id.tglLahirLinear)
        tglLahirDosen = findViewById(R.id.tglLahirDosen)
        darahDosen = findViewById(R.id.darahDosen)
        simpanProfil = findViewById(R.id.simpanProfil)

        nikDosen.setText(nik)
        namaDosen.setText(nama)
        emailDosen.setText(email)
        teleponDosen.setText(telepon)
        alamatDosen.setText(alamat)
        if(updateProfil) {
            nikKtpDosen.setText(nik)
            tmpLahirDosen.setText(tempatLahir)
            tglLahirDosen.text = tglLahir
            darahDosen.setText(golDarah)
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        tglLahirLinear.setOnClickListener {

            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        simpanProfil.setOnClickListener {

            if(namaDosen.text.isNotEmpty() && teleponDosen.text.isNotEmpty() && alamatDosen.text.isNotEmpty() && nikKtpDosen.text.isNotEmpty() && tmpLahirDosen.text.isNotEmpty() && tglLahirDosen.text != "DD/MM/YYYY" && darahDosen.text.isNotEmpty()){
                dialog = Dialog(this)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.show()

                updateDataDosen()
            } else {
                Toast.makeText(this, "Lengkapi data profil terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun updateDateInView() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        tglLahirDosen.text = dateFormat.format(cal.time)
        tglLahirDosen.setTextColor(resources.getColor(R.color.black))
    }

    private fun updateDataDosen() {

        val dataProfil = hashMapOf<String, Any>(
            "nama" to namaDosen.text.toString(),
            "telepon" to teleponDosen.text.toString(),
            "alamat" to alamatDosen.text.toString(),
            "nik" to nikKtpDosen.text.toString(),
            "tempatlahir" to tmpLahirDosen.text.toString(),
            "tgllahir" to cal.time,
            "goldarah" to darahDosen.text.toString(),
            "updateprofil" to true,
        )

        db.collection("users").document(idUser).update(dataProfil).addOnCompleteListener {

            editor.putString("namaUser", namaDosen.text.toString())
            editor.putString("teleponUser", teleponDosen.text.toString())
            editor.putString("alamatUser", alamatDosen.text.toString())
            editor.putString("nikKtpUser", nikKtpDosen.text.toString())
            editor.putString("tempatLahirUser", tmpLahirDosen.text.toString())
            editor.putString("tglLahirUser", tglLahirDosen.text.toString())
            editor.putString("golDarahUser", darahDosen.text.toString())
            editor.putBoolean("updateProfil", true)
            editor.apply()

            dialog.dismiss()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()

        }

    }

}