package com.example.sia_app.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.admin.DosenActivity
import com.example.sia_app.admin.JadwalActivity
import com.example.sia_app.admin.PertemuanMk
import com.example.sia_app.mhs.InputKrsActivity
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.JadwalModel
import com.example.sia_app.model.KrsModel
import com.example.sia_app.model.MKModel

class JadwalAdapter(val context: Context, val listJadwal: ArrayList<JadwalModel>, val listMK: ArrayList<MKModel>, val listDosen: ArrayList<DosenModel>, val listKrsMhs: ArrayList<KrsModel>, val inputKrs: Boolean, val isPresensi: Boolean) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalAdapter.JadwalViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.jadwal_card, parent, false)
        return JadwalAdapter.JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalAdapter.JadwalViewHolder, position: Int) {
        val currentJadwal = listJadwal[position]

        var namaMk = "-"
        var sksMk = "-"
        var namaDosen = "-"
        var enableKlik = true
        listMK.forEach { mk ->
            if(mk.id == currentJadwal.idMk){
                namaMk = mk.nama
                sksMk = mk.sksMk.toString()
            }
        }

        listDosen.forEach { dosen ->
            if(dosen.id == currentJadwal.idDosen){
                namaDosen = dosen.namaDosen
            }
        }

        if (isPresensi){
            holder.chevronArrow.isVisible = true
            holder.jadwalInfo.isVisible = false
        }

        holder.inisialJadwal.text = namaMk.substring(0, 1)
        holder.namaMk.text = namaMk
        holder.namaDosen.text = "Pengampu: " + namaDosen
        holder.sksMk.text = sksMk + " SKS"
        holder.jadwalMk.text = currentJadwal.hari + ", " + currentJadwal.jam
        holder.ruanganMk.text = "Ruang: " + currentJadwal.ruangan

        if(inputKrs){
            holder.checkMk.isVisible = true
            holder.checkMk.isChecked = currentJadwal.check

            listKrsMhs.forEach { krs ->
                if(krs.idJadwal == currentJadwal.id){
                    holder.checkMk.isEnabled = false
                    holder.checkMk.isChecked = true
                    enableKlik = false
                }
            }

        }

        holder.cardJadwal.setOnClickListener {
            if(inputKrs){
                if(enableKlik){
                    holder.checkMk.isChecked = !holder.checkMk.isChecked
                    currentJadwal.check = holder.checkMk.isChecked
                    val inputKrsActivity: InputKrsActivity = context as InputKrsActivity
                    inputKrsActivity.updateTotalSks(currentJadwal.tarif, holder.checkMk.isChecked)
                }
            } else if(isPresensi){
                val intent = Intent(context, PertemuanMk::class.java)
                intent.putExtra("id", currentJadwal.id)
                context.startActivity(intent)
            }
        }

        holder.checkMk.setOnClickListener {
            currentJadwal.check = holder.checkMk.isChecked
            val inputKrsActivity: InputKrsActivity = context as InputKrsActivity
            inputKrsActivity.updateTotalSks(currentJadwal.tarif, holder.checkMk.isChecked)
        }
    }

    override fun getItemCount(): Int {
        return listJadwal.size
    }

    class JadwalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardJadwal: LinearLayout
        var inisialJadwal: TextView
        var namaMk: TextView
        var namaDosen: TextView
        var sksMk: TextView
        var jadwalMk: TextView
        var ruanganMk: TextView
        var checkMk: CheckBox
        var jadwalInfo: LinearLayout
        var chevronArrow: ImageView

        init {
            cardJadwal = itemView.findViewById(R.id.cardJadwal)
            inisialJadwal = itemView.findViewById(R.id.inisialJadwal)
            namaMk = itemView.findViewById(R.id.namaMk)
            namaDosen = itemView.findViewById(R.id.namaDosen)
            sksMk = itemView.findViewById(R.id.sksMk)
            jadwalMk = itemView.findViewById(R.id.jadwalMk)
            ruanganMk = itemView.findViewById(R.id.ruanganMk)
            checkMk = itemView.findViewById(R.id.checkMk)
            jadwalInfo = itemView.findViewById(R.id.jadwalInfo)
            chevronArrow = itemView.findViewById(R.id.chevronArrow)
        }

    }
}