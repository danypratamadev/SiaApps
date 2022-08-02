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
import com.example.sia_app.model.DosenModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DosenActivity : AppCompatActivity() {

    private lateinit var tambahDosen: FloatingActionButton
    private lateinit var rvDosen: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private lateinit var db: FirebaseFirestore
    private val listDosen = ArrayList<DosenModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dosen)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        rvDosen = findViewById(R.id.rvDosen)
        tambahDosen = findViewById(R.id.tambahDosen)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvDosen.setHasFixedSize(true)
        rvDosen.layoutManager = LinearLayoutManager(this)

        tambahDosen.setOnClickListener {
            startActivity(Intent(this, TambahDosenActivity::class.java))
        }

    }

    override fun onResume() {
        getAllDosen()
        super.onResume()
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
                val dosenAdapter = DosenAdapter(this, listDosen)
                rvDosen.adapter = dosenAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }
}