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
import com.example.sia_app.adapter.MKAdapter
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.MKModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MataKuliahActivity : AppCompatActivity() {

    private lateinit var tambahMK: FloatingActionButton
    private lateinit var rvMK: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private lateinit var db: FirebaseFirestore
    private val listMK = ArrayList<MKModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mata_kuliah)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        rvMK = findViewById(R.id.rvMK)
        tambahMK = findViewById(R.id.tambahMK)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvMK.setHasFixedSize(true)
        rvMK.layoutManager = LinearLayoutManager(this)

        tambahMK.setOnClickListener {
            startActivity(Intent(this, TambahMataKuliahActivity::class.java))
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
                val mkAdapter = MKAdapter(this, listMK)
                rvMK.adapter = mkAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }
}