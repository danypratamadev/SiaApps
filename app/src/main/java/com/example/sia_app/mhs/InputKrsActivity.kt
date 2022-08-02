package com.example.sia_app.mhs

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.JadwalAdapter
import com.example.sia_app.adapter.KrsMhsAdapter
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.JadwalModel
import com.example.sia_app.model.KrsModel
import com.example.sia_app.model.MKModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class InputKrsActivity() : AppCompatActivity() {

    private lateinit var rvMataKuliah: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout
    private lateinit var jmlSks: TextView
    private lateinit var jmlTarif: TextView
    private lateinit var simpanMk: Button

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var db: FirebaseFirestore
    private val listJadwal = ArrayList<JadwalModel>()
    private val listMK = ArrayList<MKModel>()
    private val listDosen = ArrayList<DosenModel>()
    private val listKrsMhs = ArrayList<KrsModel>()

    private lateinit var idUser: String
    private var jmlSksAmbil: Int = 0
    private var jmlTarifSks: Int = 0

    private lateinit var dialog : Dialog
    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_krs)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        idUser = sharedPref.getString("idUser", "-").toString()

        rvMataKuliah = findViewById(R.id.rvMataKuliah)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)
        jmlTarif = findViewById(R.id.jmlTarif)
        jmlSks = findViewById(R.id.jmlSks)
        simpanMk = findViewById(R.id.simpanMk)

        rvMataKuliah.setHasFixedSize(true)
        rvMataKuliah.layoutManager = LinearLayoutManager(this)

        simpanMk.setOnClickListener {
            if(jmlSksAmbil > 0){
                dialog = Dialog(this)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.show()
                saveInputKrs(0)
            } else {
                Toast.makeText(this, "Tambah Mata Kuliah terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        getAllMk()
        super.onResume()
    }

    private fun getAllMk() {

        db.collection("matakuliah").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
            } else {
                listMK.clear()
                for (document in result) {
                    listMK.add(MKModel(
                        document.id,
                        document.data["kdMk"].toString(),
                        document.data["nama"].toString(),
                        document.data["sks"].toString().toInt(),
                        document.data["jenis"].toString(),
                    ))
                }
                getAllDosen()
            }
        }

    }

    private fun getAllDosen() {

        db.collection("users").whereEqualTo("role", 20).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
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
                getKrsMhs()
            }
        }

    }

    private fun getKrsMhs(){

        db.collection("users").document(idUser).collection("list_mk").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                getAllJadwal()
            } else {
                listKrsMhs.clear()
                for (document in result) {
                    listKrsMhs.add(
                        KrsModel(
                            document.id,
                            document.data["idJadwal"].toString(),
                        )
                    )
                }
                getAllJadwal()
            }
        }

    }

    private fun getAllJadwal(){

        db.collection("jadwal").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
            } else {
                listJadwal.clear()
                for (document in result) {
                    listJadwal.add(JadwalModel(
                        document.id,
                        document.data["idMK"].toString(),
                        document.data["idDosen"].toString(),
                        document.data["hari"].toString(),
                        document.data["jam"].toString(),
                        document.data["ruangan"].toString(),
                        document.data["kuota"].toString().toInt(),
                        document.data["tarif"].toString().toInt(),
                        false)
                    )
                }
                val jadwalAdapter = JadwalAdapter(this, listJadwal, listMK, listDosen, listKrsMhs, true, false)
                rvMataKuliah.adapter = jadwalAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }

    fun updateTotalSks(tarif: Int, tambahMk: Boolean) {
        var jumlahSks = 0
        if(tambahMk){
            jmlTarifSks += tarif
        } else {
            jmlTarifSks -= tarif
        }
        for (listMk in listJadwal){
            if(listMk.check){
                listMK.forEach { mk ->
                    if(mk.id == listMk.idMk){
                        jumlahSks += mk.sksMk
                    }
                }
            }
        }
        jmlSks.text = "$jumlahSks SKS"
        jmlTarif.text = numberFormat.format(jmlTarifSks)
        jmlSksAmbil = jumlahSks;
    }

    private fun saveInputKrs(index: Int){

        if(listJadwal[index].check){

            val dataMhs = hashMapOf<String, Any>(
                "idMhs" to idUser,
                "nilaiuts" to 0,
                "nilaiakhir" to "-",
            )

            val dataMk = hashMapOf<String, Any>(
                "idJadwal" to listJadwal[index].id,
            )

            val dataKuota = hashMapOf<String, Any>(
                "kuota" to FieldValue.increment(-1),
            )

            db.collection("jadwal").document(listJadwal[index].id).collection("list_mhs").add(dataMhs)
            db.collection("jadwal").document(listJadwal[index].id).update(dataKuota)

            db.collection("users").document(idUser).collection("list_mk").add(dataMk).addOnSuccessListener {
                val newIndex = index + 1
                if(newIndex < listJadwal.size){
                    saveInputKrs(newIndex)
                } else {
                    val dataTagihan = hashMapOf<String, Any>(
                        "idMhs" to idUser,
                        "tagihan" to jmlTarifSks,
                        "nama" to "SPP Variabel",
                        "bukti" to "-",
                        "status" to 0,
                    )
                    db.collection("tagihan").add(dataTagihan)
                    dialog.dismiss()
                    onBackPressed()
                }
            }.addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                val newIndex = index + 1
                if(newIndex < listJadwal.size){
                    saveInputKrs(newIndex)
                } else {
                    val dataTagihan = hashMapOf<String, Any>(
                        "idMhs" to idUser,
                        "tagihan" to jmlTarifSks,
                        "nama" to "SPP Variabel",
                        "bukti" to "-",
                        "status" to 0,
                    )
                    db.collection("tagihan").add(dataTagihan)
                    dialog.dismiss()
                    onBackPressed()
                }
            }

        } else {
            val newIndex = index + 1
            if(newIndex < listJadwal.size){
                saveInputKrs(newIndex)
            } else {
                val dataTagihan = hashMapOf<String, Any>(
                    "idMhs" to idUser,
                    "tagihan" to jmlTarifSks,
                    "nama" to "SPP Variabel",
                    "bukti" to "-",
                    "status" to 0,
                )
                db.collection("tagihan").add(dataTagihan)
                dialog.dismiss()
                onBackPressed()
            }
        }

    }
}