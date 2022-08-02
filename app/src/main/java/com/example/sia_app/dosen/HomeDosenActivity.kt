package com.example.sia_app.dosen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.LoginActivity
import com.example.sia_app.R
import com.example.sia_app.adapter.MengajarAdapter
import com.example.sia_app.mhs.AccountMhsActivity
import com.example.sia_app.model.MKModel
import com.example.sia_app.model.MengajarModel
import com.example.sia_app.model.MenuModel
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeDosenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var accountDosen: LinearLayout
    private lateinit var namaDosen: TextView
    private lateinit var nikDosen: TextView
    private lateinit var rvMengajar: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout
    private lateinit var noteHint: TextView
    private lateinit var logout : Button

    private lateinit var db: FirebaseFirestore
    private val listMK = ArrayList<MKModel>()
    private val listMengajar = ArrayList<MengajarModel>()

    private lateinit var idUser: String
    private lateinit var namaUser: String
    private lateinit var nikUser: String

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dosen)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        idUser = sharedPref.getString("idUser", "-").toString()
        namaUser = sharedPref.getString("namaUser", "-").toString()
        nikUser = sharedPref.getString("nikUser", "-").toString()

        accountDosen = findViewById(R.id.accountDosen)
        namaDosen = findViewById(R.id.namaDosen)
        nikDosen = findViewById(R.id.nikDosen)
        rvMengajar = findViewById(R.id.rvMengajar)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)
        noteHint = findViewById(R.id.noteHint)
        logout = findViewById(R.id.logout)

        namaDosen.text = namaUser
        nikDosen.text = nikUser

        rvMengajar.setHasFixedSize(true)
        rvMengajar.layoutManager = LinearLayoutManager(this)

        accountDosen.setOnClickListener {
            startActivity(Intent(this, AccountDosenActivity::class.java))
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
                    listMK.add(MKModel(
                        document.id,
                        document.data["kdMk"].toString(),
                        document.data["nama"].toString(),
                        document.data["sks"].toString().toInt(),
                        document.data["jenis"].toString(),
                    ))
                }
                getAllMengajar()
            }
        }

    }

    private fun getAllMengajar(){

        db.collection("jadwal").whereEqualTo("idDosen", idUser).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
                noteHint.isVisible = false
            } else {
                listMengajar.clear()
                for (document in result) {
                    listMengajar.add(MengajarModel(
                        document.id,
                        document.data["idMK"].toString(),
                        document.data["hari"].toString(),
                        document.data["jam"].toString(),
                        document.data["ruangan"].toString(),
                    ))
                }
                val mengajarAdapter = MengajarAdapter(this, listMengajar, listMK,)
                rvMengajar.adapter = mengajarAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
                noteHint.isVisible = true
            }
        }

    }

    private fun logoutAuthentication(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()
        auth.signOut()
        editor.clear()
        editor.apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}