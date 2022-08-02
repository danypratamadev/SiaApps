package com.example.sia_app.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.admin.DetailDosenActivity
import com.example.sia_app.admin.DosenActivity
import com.example.sia_app.dosen.BeritaAcaraActivity
import com.example.sia_app.dosen.MkDosenActivity
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.PertemuanModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sia_app.model.MahasiswaModel

import com.google.android.material.bottomsheet.BottomSheetDialog




class PertemuanAdapter(val context: Context, val mkDosenActivity: MkDosenActivity, val listPertemuan: ArrayList<PertemuanModel>, val listMahasiswa: ArrayList<MahasiswaModel>, val isDosen: Boolean) : RecyclerView.Adapter<PertemuanAdapter.PertemuanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PertemuanViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pertemuan_card, parent, false)
        return PertemuanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PertemuanViewHolder, position: Int) {
        val currentPertemuan = listPertemuan[position]
        val dateFormated = SimpleDateFormat("HH:mm")
        val dateNow: Date = Calendar.getInstance().time
        val listDurasi = arrayOf("5 Menit", "10 Menit", "15 Menit", "20 Menit", "25 Menit", "30 Menit")

        holder.pertemuanMk.text = currentPertemuan.nama

        if(currentPertemuan.beritaacara != "-"){
            holder.tombolLayout.isVisible = false
            holder.isiBeritaAcara.isVisible = true
            holder.isiBeritaAcara.text = currentPertemuan.beritaacara
            holder.mhsHadirInfo.isVisible = true
            holder.mhsHadir.text = "${currentPertemuan.list_mhs.size} Mahasiswa"
        }

        val c = Calendar.getInstance()
        c.time = currentPertemuan.presensidibuat
        c.add(Calendar.MINUTE, currentPertemuan.durasipresensi)
        val berlakuSampai = c.time

        if(currentPertemuan.kodepresensi != 0 && dateNow.before(berlakuSampai)){
            holder.presensiInfo.isVisible = true
            holder.kodePresensi.text = currentPertemuan.kodepresensi.toString()
            holder.masaBerlaku.text = dateFormated.format(berlakuSampai)
            holder.buatPresensi.isEnabled = false
            holder.buatPresensi.backgroundTintList = context.resources.getColorStateList(R.color.browser_actions_bg_grey)
        } else {
            holder.presensiInfo.isVisible = false
            holder.buatPresensi.isEnabled = true
        }

        holder.lihatMhs.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_mhs)

            val rvMhsHadir = bottomSheetDialog.findViewById<RecyclerView>(R.id.rvMhsHadir)
            rvMhsHadir?.setHasFixedSize(true)
            rvMhsHadir?.layoutManager = LinearLayoutManager(context)

            val mhsAdapter = MahasiswaAdapter3(mkDosenActivity, listMahasiswa, currentPertemuan.list_mhs, currentPertemuan.list_nilaitugas, currentPertemuan.id, isDosen)
            rvMhsHadir?.adapter = mhsAdapter

            bottomSheetDialog.show()
        }

        holder.buatPresensi.setOnClickListener {
            val batal : Button
            val presensi : Button
            val spinDurasi : Spinner
            val mdialog : Dialog
            mdialog = Dialog(context)
            mdialog.setCancelable(false)
            mdialog.setContentView(R.layout.presensi_layout2)
            batal = mdialog.findViewById(R.id.batal)
            presensi = mdialog.findViewById(R.id.presensi)
            spinDurasi = mdialog.findViewById(R.id.spinDurasi)

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, listDurasi)
            spinDurasi.adapter = adapter

            batal.setOnClickListener {

                mdialog.dismiss()

            }

            presensi.setOnClickListener {

                mdialog.dismiss()
                val random = Random.nextInt(999999)
                var kodeTemp = String.format("%06d", random)
                kodeTemp.replace("0", "1")
                val durasiPresensi = spinDurasi.selectedItem.toString()

                mkDosenActivity.setPresensi(currentPertemuan.id, kodeTemp, durasiPresensi)

            }

            mdialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mdialog.show()
        }

        holder.buatBerita.setOnClickListener {
            mkDosenActivity.isiBeritaAcara(currentPertemuan.id)
        }

    }

    override fun getItemCount(): Int {
        return listPertemuan.size
    }

    class PertemuanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardPertemuan: LinearLayout
        var pertemuanMk: TextView
        var isiBeritaAcara: TextView
        var mhsHadirInfo: LinearLayout
        var mhsHadir: TextView
        val lihatMhs: Button
        var presensiInfo: LinearLayout
        var tombolLayout: LinearLayout
        var kodePresensi: TextView
        var masaBerlaku: TextView
        var buatPresensi: Button
        var buatBerita: Button

        init {
            cardPertemuan = itemView.findViewById(R.id.cardPertemuan)
            pertemuanMk = itemView.findViewById(R.id.pertemuanMk)
            isiBeritaAcara = itemView.findViewById(R.id.isiBeritaAcara)
            mhsHadirInfo = itemView.findViewById(R.id.mhsHadirInfo)
            mhsHadir = itemView.findViewById(R.id.mhsHadir)
            lihatMhs = itemView.findViewById(R.id.lihatMhs)
            presensiInfo = itemView.findViewById(R.id.presensiInfo)
            tombolLayout = itemView.findViewById(R.id.tombolLayout)
            kodePresensi = itemView.findViewById(R.id.kodePresensi)
            masaBerlaku = itemView.findViewById(R.id.masaBerlaku)
            buatPresensi = itemView.findViewById(R.id.buatPresensi)
            buatBerita = itemView.findViewById(R.id.buatBerita)
        }

    }
}