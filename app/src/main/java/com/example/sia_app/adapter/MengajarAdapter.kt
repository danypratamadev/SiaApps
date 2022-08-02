package com.example.sia_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.dosen.HomeDosenActivity
import com.example.sia_app.dosen.MkDosenActivity
import com.example.sia_app.mhs.MkMhsActivity
import com.example.sia_app.model.MKModel
import com.example.sia_app.model.MengajarModel

class MengajarAdapter(val context: Context, val listMengajar: ArrayList<MengajarModel>, val listMK: ArrayList<MKModel>,) : RecyclerView.Adapter<MengajarAdapter.MengajarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MengajarAdapter.MengajarViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mengajar_card, parent, false)
        return MengajarAdapter.MengajarViewHolder(view)
    }

    override fun onBindViewHolder(holder: MengajarAdapter.MengajarViewHolder, position: Int) {
        val currentJadwal = listMengajar[position]

        var namaMk = "-"
        var sksMk = "-"
        var jenisMk = "-"
        listMK.forEach { mk ->
            if(mk.id == currentJadwal.idMk){
                namaMk = mk.nama
                sksMk = mk.sksMk.toString()
                jenisMk = mk.jenis
            }
        }

        holder.namaMk.text = namaMk
        holder.jadwalMk.text = currentJadwal.hari + ", " + currentJadwal.jam
        holder.ruanganMk.text = "Ruang: " + currentJadwal.ruangan

        holder.cardMengajar.setOnClickListener {
            val intent = Intent(context, MkDosenActivity::class.java)
            intent.putExtra("id", currentJadwal.id)
            intent.putExtra("idMk", currentJadwal.idMk)
            intent.putExtra("namaMk", namaMk)
            intent.putExtra("sksMk", sksMk)
            intent.putExtra("hariMk", currentJadwal.hari)
            intent.putExtra("jamMk", currentJadwal.jam)
            intent.putExtra("ruangMk", currentJadwal.ruangan)
            intent.putExtra("jenisMk", jenisMk)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listMengajar.size
    }

    class MengajarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardMengajar: LinearLayout
        var namaMk: TextView
        var jadwalMk: TextView
        var ruanganMk: TextView

        init {
            cardMengajar = itemView.findViewById(R.id.cardMengajar)
            namaMk = itemView.findViewById(R.id.namaMk)
            jadwalMk = itemView.findViewById(R.id.jadwalMk)
            ruanganMk = itemView.findViewById(R.id.ruanganMk)
        }

    }
}