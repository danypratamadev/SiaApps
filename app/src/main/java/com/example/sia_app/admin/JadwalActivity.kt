package com.example.sia_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.DosenAdapter
import com.example.sia_app.adapter.JadwalAdapter
import com.example.sia_app.adapter.MKAdapter
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.JadwalModel
import com.example.sia_app.model.KrsModel
import com.example.sia_app.model.MKModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JadwalActivity : AppCompatActivity() {

    private lateinit var tambahJadwal: FloatingActionButton
    private lateinit var rvJadwal: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private lateinit var db: FirebaseFirestore
    private val listJadwal = ArrayList<JadwalModel>()
    private val listMK = ArrayList<MKModel>()
    private val listDosen = ArrayList<DosenModel>()
    private val listKrsMhs = ArrayList<KrsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        rvJadwal = findViewById(R.id.rvJadwal)
        tambahJadwal = findViewById(R.id.tambahJadwal)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvJadwal.setHasFixedSize(true)
        rvJadwal.layoutManager = LinearLayoutManager(this)

        tambahJadwal.setOnClickListener {
            startActivity(Intent(this, TambahJadwalActivity::class.java))
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
                val jadwalAdapter = JadwalAdapter(this, listJadwal, listMK, listDosen, listKrsMhs, false, false)
                rvJadwal.adapter = jadwalAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }
}