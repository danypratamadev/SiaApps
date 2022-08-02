package com.example.sia_app.admin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import com.example.sia_app.LoginActivity
import com.example.sia_app.R
import com.example.sia_app.adapter.MenuAdapter
import com.example.sia_app.model.MenuModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var gridView: GridView
    private lateinit var logout : Button
    private val listMenu = ArrayList<MenuModel>()

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        FirebaseApp.initializeApp(this);
        auth = Firebase.auth
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        gridView = findViewById(R.id.gridView)
        logout = findViewById(R.id.logout)

        listMenu.add(MenuModel(1, R.drawable.female, "Dosen"))
        listMenu.add(MenuModel(2, R.drawable.student, "Mahasiswa"))
        listMenu.add(MenuModel(3, R.drawable.book, "Mata Kuliah"))
        listMenu.add(MenuModel(4, R.drawable.schedule, "Jadwal"))
        listMenu.add(MenuModel(5, R.drawable.calendar, "Persensi"))
        listMenu.add(MenuModel(6, R.drawable.wallet, "Validasi Pembayaran"))

        val mainAdapter = MenuAdapter(this, listMenu)
        gridView.adapter = mainAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if(listMenu[position].id == 1){
                startActivity(Intent(this, DosenActivity::class.java))
            } else if(listMenu[position].id == 2){
                startActivity(Intent(this, MahasiswaActivity::class.java))
            } else if(listMenu[position].id == 3){
                startActivity(Intent(this, MataKuliahActivity::class.java))
            } else if(listMenu[position].id == 4){
                startActivity(Intent(this, JadwalActivity::class.java))
            } else if(listMenu[position].id == 5) {
                startActivity(Intent(this, PersensiActivity::class.java))
            } else {
                startActivity(Intent(this, TagihanActivity::class.java))
            }
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

    private fun logoutAuthentication(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()
        auth.signOut()
        editor.clear()
        editor.apply()
        dialog.dismiss()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}