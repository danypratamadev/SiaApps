package com.example.sia_app.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.admin.DetailDosenActivity
import com.example.sia_app.admin.DosenActivity
import com.example.sia_app.admin.JadwalActivity
import com.example.sia_app.mhs.InputKrsActivity
import com.example.sia_app.mhs.MkMhsActivity
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.JadwalModel
import com.example.sia_app.model.KrsModel
import com.example.sia_app.model.MKModel

class KrsMhsAdapter(val context: Context, val listJadwal: ArrayList<JadwalModel>, val listMK: ArrayList<MKModel>, val listDosen: ArrayList<DosenModel>, val listKrsMhs: ArrayList<KrsModel>) : RecyclerView.Adapter<KrsMhsAdapter.KrsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KrsMhsAdapter.KrsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.krsmk_card, parent, false)
        return KrsMhsAdapter.KrsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KrsMhsAdapter.KrsViewHolder, position: Int) {
        val currentKrs = listKrsMhs[position]

        var idMk = "-"
        var idDosen = "-"
        var namaMk = "-"
        var sksMk = "-"
        var namaDosen = "-"
        var hariMk = "-"
        var jamMk = "-"
        var ruangMk = "-"
        var jenisMk = "-"

        listJadwal.forEach { jadwal ->
            if(jadwal.id == currentKrs.idJadwal){
                idMk = jadwal.idMk
                idDosen = jadwal.idDosen
                hariMk = jadwal.hari
                jamMk = jadwal.jam
                ruangMk = jadwal.ruangan
            }
        }

        listMK.forEach { mk ->
            if(mk.id == idMk){
                namaMk = mk.nama
                sksMk = mk.sksMk.toString()
                jenisMk = mk.jenis
            }
        }

        listDosen.forEach { dosen ->
            if(dosen.id == idDosen){
                namaDosen = dosen.namaDosen
            }
        }

        holder.namaMk.text = namaMk
        holder.jadwalMk.text = "$hariMk, $jamMk"
        holder.ruanganMk.text = "Ruang: $ruangMk"

        holder.cardKrsMhs.setOnClickListener {
            val intent = Intent(context, MkMhsActivity::class.java)
            intent.putExtra("id", currentKrs.id)
            intent.putExtra("idMk", idMk)
            intent.putExtra("idDosen", idDosen)
            intent.putExtra("namaDosen", namaDosen)
            intent.putExtra("idJadwal", currentKrs.idJadwal)
            intent.putExtra("namaMk", namaMk)
            intent.putExtra("sksMk", sksMk)
            intent.putExtra("hariMk", hariMk)
            intent.putExtra("jamMk", jamMk)
            intent.putExtra("ruangMk", ruangMk)
            intent.putExtra("jenisMk", jenisMk)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listKrsMhs.size
    }

    class KrsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardKrsMhs: LinearLayout
        var namaMk: TextView
        var jadwalMk: TextView
        var ruanganMk: TextView

        init {
            cardKrsMhs = itemView.findViewById(R.id.cardKrsMhs)
            namaMk = itemView.findViewById(R.id.namaMk)
            jadwalMk = itemView.findViewById(R.id.jadwalMk)
            ruanganMk = itemView.findViewById(R.id.ruanganMk)
        }

    }
}