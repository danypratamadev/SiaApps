package com.example.sia_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.admin.DetailDosenActivity
import com.example.sia_app.admin.DetailMhsActivity
import com.example.sia_app.admin.MahasiswaActivity
import com.example.sia_app.dosen.DaftarMhsActivity
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.MahasiswaModel2
import com.google.android.material.bottomsheet.BottomSheetDialog

class MahasiswaAdapter2(val context: DaftarMhsActivity, val listMahasiswa: ArrayList<MahasiswaModel>, val listMhsMk: ArrayList<MahasiswaModel2>) : RecyclerView.Adapter<MahasiswaAdapter2.MahasiswaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mhs_card2, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        val currentMhs = listMhsMk[position]

        var namaMhs = "-"
        var nimMhs = "-"
        listMahasiswa.forEach { mhs ->
            if(mhs.id == currentMhs.idMhs){
                namaMhs = mhs.namaMhs
                nimMhs = mhs.nimMhs
            }
        }

        holder.inisialMhs.text = namaMhs.substring(0, 1)
        holder.namaMhs.text = namaMhs
        holder.nimMhs.text = nimMhs
        holder.nilaiUts.text = "Nilai UTS: ${currentMhs.nilaiuts}"
        holder.nilaiUas.text = "Nilai Akhir: ${currentMhs.nilaiakhir}"

        holder.cardMhs.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_nilai)

            val namaMhsTv = bottomSheetDialog.findViewById<TextView>(R.id.namaMhs)
            val inputUts = bottomSheetDialog.findViewById<EditText>(R.id.inputUts)
            val inputAkhir = bottomSheetDialog.findViewById<EditText>(R.id.inputAkhir)
            val simpanUts = bottomSheetDialog.findViewById<Button>(R.id.simpanUts)
            val simpanAkhir = bottomSheetDialog.findViewById<Button>(R.id.simpanAkhir)

            namaMhsTv?.text = namaMhs
            inputUts?.setText(currentMhs.nilaiuts.toString())
            inputAkhir?.setText(currentMhs.nilaiakhir)

            simpanUts?.setOnClickListener {
                if(inputUts?.text!!.isNotEmpty()){
                    bottomSheetDialog.dismiss()
                    context.simpanNilaiUts(currentMhs.id, inputUts?.text.toString())
                } else {
                    Toast.makeText(context, "Masukkan niali UTS!", Toast.LENGTH_SHORT).show()
                }
            }

            simpanAkhir?.setOnClickListener {
                if(inputAkhir?.text!!.isNotEmpty()){
                    bottomSheetDialog.dismiss()
                    context.simpanNilaiAkhir(currentMhs.id, inputAkhir?.text.toString())
                } else {
                    Toast.makeText(context, "Masukkan niali Akhir!", Toast.LENGTH_SHORT).show()
                }
            }

            bottomSheetDialog.show()

        }

    }

    override fun getItemCount(): Int {
        return listMhsMk.size
    }

    class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardMhs: LinearLayout
        var inisialMhs: TextView
        var namaMhs: TextView
        var nimMhs: TextView
        var nilaiUts: TextView
        var nilaiUas: TextView

        init {
            cardMhs = itemView.findViewById(R.id.cardMhs)
            inisialMhs = itemView.findViewById(R.id.inisialMhs)
            namaMhs = itemView.findViewById(R.id.namaMhs)
            nimMhs = itemView.findViewById(R.id.nimMhs)
            nilaiUts = itemView.findViewById(R.id.nilaiUts)
            nilaiUas = itemView.findViewById(R.id.nilaiUas)
        }

    }


}