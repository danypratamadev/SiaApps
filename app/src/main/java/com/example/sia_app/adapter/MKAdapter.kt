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
import com.example.sia_app.admin.DetailMhsActivity
import com.example.sia_app.admin.DetailMkActivity
import com.example.sia_app.admin.MataKuliahActivity
import com.example.sia_app.model.MKModel

class MKAdapter(val context: Context, val listMK: ArrayList<MKModel>) : RecyclerView.Adapter<MKAdapter.MKViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MKAdapter.MKViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mk_card, parent, false)
        return MKAdapter.MKViewHolder(view)
    }

    override fun onBindViewHolder(holder: MKAdapter.MKViewHolder, position: Int) {
        val currentMk = listMK[position]

        holder.inisialMk.text = currentMk.nama.substring(0, 1)
        holder.namaMk.text = currentMk.nama
        holder.kodeMk.text = currentMk.kdMk
        holder.jenisMk.text = currentMk.jenis.uppercase()

        holder.cardMk.setOnClickListener {
            val intent = Intent(context, DetailMkActivity::class.java)
            intent.putExtra("id", currentMk.id)
            intent.putExtra("kdmk", currentMk.kdMk)
            intent.putExtra("nama", currentMk.nama)
            intent.putExtra("sks", currentMk.sksMk)
            intent.putExtra("jenis", currentMk.jenis)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listMK.size
    }

    class MKViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardMk: LinearLayout
        var inisialMk: TextView
        var namaMk: TextView
        var kodeMk: TextView
        var jenisMk: TextView

        init {
            cardMk = itemView.findViewById(R.id.cardMk)
            inisialMk = itemView.findViewById(R.id.inisialMk)
            namaMk = itemView.findViewById(R.id.namaMk)
            kodeMk = itemView.findViewById(R.id.kodeMk)
            jenisMk = itemView.findViewById(R.id.jenisMk)
        }

    }
}