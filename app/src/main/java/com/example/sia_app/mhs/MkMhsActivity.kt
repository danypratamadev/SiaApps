package com.example.sia_app.mhs

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.PertemuanAdapter
import com.example.sia_app.adapter.PertemuanAdapter2
import com.example.sia_app.model.PertemuanModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MkMhsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences

    private lateinit var namaMkTv: TextView
    private lateinit var sksMkTv: TextView
    private lateinit var jenisMkTv: TextView
    private lateinit var jamMkTv: TextView
    private lateinit var ruanganMkTv: TextView
    private lateinit var pengampuMk: TextView
    private lateinit var nilaiUts: TextView
    private lateinit var nilaiAkhir: TextView
    private lateinit var rvPertemuan: RecyclerView
    private lateinit var loadingIndicator: LinearLayout

    private var listPertemuan = ArrayList<PertemuanModel>()
    var idUser: String = "-"
    var idJadwal: String = "-"

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mk_mhs)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)

        idUser = sharedPref.getString("idUser", "-").toString()

        val id = intent.getStringExtra("id")
        val idMk = intent.getStringExtra("idMk")
        val idDosen = intent.getStringExtra("idDosen")
        val namaDosen = intent.getStringExtra("namaDosen")
        idJadwal = intent.getStringExtra("idJadwal").toString()
        val namaMk = intent.getStringExtra("namaMk")
        val sksMk = intent.getStringExtra("sksMk")
        val hariMk = intent.getStringExtra("hariMk")
        val jamMk = intent.getStringExtra("jamMk")
        val ruangMk = intent.getStringExtra("ruangMk")
        val jenisMk = intent.getStringExtra("jenisMk")

        namaMkTv = findViewById(R.id.namaMk)
        sksMkTv = findViewById(R.id.sksMk)
        jenisMkTv = findViewById(R.id.jenisMk)
        jamMkTv = findViewById(R.id.jamMk)
        ruanganMkTv = findViewById(R.id.ruanganMk)
        pengampuMk = findViewById(R.id.pengampuMk)
        nilaiUts = findViewById(R.id.nilaiUts)
        nilaiAkhir = findViewById(R.id.nilaiAkhir)
        rvPertemuan = findViewById(R.id.rvPertemuan)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        namaMkTv.text = namaMk
        sksMkTv.text = "$sksMk SKS"
        jenisMkTv.text = jenisMk?.uppercase()
        jamMkTv.text = "$hariMk, $jamMk WIB"
        ruanganMkTv.text = ruangMk
        pengampuMk.text = namaDosen

        rvPertemuan.setHasFixedSize(true)
        rvPertemuan.layoutManager = LinearLayoutManager(this)

        getNilaiMhs()
        getAllPertemuan()

    }

    private fun getNilaiMhs() {

        db.collection("jadwal").document(idJadwal).collection("list_mhs").whereEqualTo("idMhs", idUser).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                nilaiUts.text = "Nilai UTS: N/a"
                nilaiAkhir.text = "Nilai UTS: N/a"
            } else {
                for (document in result) {
                    val nilaiuts = document.data["nilaiuts"].toString()
                    val nilaiakhir = document.data["nilaiakhir"].toString()
                    nilaiUts.text = "Nilai UTS: $nilaiuts"
                    nilaiAkhir.text = "Nilai Akhir: $nilaiakhir"
                }
            }
        }

    }

    private fun getAllPertemuan() {

        db.collection("jadwal").document(idJadwal).collection("pertemuan").orderBy("pertemuan").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
            } else {
                listPertemuan.clear()
                for (document in result) {
                    val presensidibuat: Timestamp = document.data["presensidibuat"] as Timestamp
                    val beritadibuat: Timestamp = document.data["beritadibuat"] as Timestamp
                    val listMhs: ArrayList<String> = document.data["list_mhs"] as ArrayList<String>
                    val listNilaiTugas: ArrayList<Int> = document.data["list_nilaitugas"] as ArrayList<Int>
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
                val pertemuanAdapter = PertemuanAdapter2(this, listPertemuan, idUser)
                rvPertemuan.adapter = pertemuanAdapter
                loadingIndicator.isVisible = false
            }
        }

    }

    fun presensiMhs(id: String){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataPresensi = hashMapOf<String, Any>(
            "list_mhs" to FieldValue.arrayUnion(idUser),
            "list_nilaitugas" to FieldValue.arrayUnion(0)
        )

        db.collection("jadwal").document(idJadwal).collection("pertemuan").document(id).update(dataPresensi).addOnCompleteListener {
            dialog.dismiss()
            getAllPertemuan()
        }

    }

}