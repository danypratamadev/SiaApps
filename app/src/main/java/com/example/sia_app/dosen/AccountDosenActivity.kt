package com.example.sia_app.dosen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.sia_app.R
import com.example.sia_app.admin.TambahDosenActivity

class AccountDosenActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var inisialDosen: TextView
    private lateinit var nikDosen: TextView
    private lateinit var namaDosen: TextView
    private lateinit var emailDosen: TextView
    private lateinit var teleponDosen: TextView
    private lateinit var alamatDosen: TextView
    private lateinit var editDosen: LinearLayout
    private lateinit var infoProfilDosen: LinearLayout
    private lateinit var nikKtpDosenTv: TextView
    private lateinit var tmpLahirDosenTv: TextView
    private lateinit var tglLahirDosenTv: TextView
    private lateinit var golDarahDosenTv: TextView

    private var idUser: String = "-"
    private var namaUser: String = "-"
    private var nikUser: String = "-"
    private var emailUser: String = "-"
    private var tlpUser: String = "-"
    private var alamatUser: String = "-"
    private var nikKtpUser: String = "-"
    private var tempatLahir: String = "-"
    private var tglLahir: String = "-"
    private var golDarah: String = "-"
    private var updateProfil: Boolean = false
    private val REQ = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_dosen)

        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        inisialDosen = findViewById(R.id.inisialDosen)
        nikDosen = findViewById(R.id.nikDosen)
        namaDosen = findViewById(R.id.namaDosen)
        emailDosen = findViewById(R.id.emailDosen)
        teleponDosen = findViewById(R.id.teleponDosen)
        alamatDosen = findViewById(R.id.alamatDosen)
        editDosen = findViewById(R.id.editDosen)
        infoProfilDosen = findViewById(R.id.infoProfilDosen)
        nikKtpDosenTv = findViewById(R.id.nikKtpDosen)
        tmpLahirDosenTv = findViewById(R.id.tmpLahirDosen)
        tglLahirDosenTv = findViewById(R.id.tglLahirDosen)
        golDarahDosenTv = findViewById(R.id.golDarahDosen)

        getDataProfilDosen()

        editDosen.setOnClickListener {
            val intent = Intent(this, EditAccountDosenActivity::class.java)
            intent.putExtra("id", idUser)
            intent.putExtra("nik", nikUser)
            intent.putExtra("nama", namaUser)
            intent.putExtra("email", emailUser)
            intent.putExtra("telepon", tlpUser)
            intent.putExtra("alamat", alamatUser)
            intent.putExtra("nikKtpUser", nikKtpUser)
            intent.putExtra("tempatLahirUser", tempatLahir)
            intent.putExtra("tglLahirUser", tglLahir)
            intent.putExtra("golDarahUser", golDarah)
            startActivityForResult(intent, REQ)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == REQ){
            getDataProfilDosen()
        }
    }

    private fun getDataProfilDosen() {

        idUser = sharedPref.getString("idUser", "-").toString()
        namaUser = sharedPref.getString("namaUser", "-").toString()
        nikUser = sharedPref.getString("nikUser", "-").toString()
        emailUser = sharedPref.getString("emailUser", "-").toString()
        tlpUser = sharedPref.getString("teleponUser", "-").toString()
        alamatUser = sharedPref.getString("alamatUser", "-").toString()
        nikKtpUser = sharedPref.getString("nikKtpUser", "-").toString()
        tempatLahir = sharedPref.getString("tempatLahirUser", "-").toString()
        tglLahir = sharedPref.getString("tglLahirUser", "-").toString()
        golDarah = sharedPref.getString("golDarahUser", "-").toString()
        updateProfil = sharedPref.getBoolean("updateProfil", false)

        inisialDosen.text = namaUser?.substring(0,1)
        nikDosen.text = nikUser
        namaDosen.text = namaUser
        emailDosen.text = emailUser
        teleponDosen.text = tlpUser
        alamatDosen.text = alamatUser

        if(updateProfil){
            infoProfilDosen.isVisible = true
            nikKtpDosenTv.text = nikKtpUser
            tmpLahirDosenTv.text = tempatLahir
            tglLahirDosenTv.text = tglLahir
            golDarahDosenTv.text = golDarah
        }

    }

}