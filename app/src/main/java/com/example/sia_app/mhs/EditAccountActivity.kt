package com.example.sia_app.mhs

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

class EditAccountActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var nimMhs: EditText
    private lateinit var namaMhs: EditText
    private lateinit var emailMhs: EditText
    private lateinit var teleponMhs: EditText
    private lateinit var alamatMhs: EditText
    private lateinit var nikMhs: EditText
    private lateinit var tmpLahirMhs: EditText
    private lateinit var tglLahirLinear: LinearLayout
    private lateinit var tglLahirMhs: TextView
    private lateinit var darahMhs: EditText
    private lateinit var sekolahMhs: EditText
    private lateinit var nikAyahTv: EditText
    private lateinit var namaAyahTv: EditText
    private lateinit var pekerjaanAyahTv: EditText
    private lateinit var alamatAyahTv: EditText
    private lateinit var nikIbuTv: EditText
    private lateinit var namaIbuTv: EditText
    private lateinit var pekerjaanIbuTv: EditText
    private lateinit var alamatIbuTv: EditText
    private lateinit var simpanProfil: Button

    private var cal = Calendar.getInstance()
    private var idUser: String = "-"
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        idUser = intent.getStringExtra("id").toString()
        val nim = intent.getStringExtra("nim")
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")
        val telepon = intent.getStringExtra("telepon")
        val alamat = intent.getStringExtra("alamat")
        val nik = intent.getStringExtra("nikKtpUser")
        val tempatLahir = intent.getStringExtra("tempatLahirUser")
        val tglLahir = intent.getStringExtra("tglLahirUser")
        val golDarah = intent.getStringExtra("golDarahUser")
        val asalSekolah = intent.getStringExtra("sekolahUser")
        val nikAyah = intent.getStringExtra("nikAyah")
        val namaAyah = intent.getStringExtra("namaAyah")
        val pekerjaanAyah = intent.getStringExtra("pekerjaanAyah")
        val alamatAyah = intent.getStringExtra("alamatAyah")
        val nikIbu = intent.getStringExtra("nikIbu")
        val namaIbu = intent.getStringExtra("namaIbu")
        val pekerjaanIbu = intent.getStringExtra("pekerjaanIbu")
        val alamatIbu = intent.getStringExtra("alamatIbu")
        val updateProfil = intent.getBooleanExtra("updateProfil", false)

        nimMhs = findViewById(R.id.nimMhs)
        namaMhs = findViewById(R.id.namaMhs)
        emailMhs = findViewById(R.id.emailMhs)
        teleponMhs = findViewById(R.id.teleponMhs)
        alamatMhs = findViewById(R.id.alamatMhs)
        simpanProfil = findViewById(R.id.simpanProfil)
        nikMhs = findViewById(R.id.nikMhs)
        tmpLahirMhs = findViewById(R.id.tmpLahirMhs)
        tglLahirLinear = findViewById(R.id.tglLahirLinear)
        tglLahirMhs = findViewById(R.id.tglLahirMhs)
        darahMhs = findViewById(R.id.darahMhs)
        sekolahMhs = findViewById(R.id.sekolahMhs)
        nikAyahTv = findViewById(R.id.nikAyah)
        namaAyahTv = findViewById(R.id.namaAyah)
        pekerjaanAyahTv = findViewById(R.id.pekerjaanAyah)
        alamatAyahTv = findViewById(R.id.alamatAyah)
        nikIbuTv = findViewById(R.id.nikIbu)
        namaIbuTv = findViewById(R.id.namaIbu)
        pekerjaanIbuTv = findViewById(R.id.pekerjaanIbu)
        alamatIbuTv = findViewById(R.id.alamatIbu)

        nimMhs.setText(nim)
        namaMhs.setText(nama)
        emailMhs.setText(email)
        teleponMhs.setText(telepon)
        alamatMhs.setText(alamat)
        if(updateProfil){
            nikMhs.setText(nik)
            tmpLahirMhs.setText(tempatLahir)
            tglLahirMhs.text = tglLahir
            darahMhs.setText(golDarah)
            sekolahMhs.setText(asalSekolah)
            nikAyahTv.setText(nikAyah)
            namaAyahTv.setText(namaAyah)
            pekerjaanAyahTv.setText(pekerjaanAyah)
            alamatAyahTv.setText(alamatAyah)
            nikIbuTv.setText(nikIbu)
            namaIbuTv.setText(namaIbu)
            pekerjaanIbuTv.setText(pekerjaanIbu)
            alamatIbuTv.setText(alamatIbu)
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

            if(namaMhs.text.isNotEmpty() && teleponMhs.text.isNotEmpty() && alamatMhs.text.isNotEmpty() && nikMhs.text.isNotEmpty() && tmpLahirMhs.text.isNotEmpty() && tglLahirMhs.text != "DD/MM/YYYY" && darahMhs.text.isNotEmpty() && sekolahMhs.text.isNotEmpty() && nikAyahTv.text.isNotEmpty() && namaAyahTv.text.isNotEmpty() && pekerjaanAyahTv.text.isNotEmpty() && alamatAyahTv.text.isNotEmpty() && nikIbuTv.text.isNotEmpty() && namaIbuTv.text.isNotEmpty() && pekerjaanIbuTv.text.isNotEmpty() && alamatIbuTv.text.isNotEmpty()){
                dialog = Dialog(this)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.show()

                updateDataMhs()
            } else {
                Toast.makeText(this, "Lengkapi data profil terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun updateDateInView() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        tglLahirMhs.text = dateFormat.format(cal.time)
        tglLahirMhs.setTextColor(resources.getColor(R.color.black))
    }

    private fun updateDataMhs() {

        val dataProfil = hashMapOf<String, Any>(
            "nama" to namaMhs.text.toString(),
            "telepon" to teleponMhs.text.toString(),
            "alamat" to alamatMhs.text.toString(),
            "nik" to nikMhs.text.toString(),
            "tempatlahir" to tmpLahirMhs.text.toString(),
            "tgllahir" to cal.time,
            "goldarah" to darahMhs.text.toString(),
            "asalsekolah" to sekolahMhs.text.toString(),
            "nikayah" to nikAyahTv.text.toString(),
            "namaayah" to namaAyahTv.text.toString(),
            "pekerjaanayah" to pekerjaanAyahTv.text.toString(),
            "alamatayah" to alamatAyahTv.text.toString(),
            "nikibu" to nikIbuTv.text.toString(),
            "namaibu" to namaIbuTv.text.toString(),
            "pekerjaanibu" to pekerjaanIbuTv.text.toString(),
            "alamatibu" to alamatIbuTv.text.toString(),
            "updateprofil" to true,
        )

        db.collection("users").document(idUser).update(dataProfil).addOnCompleteListener {

            editor.putString("namaUser", namaMhs.text.toString())
            editor.putString("teleponUser", teleponMhs.text.toString())
            editor.putString("alamatUser", alamatMhs.text.toString())
            editor.putString("nikKtpUser", nikMhs.text.toString())
            editor.putString("tempatLahirUser", tmpLahirMhs.text.toString())
            editor.putString("tglLahirUser", tglLahirMhs.text.toString())
            editor.putString("golDarahUser", darahMhs.text.toString())
            editor.putString("sekolahUser", sekolahMhs.text.toString())
            editor.putString("nikAyah", nikAyahTv.text.toString())
            editor.putString("namaAyah", namaAyahTv.text.toString())
            editor.putString("pekerjaanAyah", pekerjaanAyahTv.text.toString())
            editor.putString("alamatAyah", alamatAyahTv.text.toString())
            editor.putString("nikIbu", nikIbuTv.text.toString())
            editor.putString("namaIbu", namaIbuTv.text.toString())
            editor.putString("pekerjaanIbu", pekerjaanIbuTv.text.toString())
            editor.putString("alamatIbu", alamatIbuTv.text.toString())
            editor.putBoolean("updateProfil", true)
            editor.apply()

            dialog.dismiss()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

    }

}