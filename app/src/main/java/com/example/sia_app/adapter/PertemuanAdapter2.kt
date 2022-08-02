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
import com.example.sia_app.dosen.MkDosenActivity
import com.example.sia_app.mhs.MkMhsActivity
import com.example.sia_app.model.DosenModel
import com.example.sia_app.model.PertemuanModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class PertemuanAdapter2(val context: MkMhsActivity, val listPertemuan: ArrayList<PertemuanModel>, val idUser: String) : RecyclerView.Adapter<PertemuanAdapter2.PertemuanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PertemuanViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pertemuan_card2, parent, false)
        return PertemuanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PertemuanViewHolder, position: Int) {
        val currentPertemuan = listPertemuan[position]
        val dateNow: Date = Calendar.getInstance().time

        var indexNilai: Int = -1
        var indexMhs: Int = 0
        if(currentPertemuan.list_nilaitugas.size > 0){
            currentPertemuan.list_mhs.forEach { mhs ->
                if(mhs == idUser){
                    indexNilai = indexMhs
                }
                indexMhs++

            }
        }

        holder.pertemuanMk.text = currentPertemuan.nama
        if(indexNilai != -1){
            holder.nilaiMhs.text = "Tugas: ${currentPertemuan.list_nilaitugas[indexNilai]}"
        }

        var sudahPresensi = false
        currentPertemuan.list_mhs.forEach { mhs ->
            if(mhs == idUser){
                sudahPresensi = true
                holder.statusKehadiran.text = "Status: Hadir"
                holder.indikatorPresensi.isVisible = true
            }
        }

        if(!sudahPresensi){
            val c = Calendar.getInstance()
            c.time = currentPertemuan.presensidibuat
            c.add(Calendar.MINUTE, currentPertemuan.durasipresensi)
            val berlakuSampai = c.time

            holder.presensiMk.isVisible = currentPertemuan.kodepresensi != 0 && dateNow.before(berlakuSampai)
        }

        holder.presensiMk.setOnClickListener {
            val kodeInput: EditText
            val batal : Button
            val presensi : Button
            val mdialog : Dialog
            mdialog = Dialog(context)
            mdialog.setCancelable(false)
            mdialog.setContentView(R.layout.presensi_layout)
            kodeInput = mdialog.findViewById(R.id.kodeInput)
            batal = mdialog.findViewById(R.id.batal)
            presensi = mdialog.findViewById(R.id.presensi)

            batal.setOnClickListener {
                mdialog.dismiss()
            }

            presensi.setOnClickListener {
                mdialog.dismiss()

                val c = Calendar.getInstance()
                c.time = currentPertemuan.presensidibuat
                c.add(Calendar.MINUTE, currentPertemuan.durasipresensi)
                val berlakuSampai = c.time

                if(dateNow.before(berlakuSampai)){
                    if(kodeInput.text.toString().toInt() == currentPertemuan.kodepresensi){
                        context.presensiMhs(currentPertemuan.id)
                    } else {
                        Toast.makeText(context, "Kode presensi salah!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Waktu presensi telah habis!", Toast.LENGTH_SHORT).show()
                }

            }

            mdialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mdialog.show()
        }

    }

    override fun getItemCount(): Int {
        return listPertemuan.size
    }

    class PertemuanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardPertemuan: LinearLayout
        var pertemuanMk: TextView
        var statusKehadiran: TextView
        var nilaiMhs: TextView
        var presensiMk: Button
        var indikatorPresensi: ImageView

        init {
            cardPertemuan = itemView.findViewById(R.id.cardPertemuan)
            pertemuanMk = itemView.findViewById(R.id.pertemuanMk)
            statusKehadiran = itemView.findViewById(R.id.statusKehadiran)
            nilaiMhs = itemView.findViewById(R.id.nilaiMhs)
            presensiMk = itemView.findViewById(R.id.presensiMk)
            indikatorPresensi = itemView.findViewById(R.id.indikatorPresensi)
        }

    }
}