package com.example.sia_app.admin

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.DosenAdapter
import com.example.sia_app.adapter.MahasiswaAdapter
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.MahasiswaModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MahasiswaActivity : AppCompatActivity() {

    private lateinit var tambahMahasiswa: FloatingActionButton
    private lateinit var rvMahasiswa: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private lateinit var db: FirebaseFirestore
    private val listMahasiswa = ArrayList<MahasiswaModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mahasiswa)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        rvMahasiswa = findViewById(R.id.rvMahasiswa)
        tambahMahasiswa = findViewById(R.id.tambahMahasiswa)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvMahasiswa.setHasFixedSize(true)
        rvMahasiswa.layoutManager = LinearLayoutManager(this)

        tambahMahasiswa.setOnClickListener {
            startActivity(Intent(this, TambahMahasiswaActivity::class.java))
        }
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
                val mhsAdapter = MahasiswaAdapter(this, listMahasiswa)
                rvMahasiswa.adapter = mhsAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }
}