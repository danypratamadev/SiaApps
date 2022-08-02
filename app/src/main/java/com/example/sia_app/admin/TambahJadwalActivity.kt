package com.example.sia_app.admin

import android.app.Dialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import com.example.sia_app.R
import com.example.sia_app.adapter.DosenAdapter
import com.example.sia_app.adapter.MKAdapter
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.JadwalModel
import com.example.sia_app.model.MKModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class TambahJadwalActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var db: FirebaseFirestore
    private val listMk = ArrayList<MKModel>()
    private val listDosen = ArrayList<DosenModel>()
    private lateinit var dialog : Dialog

    private lateinit var spinMk: Spinner
    private lateinit var spinDosen: Spinner
    private lateinit var spinHari: Spinner
    private lateinit var spinJam: Spinner
    private lateinit var spinRuangan: Spinner
    private lateinit var kuotaKelas: EditText
    private lateinit var tarifKelas: EditText
    private lateinit var simpanJadwal: Button

    private val listHari = arrayOf("Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu")
    private val listRuangan = arrayOf("E11", "E12", "E13", "E14", "D11", "D12", "D13", "D14", "E21", "E22", "E23", "E24", "D21", "D22", "D23", "D24", "E31", "E32", "E33", "E34", "D31", "D32", "D33", "D34")
    private val list2Sks = arrayOf("07:00 - 08:40", "08:50 - 10:30", "10:40 - 12:20", "12:50 - 14:30", "14:40 - 16:20", "16:30 - 18:10")
    private val list3Sks = arrayOf("07:00 - 09:30", "09:40 - 12:10", "12:50 - 15:20", "15:30 - 18:00")

    private lateinit var idMkChoose: String
    private lateinit var idDosenChoose: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_jadwal)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        spinMk = findViewById(R.id.spinMk)
        spinDosen = findViewById(R.id.spinDosen)
        spinHari = findViewById(R.id.spinHari)
        spinJam = findViewById(R.id.spinJam)
        spinRuangan = findViewById(R.id.spinRuangan)
        kuotaKelas = findViewById(R.id.kuotaKelas)
        tarifKelas = findViewById(R.id.tarifKelas)
        simpanJadwal = findViewById(R.id.simpanJadwal)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listHari)
        spinHari.adapter = adapter

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listRuangan)
        spinRuangan.adapter = adapter2

        getAllMk()
        getAllDosen()

        spinMk.onItemSelectedListener = this
        spinDosen.onItemSelectedListener = this

        simpanJadwal.setOnClickListener {
            if(kuotaKelas.text.isNotEmpty() && tarifKelas.text.isNotEmpty()){
                try {
                    val keyInput = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    keyInput.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    Log.e("Error", "Something went wrong!")
                }

                dialog = Dialog(this)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.show()

                saveJadwal()
            }
        }

    }

    private fun getAllMk() {

        db.collection("matakuliah").get().addOnSuccessListener { result ->
            if(result.isEmpty){

            } else {
                listMk.clear()
                for (document in result) {
                    listMk.add(MKModel(
                        document.id,
                        document.data["kdMk"].toString(),
                        document.data["nama"].toString(),
                        document.data["sks"].toString().toInt(),
                        document.data["jenis"].toString(),
                    ))
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listMk)
                spinMk.adapter = adapter
            }
        }

    }

    private fun getAllDosen() {

        db.collection("users").whereEqualTo("role", 20).get().addOnSuccessListener { result ->
            if(result.isEmpty){

            } else {
                listDosen.clear()
                for (document in result) {
                    listDosen.add(DosenModel(
                        document.id,
                        document.data["nik"].toString(),
                        document.data["nama"].toString(),
                        document.data["email"].toString(),
                        document.data["telepon"].toString(),
                        document.data["alamat"].toString(),
                    ))
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listDosen)
                spinDosen.adapter = adapter
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.spinMk -> {
                idMkChoose = listMk[position].id
                if(listMk[position].sksMk == 2){
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list2Sks)
                    spinJam.adapter = adapter
                } else {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list3Sks)
                    spinJam.adapter = adapter
                }
            }
            R.id.spinDosen -> {
                idDosenChoose = listDosen[position].id
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun saveJadwal(){

        val dataJadwal = hashMapOf<String, Any>(
            "idMK" to idMkChoose,
            "idDosen" to idDosenChoose,
            "hari" to spinHari.selectedItem,
            "jam" to spinJam.selectedItem,
            "ruangan" to spinRuangan.selectedItem,
            "kuota" to kuotaKelas.text.toString().toInt(),
            "tarif" to tarifKelas.text.toString().toInt(),
        )

        db.collection("jadwal").add(dataJadwal).addOnSuccessListener { result ->
            savePertemuan(result.id)
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error adding document", e)
        }
    }

    private fun savePertemuan(id: String){
        for(i in 1..14){
            val pertemuanKe = "Pertemuan $i"
            var listMhs = ArrayList<String>()
            var listNilaiTugas = ArrayList<Int>()
            val dataPertemuan = hashMapOf<String, Any>(
                "pertemuan" to i,
                "nama" to pertemuanKe,
                "kodepresensi" to 0,
                "durasipresensi" to 0,
                "presensidibuat" to FieldValue.serverTimestamp(),
                "list_mhs" to listMhs,
                "list_nilaitugas" to listNilaiTugas,
                "beritaacara" to "-",
                "beritadibuat" to FieldValue.serverTimestamp(),
            )
            db.collection("jadwal").document(id).collection("pertemuan").add(dataPertemuan)
        }
        dialog.dismiss()
        onBackPressed()
    }
}