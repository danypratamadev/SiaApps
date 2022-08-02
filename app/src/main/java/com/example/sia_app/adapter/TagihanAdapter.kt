package com.example.sia_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sia_app.R
import com.example.sia_app.admin.TagihanActivity
import com.example.sia_app.model.MahasiswaModel
import com.example.sia_app.model.TagihanModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TagihanAdapter(val context: Context, val listTagihan: ArrayList<TagihanModel>, val listMahasiswa: ArrayList<MahasiswaModel>, val tagihanActivity: TagihanActivity, val isValidasi: Boolean) : RecyclerView.Adapter<TagihanAdapter.TagihanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagihanAdapter.TagihanViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.tagihan_card, parent, false)
        return TagihanAdapter.TagihanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagihanAdapter.TagihanViewHolder, position: Int) {
        val currentTg = listTagihan[position]
        val locale = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(locale)

        var namaMhs = "-"
        listMahasiswa.forEach { mhs ->
            if(mhs.id == currentTg.idMhs){
                namaMhs = mhs.namaMhs
            }
        }

        if(isValidasi){
            holder.namaTagihan.text = namaMhs
            holder.jmlTagihan.text = currentTg.nama
            holder.statusTagihan.text = numberFormat.format(currentTg.tagihan)
        } else {
            holder.namaTagihan.text = currentTg.nama
            holder.jmlTagihan.text = numberFormat.format(currentTg.tagihan)
            if(currentTg.status == 0){
                holder.statusTagihan.text = "Belum Lunas"
            } else if(currentTg.status == 1) {
                holder.statusTagihan.text = "Menunggu Validasi"
            } else {
                holder.statusTagihan.text = "Lunas"
            }
        }

        holder.cardTagihan.setOnClickListener {
            if(isValidasi){
                val bottomSheetDialog = BottomSheetDialog(context)
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_validasi)

                val imgBukti = bottomSheetDialog.findViewById<ImageView>(R.id.imgBukti)
                val tolakBayar = bottomSheetDialog.findViewById<Button>(R.id.tolakBayar)
                val terimaBayar = bottomSheetDialog.findViewById<Button>(R.id.terimaBayar)

                Glide.with(context)
                    .load(currentTg.bukti)
                    .error(R.drawable.round_broken_image_black_48dp)
                    .into(imgBukti!!)

                tolakBayar?.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    tagihanActivity.updateValidasiPembayaran(currentTg.id, 0, currentTg.tagihan)
                }

                terimaBayar?.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    tagihanActivity.updateValidasiPembayaran(currentTg.id, 2, 0)
                }

                bottomSheetDialog.show()
            } else {
                val intent = Intent(context, com.example.sia_app.mhs.TagihanActivity::class.java)
                intent.putExtra("id", currentTg.id)
                intent.putExtra("nama", currentTg.nama)
                intent.putExtra("tagihan", currentTg.tagihan)
                intent.putExtra("bukti", currentTg.bukti)
                intent.putExtra("status", currentTg.status)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return listTagihan.size
    }

    class TagihanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardTagihan: LinearLayout
        var namaTagihan: TextView
        var jmlTagihan: TextView
        var statusTagihan: TextView

        init {
            cardTagihan = itemView.findViewById(R.id.cardTagihan)
            namaTagihan = itemView.findViewById(R.id.namaTagihan)
            jmlTagihan = itemView.findViewById(R.id.jmlTagihan)
            statusTagihan = itemView.findViewById(R.id.statusTagihan)
        }

    }
}