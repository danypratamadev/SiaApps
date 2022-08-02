package com.example.sia_app.mhs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.LoginActivity
import com.example.sia_app.R
import com.example.sia_app.adapter.JadwalAdapter
import com.example.sia_app.adapter.KrsMhsAdapter
import com.example.sia_app.adapter.TagihanAdapter
import com.example.sia_app.admin.DetailDosenActivity
import com.example.sia_app.admin.TagihanActivity
import com.example.sia_app.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeMhsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var accountMhs: LinearLayout
    private lateinit var namaMhs: TextView
    private lateinit var nimMhs: TextView
    private lateinit var rvMkMhs: RecyclerView
    private lateinit var rvTgMhs: RecyclerView
    private lateinit var noteHint: TextView
    private lateinit var labelPembayaran: TextView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout
    private lateinit var tambahMk : Button
    private lateinit var logout : Button

    private lateinit var db: FirebaseFirestore

    private lateinit var idUser: String
    private lateinit var namaUser: String
    private lateinit var nimUser: String

    private val listJadwal = ArrayList<JadwalModel>()
    private val listMK = ArrayList<MKModel>()
    private val listDosen = ArrayList<DosenModel>()
    private val listKrsMhs = ArrayList<KrsModel>()
    private val listTgMhs = ArrayList<TagihanModel>()
    private val listMahasiswa = ArrayList<MahasiswaModel>()

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_mhs)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)

        idUser = sharedPref.getString("idUser", "-").toString()
        namaUser = sharedPref.getString("namaUser", "-").toString()
        nimUser = sharedPref.getString("nikUser", "-").toString()

        accountMhs = findViewById(R.id.accountMhs)
        namaMhs = findViewById(R.id.namaMhs)
        nimMhs = findViewById(R.id.nimMhs)
        rvMkMhs = findViewById(R.id.rvMkMhs)
        rvTgMhs = findViewById(R.id.rvTgMhs)
        noteHint = findViewById(R.id.noteHint)
        labelPembayaran = findViewById(R.id.labelPembayaran)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)
        tambahMk = findViewById(R.id.tambahMk)
        logout = findViewById(R.id.logout)

        namaMhs.text = namaUser
        nimMhs.text = nimUser

        rvMkMhs.setHasFixedSize(true)
        rvMkMhs.layoutManager = LinearLayoutManager(this)
        rvTgMhs.setHasFixedSize(true)
        rvTgMhs.layoutManager = LinearLayoutManager(this)

        accountMhs.setOnClickListener {
            startActivity(Intent(this, AccountMhsActivity::class.java))
        }

        tambahMk.setOnClickListener {
            startActivity(Intent(this, InputKrsActivity::class.java))
        }

        logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Keluar Akun")
            builder.setMessage("Apakah anda yakin ingin keluar dari akun?")
            builder.setCancelable(true)

            builder.setNegativeButton("Ya") { dialog, id ->
                dialog.cancel()
                logoutAuthentication()
            }

            builder.setPositiveButton("Tidak") { dialog, id ->
                dialog.cancel()
            }

            val alertDelete = builder.create()
            alertDelete.show()
        }

    }

    override fun onResume() {
        getAllMk()
        getTagihanMhs()
        super.onResume()
    }

    private fun getAllMk() {

        db.collection("matakuliah").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
                noteHint.isVisible = false
            } else {
                listMK.clear()
                for (document in result) {
                    listMK.add(
                        MKModel(
                        document.id,
                        document.data["kdMk"].toString(),
                        document.data["nama"].toString(),
                        document.data["sks"].toString().toInt(),
                        document.data["jenis"].toString(),
                    )
                    )
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
                noteHint.isVisible = false
            } else {
                listDosen.clear()
                for (document in result) {
                    listDosen.add(
                        DosenModel(
                        document.id,
                        document.data["nik"].toString(),
                        document.data["nama"].toString(),
                        document.data["email"].toString(),
                        document.data["telepon"].toString(),
                        document.data["alamat"].toString(),
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
                noteHint.isVisible = false
            } else {
                listJadwal.clear()
                for (document in result) {
                    listJadwal.add(
                        JadwalModel(
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
                getKrsMhs()
            }
        }

    }

    private fun getKrsMhs(){

        db.collection("users").document(idUser).collection("list_mk").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
                noteHint.isVisible = false
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
                val krsAdapter = KrsMhsAdapter(this, listJadwal, listMK, listDosen, listKrsMhs)
                rvMkMhs.adapter = krsAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
                noteHint.isVisible = true
                labelPembayaran.isVisible = true
                rvTgMhs.isVisible = true
            }
        }

    }

    private fun getTagihanMhs(){

        db.collection("tagihan").whereEqualTo("idMhs", idUser).get().addOnSuccessListener { result ->
            if(result.isEmpty){

            } else {
                listTgMhs.clear()
                for (document in result) {
                    listTgMhs.add(
                        TagihanModel(
                            document.id,
                            document.data["idMhs"].toString(),
                            document.data["nama"].toString(),
                            document.data["tagihan"].toString().toInt(),
                            document.data["bukti"].toString(),
                            document.data["status"].toString().toInt()
                        )
                    )
                }
                val tgAdapter = TagihanAdapter(this, listTgMhs, listMahasiswa, TagihanActivity(), false)
                rvTgMhs.adapter = tgAdapter
            }
        }

    }

    private fun logoutAuthentication(){
        dialog.show()
        auth.signOut()
        editor.clear()
        editor.apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}