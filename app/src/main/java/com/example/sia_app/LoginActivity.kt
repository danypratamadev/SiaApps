package com.example.sia_app
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sia_app.admin.HomeAdminActivity
import com.example.sia_app.dosen.HomeDosenActivity
import com.example.sia_app.mhs.HomeMhsActivity
import com.google.common.collect.Maps
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var login : Button

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        db = Firebase.firestore
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)

        login.setOnClickListener {
            email.clearFocus()
            password.clearFocus()
            try {
                val keyInput = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                keyInput.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                Log.e("Error", "Something went wrong!")
            }
            dialog = Dialog(this)
            dialog.setContentView(R.layout.loading_dialog)
            dialog.show()
            checkUsers()
        }

    }

    private fun checkUsers(){

        db.collection("users").whereEqualTo("email", email.text.toString()).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                dialog.dismiss()
                email.text.clear()
                password.text.clear()
                email.requestFocus()
                email.error = "Email tidak terdaftar!"
                Toast.makeText(this, "Email anda tidak terdaftar!", Toast.LENGTH_SHORT).show()
            } else {
                for (document in result) {
                    if(document.data["password"].toString() == password.text.toString()){
                        if(document.data["setupAuth"] == true){
                            loginAuthentication(document)
                        } else {
                            regisAndLoginAuthentication(document)
                        }
                    } else {
                        dialog.dismiss()
                        password.error = "Password salah!"
                        Toast.makeText(this, "Password anda salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun loginAuthentication(document: DocumentSnapshot){
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    dialog.dismiss()
                    val user = auth.currentUser
                    var roleUser = document.data!!["role"].toString().toInt()
                    var nikUser = "-"
                    nikUser = if(roleUser == 30){
                        document.data!!["nim"].toString()
                    } else {
                        document.data!!["nik"].toString()
                    }
                    editor.putString("uidUser", user?.uid)
                    editor.putString("idUser", document.id)
                    editor.putInt("roleUser", document.data!!["role"].toString().toInt())
                    editor.putString("nikUser", nikUser)
                    editor.putString("namaUser", document.data!!["nama"].toString())
                    editor.putString("emailUser", document.data!!["email"].toString())
                    editor.putString("teleponUser", document.data!!["telepon"].toString())
                    editor.putString("alamatUser", document.data!!["alamat"].toString())
                    if(roleUser == 30){
                        val tglLahir: Timestamp = document.data!!["tgllahir"] as Timestamp
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        editor.putString("nikKtpUser", document.data!!["nik"].toString())
                        editor.putString("tempatLahirUser", document.data!!["tempatlahir"].toString())
                        editor.putString("tglLahirUser", dateFormat.format(tglLahir.toDate()))
                        editor.putString("golDarahUser", document.data!!["goldarah"].toString())
                        editor.putString("sekolahUser", document.data!!["asalsekolah"].toString())
                        editor.putString("nikAyah", document.data!!["nikayah"].toString())
                        editor.putString("namaAyah", document.data!!["namaayah"].toString())
                        editor.putString("pekerjaanAyah", document.data!!["pekerjaanayah"].toString())
                        editor.putString("alamatAyah", document.data!!["alamatayah"].toString())
                        editor.putString("nikIbu", document.data!!["nikibu"].toString())
                        editor.putString("namaIbu", document.data!!["namaibu"].toString())
                        editor.putString("pekerjaanIbu", document.data!!["pekerjaanibu"].toString())
                        editor.putString("alamatIbu", document.data!!["alamatibu"].toString())
                        editor.putBoolean("updateProfil", document.data!!["updateprofil"] as Boolean)
                    } else if(roleUser == 20){
                        val tglLahir: Timestamp = document.data!!["tgllahir"] as Timestamp
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        editor.putString("nikKtpUser", document.data!!["nikktp"].toString())
                        editor.putString("tempatLahirUser", document.data!!["tempatlahir"].toString())
                        editor.putString("tglLahirUser", dateFormat.format(tglLahir.toDate()))
                        editor.putString("golDarahUser", document.data!!["goldarah"].toString())
                        editor.putBoolean("updateProfil", document.data!!["updateprofil"] as Boolean)
                    }
                    editor.apply()
                    jumpToHomePage(document.data!!["role"].toString().toInt())
                } else {
                    dialog.dismiss()
                    Toast.makeText(baseContext, "Tidak dapat login aplikasi saat ini. Coba lagi beberapa saat kedepan", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun regisAndLoginAuthentication(document: DocumentSnapshot){
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    dialog.dismiss()
                    val user = auth.currentUser
                    var roleUser = document.data!!["role"].toString().toInt()
                    var nikUser = "-"
                    nikUser = if(roleUser == 30){
                        document.data!!["nim"].toString()
                    } else {
                        document.data!!["nik"].toString()
                    }
                    editor.putString("uidUser", user?.uid)
                    editor.putString("idUser", document.id)
                    editor.putInt("roleUser", document.data!!["role"].toString().toInt())
                    editor.putString("nikUser", nikUser)
                    editor.putString("namaUser", document.data!!["nama"].toString())
                    editor.putString("emailUser", document.data!!["email"].toString())
                    editor.putString("teleponUser", document.data!!["telepon"].toString())
                    editor.putString("alamatUser", document.data!!["alamat"].toString())
                    if(roleUser == 30){
                        val tglLahir: Timestamp = document.data!!["tgllahir"] as Timestamp
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        editor.putString("nikKtpUser", document.data!!["nik"].toString())
                        editor.putString("tempatLahirUser", document.data!!["tempatlahir"].toString())
                        editor.putString("tglLahirUser", dateFormat.format(tglLahir.toDate()))
                        editor.putString("golDarahUser", document.data!!["goldarah"].toString())
                        editor.putString("sekolahUser", document.data!!["asalsekolah"].toString())
                        editor.putString("nikAyah", document.data!!["nikayah"].toString())
                        editor.putString("namaAyah", document.data!!["namaayah"].toString())
                        editor.putString("pekerjaanAyah", document.data!!["pekerjaanayah"].toString())
                        editor.putString("alamatAyah", document.data!!["alamatayah"].toString())
                        editor.putString("nikIbu", document.data!!["nikibu"].toString())
                        editor.putString("namaIbu", document.data!!["namaibu"].toString())
                        editor.putString("pekerjaanIbu", document.data!!["pekerjaanibu"].toString())
                        editor.putString("alamatIbu", document.data!!["alamatibu"].toString())
                        editor.putBoolean("updateProfil", document.data!!["updateprofil"] as Boolean)
                    } else if(roleUser == 20){
                        val tglLahir: Timestamp = document.data!!["tgllahir"] as Timestamp
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        editor.putString("nikKtpUser", document.data!!["nikktp"].toString())
                        editor.putString("tempatLahirUser", document.data!!["tempatlahir"].toString())
                        editor.putString("tglLahirUser", dateFormat.format(tglLahir.toDate()))
                        editor.putString("golDarahUser", document.data!!["goldarah"].toString())
                        editor.putBoolean("updateProfil", document.data!!["updateprofil"] as Boolean)
                    }
                    editor.apply()
                    updateSetupAuth(document.id)
                    jumpToHomePage(document.data!!["role"].toString().toInt())
                } else {
                    dialog.dismiss()
                    Toast.makeText(baseContext, "Tidak dapat login aplikasi saat ini. Coba lagi beberapa saat kedepan", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun updateSetupAuth(userId: String) {
        db.collection("users").document(userId).update(mapOf(
            "setupAuth" to true
        ))
    }

    private fun jumpToHomePage(role: Int){
        when (role) {
            10 -> {
                startActivity(Intent(this, HomeAdminActivity::class.java))
            }
            20 -> {
                startActivity(Intent(this, HomeDosenActivity::class.java))
            }
            else -> {
                startActivity(Intent(this, HomeMhsActivity::class.java))
            }
        }
        finish()
    }
}