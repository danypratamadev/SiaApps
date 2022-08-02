package com.example.sia_app.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.PertemuanAdapter
import com.example.sia_app.dosen.MkDosenActivity
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.PertemuanModel
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PertemuanMk : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var rvPertemuan: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private var listPertemuan = ArrayList<PertemuanModel>()
    private val listMahasiswa = ArrayList<MahasiswaModel>()

    private var idJadwal: String = "-"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pertemuan_mk)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        idJadwal = intent.getStringExtra("id").toString()

        rvPertemuan = findViewById(R.id.rvPertemuan)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvPertemuan.setHasFixedSize(true)
        rvPertemuan.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        getAllMahasiswa()
        super.onResume()
    }

    private fun getAllMahasiswa() {

        db.collection("users").whereEqualTo("role", 30).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
            } else {
                listMahasiswa.clear()
                for (document in result) {
                    listMahasiswa.add(
                        MahasiswaModel(
                            document.id,
                            document.data["nim"].toString(),
                            document.data["nama"].toString(),
                            document.data["email"].toString(),
                            document.data["telepon"].toString(),
                            document.data["alamat"].toString())
                    )
                }
                getAllPertemuan()
            }
        }

    }

    private fun getAllPertemuan() {

        db.collection("jadwal").document(idJadwal).collection("pertemuan").orderBy("pertemuan").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
            } else {
                listPertemuan.clear()
                for (document in result) {
                    val presensidibuat: Timestamp = document.data["presensidibuat"] as Timestamp
                    val beritadibuat: Timestamp = document.data["beritadibuat"] as Timestamp
                    val listMhs: ArrayList<String> = document.data["list_mhs"] as ArrayList<String>
                    val listNilaiTugas: ArrayList<Int> = document.data["list_nilaitugas"] as ArrayList<Int>
                    if(document.data["beritaacara"].toString() != "-" && listMhs.size > 0){
                        listPertemuan.add(
                            PertemuanModel(
                                document.id,
                                document.data["nama"].toString(),
                                document.data["kodepresensi"].toString().toInt(),
                                document.data["durasipresensi"].toString().toInt(),
                                presensidibuat.toDate(),
                                document.data["beritaacara"].toString(),
                                beritadibuat.toDate(),
                                listMhs,
                                listNilaiTugas
                            )
                        )
                    }
                }
                val pertemuanAdapter = PertemuanAdapter(this, MkDosenActivity(), listPertemuan, listMahasiswa, false)
                rvPertemuan.adapter = pertemuanAdapter
                if(listPertemuan.size > 0){
                    loadingIndicator.isVisible = false
                    emptyInfo.isVisible = false
                } else {
                    loadingIndicator.isVisible = false
                    emptyInfo.isVisible = true
                }
            }
        }

    }

}