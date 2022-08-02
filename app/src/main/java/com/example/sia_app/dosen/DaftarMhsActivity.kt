package com.example.sia_app.dosen

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.adapter.MahasiswaAdapter
import com.example.sia_app.adapter.MahasiswaAdapter2
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.MahasiswaModel2
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DaftarMhsActivity : AppCompatActivity() {

    private lateinit var rvMahasiswa: RecyclerView
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var emptyInfo: LinearLayout

    private lateinit var db: FirebaseFirestore
    private val listMahasiswa = ArrayList<MahasiswaModel>()
    private val listMhsMk = ArrayList<MahasiswaModel2>()

    var idJadwal: String = "-"

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_mhs)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore

        idJadwal = intent.getStringExtra("id").toString()

        rvMahasiswa = findViewById(R.id.rvMahasiswa)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyInfo = findViewById(R.id.emptyInfo)

        rvMahasiswa.setHasFixedSize(true)
        rvMahasiswa.layoutManager = LinearLayoutManager(this)

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
                getAllMhs()
            }
        }

    }

    private fun getAllMhs() {

        db.collection("jadwal").document(idJadwal).collection("list_mhs").get().addOnSuccessListener { result ->
            if(result.isEmpty){
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = true
            } else {
                listMhsMk.clear()
                for (document in result) {
                    listMhsMk.add(
                        MahasiswaModel2(
                            document.id,
                            document.data["idMhs"].toString(),
                            document.data["nilaiuts"].toString().toInt(),
                            document.data["nilaiakhir"].toString(),
                        )
                    )
                }
                val mhsAdapter = MahasiswaAdapter2(this, listMahasiswa, listMhsMk)
                rvMahasiswa.adapter = mhsAdapter
                loadingIndicator.isVisible = false
                emptyInfo.isVisible = false
            }
        }

    }

    fun simpanNilaiUts(id: String, nilai: String,) {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataUts = hashMapOf<String, Any>(
            "nilaiuts" to nilai.toInt(),
        )

        db.collection("jadwal").document(idJadwal).collection("list_mhs").document(id).update(dataUts).addOnCompleteListener {
            dialog.dismiss()
            getAllMhs()
        }

    }

    fun simpanNilaiAkhir(id: String, nilai: String,){

        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.show()

        val dataAkhir = hashMapOf<String, Any>(
            "nilaiakhir" to nilai.uppercase(),
        )

        db.collection("jadwal").document(idJadwal).collection("list_mhs").document(id).update(dataAkhir).addOnCompleteListener {
            dialog.dismiss()
            getAllMhs()
        }

    }

}