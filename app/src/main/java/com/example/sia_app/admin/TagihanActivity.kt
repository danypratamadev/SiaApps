package com.example.sia_app.admin

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.DosenAdapter
import com.example.sia_app.adapter.MahasiswaAdapter
import com.example.sia_app.adapter.TagihanAdapter
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.TagihanModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TagihanActivity : AppCompatActivity() {

    private lateinit var rvPembayaran: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private lateinit var db: FirebaseFirestore
    private val listMahasiswa = ArrayList<MahasiswaModel>()
    private val listPembayaran = ArrayList<TagihanModel>()

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagihan2)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        rvPembayaran = findViewById(R.id.rvPembayaran)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvPembayaran.setHasFixedSize(true)
        rvPembayaran.layoutManager = LinearLayoutManager(this)



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
                getAllPembayaran()
            }
        }

    }

    private fun getAllPembayaran() {

        db.collection("tagihan").whereEqualTo("status", 1).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                listPembayaran.clear()
                val tagihanAdapter = TagihanAdapter(this, listPembayaran, listMahasiswa, this,true)
                rvPembayaran.adapter = tagihanAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
            } else {
                listPembayaran.clear()
                for (document in result) {
                    listPembayaran.add(
                        TagihanModel(
                            document.id,
                            document.data["idMhs"].toString(),
                            document.data["nama"].toString(),
                            document.data["tagihan"].toString().toInt(),
                            document.data["bukti"].toString(),
                            document.data["status"].toString().toInt())
                    )
                }
                val tagihanAdapter = TagihanAdapter(this, listPembayaran, listMahasiswa, this,true)
                rvPembayaran.adapter = tagihanAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }

    fun updateValidasiPembayaran(id: String, status: Int, tagihan: Int){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataValidasi = hashMapOf<String, Any>(
            "status" to status,
            "tagihan" to tagihan
        )

        db.collection("tagihan").document(id).update(dataValidasi).addOnCompleteListener {
            dialog.dismiss()
            getAllPembayaran()
        }

    }
}