package com.example.sia_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sia_app.R
import com.example.sia_app.admin.DetailDosenActivity
import com.example.sia_app.admin.DetailMhsActivity
import com.example.sia_app.admin.MahasiswaActivity
import com.example.sia_app.dosen.MkDosenActivity
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.MahasiswaModel2
import com.google.android.material.bottomsheet.BottomSheetDialog

class MahasiswaAdapter3(val mkDosenActivity: MkDosenActivity, val listMahasiswa: ArrayList<MahasiswaModel>, val listMhsMk: ArrayList<String>, val listNilai: ArrayList<Int>, val idPertemuan: String, val isDosen: Boolean) : RecyclerView.Adapter<MahasiswaAdapter3.MahasiswaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mhs_card3, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        val currentMhs = listMhsMk[position]

        var namaMhs = "-"
        var nimMhs = "-"
        listMahasiswa.forEach { mhs ->
            if(mhs.id == currentMhs){
                namaMhs = mhs.namaMhs
                nimMhs = mhs.nimMhs
            }
        }

        holder.inisialMhs.text = namaMhs.substring(0, 1)
        holder.namaMhs.text = namaMhs
        holder.nimMhs.text = nimMhs
        if(listNilai[position].toString() != "0"){
            holder.infoNilai.isVisible = true
            holder.inputNilaiTugas.isVisible = false
            holder.nilaiTugas.text = listNilai[position].toString()
        } else {
            if(!isDosen){
                holder.inputNilaiTugas.isVisible = false
            }
        }

        holder.inputNilaiTugas.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(mkDosenActivity)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_tugas)

            val namaMhsTv = bottomSheetDialog.findViewById<TextView>(R.id.namaMhs)
            val inputTugas = bottomSheetDialog.findViewById<EditText>(R.id.inputTugas)
            val simpanTugas = bottomSheetDialog.findViewById<Button>(R.id.simpanTugas)

            namaMhsTv?.text = namaMhs
            inputTugas?.setText(listNilai[position].toString())

            simpanTugas?.setOnClickListener {
                if(inputTugas?.text!!.isNotEmpty()){
                    bottomSheetDialog.dismiss()
                    listNilai[position] = inputTugas.text.toString().toInt()
                    mkDosenActivity.inputNilaiTugasMhs(idPertemuan, listNilai)
                } else {
                    Toast.makeText(mkDosenActivity, "Masukkan niali Tugas Harian!", Toast.LENGTH_SHORT).show()
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
        var infoNilai: LinearLayout
        var nilaiTugas: TextView
        var inputNilaiTugas: Button

        init {
            cardMhs = itemView.findViewById(R.id.cardMhs)
            inisialMhs = itemView.findViewById(R.id.inisialMhs)
            namaMhs = itemView.findViewById(R.id.namaMhs)
            nimMhs = itemView.findViewById(R.id.nimMhs)
            infoNilai = itemView.findViewById(R.id.infoNilai)
            nilaiTugas = itemView.findViewById(R.id.nilaiTugas)
            inputNilaiTugas = itemView.findViewById(R.id.inputNilaiTugas)
        }

    }
}