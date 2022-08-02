package com.example.sia_app.mhs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.sia_app.R
import com.example.sia_app.admin.TambahMahasiswaActivity

class AccountMhsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var inisialMhs: TextView
    private lateinit var nimMhs: TextView
    private lateinit var namaMhs: TextView
    private lateinit var emailMhs: TextView
    private lateinit var teleponMhs: TextView
    private lateinit var alamatMhs: TextView
    private lateinit var editMhs: LinearLayout
    private lateinit var infoProfilMhs: LinearLayout
    private lateinit var infoAyahMhs: LinearLayout
    private lateinit var infoIbuMhs: LinearLayout
    private lateinit var nikMhs: TextView
    private lateinit var tmpLahirMhs: TextView
    private lateinit var tglLahirMhs: TextView
    private lateinit var golDarahMhs: TextView
    private lateinit var aselSekolahMhs: TextView
    private lateinit var nikAyahMhs: TextView
    private lateinit var namaAyahMhs: TextView
    private lateinit var pekerjaanAyahMhs: TextView
    private lateinit var alamatAyahMhs: TextView
    private lateinit var nikIbuMhs: TextView
    private lateinit var namaIbuMhs: TextView
    private lateinit var pekerjaanIbuMhs: TextView
    private lateinit var alamatIbuMhs: TextView

    private var idUser: String = "-"
    private var namaUser: String = "-"
    private var nimUser: String = "-"
    private var emailUser: String = "-"
    private var tlpUser: String = "-"
    private var alamatUser: String = "-"
    private var nik: String = "-"
    private var tempatLahir: String = "-"
    private var tglLahir: String = "-"
    private var golDarah: String = "-"
    private var asalSekolah: String = "-"
    private var nikAyah: String = "-"
    private var namaAyah: String = "-"
    private var pekerjaanAyah: String = "-"
    private var alamatAyah: String = "-"
    private var nikIbu: String = "-"
    private var namaIbu: String = "-"
    private var pekerjaanIbu: String = "-"
    private var alamatIbu: String = "-"
    private var updateProfil: Boolean = false
    private val REQ = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_mhs)

        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        inisialMhs = findViewById(R.id.inisialMhs)
        nimMhs = findViewById(R.id.nimMhs)
        namaMhs = findViewById(R.id.namaMhs)
        emailMhs = findViewById(R.id.emailMhs)
        teleponMhs = findViewById(R.id.teleponMhs)
        alamatMhs = findViewById(R.id.alamatMhs)
        editMhs = findViewById(R.id.editMhs)
        infoProfilMhs = findViewById(R.id.infoProfilMhs)
        infoAyahMhs = findViewById(R.id.infoAyahMhs)
        infoIbuMhs = findViewById(R.id.infoIbuMhs)
        nikMhs = findViewById(R.id.nikMhs)
        tmpLahirMhs = findViewById(R.id.tmpLahirMhs)
        tglLahirMhs = findViewById(R.id.tglLahirMhs)
        golDarahMhs = findViewById(R.id.golDarahMhs)
        aselSekolahMhs = findViewById(R.id.aselSekolahMhs)
        nikAyahMhs = findViewById(R.id.nikAyahMhs)
        namaAyahMhs = findViewById(R.id.namaAyahMhs)
        pekerjaanAyahMhs = findViewById(R.id.pekerjaanAyahMhs)
        alamatAyahMhs = findViewById(R.id.alamatAyahMhs)
        nikIbuMhs = findViewById(R.id.nikIbuMhs)
        namaIbuMhs = findViewById(R.id.namaIbuMhs)
        pekerjaanIbuMhs = findViewById(R.id.pekerjaanIbuMhs)
        alamatIbuMhs = findViewById(R.id.alamatIbuMhs)

        getDataProfilMhs()

        editMhs.setOnClickListener {
            val intent = Intent(this, EditAccountActivity::class.java)
            intent.putExtra("id", idUser)
            intent.putExtra("nim", nimUser)
            intent.putExtra("nama", namaUser)
            intent.putExtra("email", emailUser)
            intent.putExtra("telepon", tlpUser)
            intent.putExtra("alamat", alamatUser)
            intent.putExtra("nikKtpUser", nik)
            intent.putExtra("tempatLahirUser", tempatLahir)
            intent.putExtra("tglLahirUser", tglLahir)
            intent.putExtra("golDarahUser", golDarah)
            intent.putExtra("sekolahUser", asalSekolah)
            intent.putExtra("nikAyah", nikAyah)
            intent.putExtra("namaAyah", namaAyah)
            intent.putExtra("pekerjaanAyah", pekerjaanAyah)
            intent.putExtra("alamatAyah", alamatAyah)
            intent.putExtra("nikIbu", nikIbu)
            intent.putExtra("namaIbu", namaIbu)
            intent.putExtra("pekerjaanIbu", pekerjaanIbu)
            intent.putExtra("alamatIbu", alamatIbu)
            intent.putExtra("updateProfil", updateProfil)
            startActivityForResult(intent, REQ)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == REQ){
            getDataProfilMhs()
        }
    }

    private fun getDataProfilMhs() {

        idUser = sharedPref.getString("idUser", "-").toString()
        namaUser = sharedPref.getString("namaUser", "-").toString()
        nimUser = sharedPref.getString("nikUser", "-").toString()
        emailUser = sharedPref.getString("emailUser", "-").toString()
        tlpUser = sharedPref.getString("teleponUser", "-").toString()
        alamatUser = sharedPref.getString("alamatUser", "-").toString()
        nik = sharedPref.getString("nikKtpUser", "-").toString()
        tempatLahir = sharedPref.getString("tempatLahirUser", "-").toString()
        tglLahir = sharedPref.getString("tglLahirUser", "-").toString()
        golDarah = sharedPref.getString("golDarahUser", "-").toString()
        asalSekolah = sharedPref.getString("sekolahUser", "-").toString()
        nikAyah = sharedPref.getString("nikAyah", "-").toString()
        namaAyah = sharedPref.getString("namaAyah", "-").toString()
        pekerjaanAyah = sharedPref.getString("pekerjaanAyah", "-").toString()
        alamatAyah = sharedPref.getString("alamatAyah", "-").toString()
        nikIbu = sharedPref.getString("nikIbu", "-").toString()
        namaIbu = sharedPref.getString("namaIbu", "-").toString()
        pekerjaanIbu = sharedPref.getString("pekerjaanIbu", "-").toString()
        alamatIbu = sharedPref.getString("alamatIbu", "-").toString()
        updateProfil = sharedPref.getBoolean("updateProfil", false)

        inisialMhs.text = namaUser?.substring(0,1)
        nimMhs.text = nimUser
        namaMhs.text = namaUser
        emailMhs.text = emailUser
        teleponMhs.text = tlpUser
        alamatMhs.text = alamatUser

        if(updateProfil){
            infoProfilMhs.isVisible = true
            infoAyahMhs.isVisible = true
            infoIbuMhs.isVisible = true
            nikMhs.text = nik
            tmpLahirMhs.text = tempatLahir
            tglLahirMhs.text = tglLahir
            golDarahMhs.text = golDarah
            aselSekolahMhs.text = asalSekolah
            nikAyahMhs.text = nikAyah
            namaAyahMhs.text = namaAyah
            pekerjaanAyahMhs.text = pekerjaanAyah
            alamatAyahMhs.text = alamatAyah
            nikIbuMhs.text = nikIbu
            namaIbuMhs.text = namaIbu
            pekerjaanIbuMhs.text = pekerjaanIbu
            alamatIbuMhs.text = alamatIbu
        }

    }
}