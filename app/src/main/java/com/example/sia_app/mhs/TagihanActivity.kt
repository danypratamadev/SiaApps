package com.example.sia_app.mhs

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.sia_app.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.NumberFormat
import java.util.*

class TagihanActivity : AppCompatActivity() {

    private lateinit var namaTg: TextView
    private lateinit var jmlTg: TextView
    private lateinit var statusTg: TextView
    private lateinit var chooseFile: LinearLayout
    private lateinit var imgIcon: ImageView
    private lateinit var imgChoose: ImageView
    private lateinit var captionImg: TextView
    private lateinit var bayarTg: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var sharedPref: SharedPreferences

    private lateinit var dialog : Dialog

    private lateinit var nimUser: String
    private var status = 0

    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var uriDownload = "-"

    private val REQ = 100
    val permission = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagihan)

        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        storage = Firebase.storage
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)

        nimUser = sharedPref.getString("nikUser", "-").toString()

        val id = intent.getStringExtra("id")
        val nama = intent.getStringExtra("nama")
        val tagihan = intent.getIntExtra("tagihan", 0)
        val bukti = intent.getStringExtra("bukti")
        status = intent.getIntExtra("status", 0)

        namaTg = findViewById(R.id.namaTg)
        jmlTg = findViewById(R.id.jmlTg)
        statusTg = findViewById(R.id.statusTg)
        chooseFile = findViewById(R.id.chooseFile)
        imgIcon = findViewById(R.id.imgIcon)
        imgChoose = findViewById(R.id.imgChoose)
        captionImg = findViewById(R.id.captionImg)
        bayarTg = findViewById(R.id.bayarTg)

        namaTg.text = nama
        jmlTg.text = numberFormat.format(tagihan)
        statusTg.text = when (status) {
            2 -> {
                "PEMBAYARAN LUNAS"
            }
            1 -> {
                "MENUNGGU\nVALIDASI"
            }
            else -> {
                "BELUM LUNAS"
            }
        }

        statusTg.setTextColor(
            when (status) {
                2 -> {
                    Color.parseColor("#00695C")
                }
                1 -> {
                    Color.parseColor("#EF6C00")
                }
                else -> {
                    Color.parseColor("#C62828")
                }
            }
        )

        if(status == 1 || status == 2){
            imgChoose.isVisible = true
            captionImg.text = "Bukti Pembayaran Saya"
            imgIcon.isVisible = false
            bayarTg.isVisible = false
            Glide.with(this)
                .load(bukti)
                .error(R.drawable.round_broken_image_black_48dp)
                .into(imgChoose)
        }

        chooseFile.setOnClickListener {
            if(status == 0){
                if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_GRANTED){
                    val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, pickImage)
                } else {
                    ActivityCompat.requestPermissions(this, permission, REQ)
                }
            }
        }

        bayarTg.setOnClickListener {
            if(imageUri != null){
                dialog.show()
                uploadImageToFirebase(id!!)
            } else {
                Toast.makeText(this, "Pilih bukti pembayaran terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor: Cursor? = contentResolver.query(
                imageUri!!,
                filePathColumn, null, null, null
            )
            cursor?.moveToFirst()

            val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
            val picturePath: String = cursor!!.getString(columnIndex)
            cursor?.close()

            imgChoose.setImageBitmap(ExifUtil.rotateBitmap(picturePath, BitmapFactory.decodeFile(picturePath)))
            imgChoose.isVisible = true
            captionImg.text = picturePath.split("/").last().toString()
            imgIcon.isVisible = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQ){
            if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_GRANTED){
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            } else {
                ActivityCompat.requestPermissions(this, permission, REQ)
            }
        }
    }

    private fun uploadImageToFirebase(id: String) {

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor? = contentResolver.query(
            imageUri!!,
            filePathColumn, null, null, null
        )
        cursor?.moveToFirst()

        val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
        val picturePath: String = cursor!!.getString(columnIndex)
        cursor?.close()

        val fileName = picturePath.split("/").last().toString()

        val refStorage = storage.reference.child("Pembayaran/$nimUser/$fileName")

        refStorage.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                uriDownload = it.toString()

                val dataImgae = hashMapOf<String, Any>(
                    "bukti" to uriDownload,
                    "status" to 1
                )

                db.collection("tagihan").document(id).update(dataImgae).addOnCompleteListener {
                    dialog.dismiss()

                    status = 1
                    captionImg.text = "Bukti Pembayaran Saya"
                    statusTg.text = "MENUNGGU\nVALIDASI"
                    statusTg.setTextColor(Color.parseColor("#EF6C00"))
                    bayarTg.isVisible = false

                }
            }
        }.addOnFailureListener { e ->
            print(e.message)
        }

    }
}