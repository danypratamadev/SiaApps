package com.example.sia_app.dosen

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.MengajarAdapter
import com.example.sia_app.adapter.PertemuanAdapter
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.MengajarModel
import com.example.sia_app.model.PertemuanModel
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MkDosenActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var namaMkTv: TextView
    private lateinit var sksMkTv: TextView
    private lateinit var jamMkTv: TextView
    private lateinit var ruanganMkTv: TextView
    private lateinit var jenisMkTv: TextView
    private lateinit var jmlMhsMk: TextView
    private lateinit var rvPertemuan: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var daftarMhs: LinearLayout

    private var listPertemuan = ArrayList<PertemuanModel>()
    private val listMahasiswa = ArrayList<MahasiswaModel>()
    private var idJadwal: String = "-"

    private lateinit var dialog : Dialog
    private val REQ = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mk_dosen)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        idJadwal = intent.getStringExtra("id").toString()
        val idMk = intent.getStringExtra("idMk")
        val namaMk = intent.getStringExtra("namaMk")
        val sksMk = intent.getStringExtra("sksMk")
        val hariMk = intent.getStringExtra("hariMk")
        val jamMk = intent.getStringExtra("jamMk")
        val ruangMk = intent.getStringExtra("ruangMk")
        val jenisMk = intent.getStringExtra("jenisMk")

        namaMkTv = findViewById(R.id.namaMk)
        sksMkTv = findViewById(R.id.sksMk)
        jamMkTv = findViewById(R.id.jamMk)
        ruanganMkTv = findViewById(R.id.ruanganMk)
        jenisMkTv = findViewById(R.id.jenisMk)
        jmlMhsMk = findViewById(R.id.jmlMhsMk)
        rvPertemuan = findViewById(R.id.rvPertemuan)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        daftarMhs = findViewById(R.id.daftarMhs)

        namaMkTv.text = namaMk
        sksMkTv.text = "$sksMk SKS"
        jamMkTv.text = "$hariMk, $jamMk WIB"
        ruanganMkTv.text = ruangMk
        jenisMkTv.text = jenisMk?.uppercase()

        rvPertemuan.setHasFixedSize(true)
        rvPertemuan.layoutManager = LinearLayoutManager(this)

        getAllMahasiswa()

        daftarMhs.setOnClickListener {
            val intent = Intent(this, DaftarMhsActivity::class.java)
            intent.putExtra("id", idJadwal)
            startActivity(intent)
        }

    }

    private fun getAllMahasiswa() {

        db.collection("users").whereEqualTo("role", 30).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                jmlMhsMk.text = "0 Mahasiswa"
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
                getAllMhs()
                getAllPertemuan()
            }
        }

    }

    private fun getAllMhs() {

        db.collection("jadwal").document(idJadwal).collection("list_mhs").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                jmlMhsMk.text = "0 Mahasiswa"
            } else {
                jmlMhsMk.text = result.documents.size.toString() + " Mahasiswa"
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
                            listNilaiTugas,
                        )
                    )
                }
                val pertemuanAdapter = PertemuanAdapter(this, this, listPertemuan, listMahasiswa, true)
                rvPertemuan.adapter = pertemuanAdapter
                loadingIndicator.isVisible = false
            }
        }

    }

    fun setPresensi(id: String, kode: String, durasi: String) {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataJadwal = hashMapOf<String, Any>(
            "kodepresensi" to kode.toInt(),
            "durasipresensi" to durasi.split(" ").first().toString().toInt(),
            "presensidibuat" to FieldValue.serverTimestamp()
        )

        db.collection("jadwal").document(idJadwal).collection("pertemuan").document(id).update(dataJadwal).addOnCompleteListener {
            dialog.dismiss()
            getAllPertemuan()
        }

    }

    fun isiBeritaAcara(id: String,){
        val intent = Intent(this, BeritaAcaraActivity::class.java)
        intent.putExtra("idJadwal", idJadwal)
        intent.putExtra("idPertemuan", id)
        startActivityForResult(intent, REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == REQ){
            getAllPertemuan()
        }
    }

    fun inputNilaiTugasMhs(id: String, listNilai: ArrayList<Int>,){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataPresensi = hashMapOf<String, Any>(
            "list_nilaitugas" to listNilai
        )

        db.collection("jadwal").document(idJadwal).collection("pertemuan").document(id).update(dataPresensi).addOnCompleteListener {
            dialog.dismiss()
            getAllPertemuan()
        }

    }

}