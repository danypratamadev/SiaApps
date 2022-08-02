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
import com.example.sia_app.admin.DetailDosenActivity
import com.example.sia_app.admin.DosenActivity
import com.example.sia_app.model.DosenModel

class DosenAdapter(val context: Context, val listDosen: ArrayList<DosenModel>) : RecyclerView.Adapter<DosenAdapter.DosenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DosenViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.dosen_card, parent, false)
        return DosenViewHolder(view)
    }

    override fun onBindViewHolder(holder: DosenViewHolder, position: Int) {
        val currentDosen = listDosen[position]

        holder.inisialDosen.text = currentDosen.namaDosen.substring(0, 1)
        holder.namaDosen.text = currentDosen.namaDosen
        holder.nikDosen.text = currentDosen.nikDosen

        holder.cardDosen.setOnClickListener {
            val intent = Intent(context, DetailDosenActivity::class.java)
            intent.putExtra("id", currentDosen.id)
            intent.putExtra("nik", currentDosen.nikDosen)
            intent.putExtra("nama", currentDosen.namaDosen)
            intent.putExtra("email", currentDosen.emailDosen)
            intent.putExtra("telepon", currentDosen.teleponDosen)
            intent.putExtra("alamat", currentDosen.alamatDosen)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listDosen.size
    }

    class DosenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardDosen: LinearLayout
        var inisialDosen: TextView
        var namaDosen: TextView
        var nikDosen: TextView

        init {
            cardDosen = itemView.findViewById(R.id.cardDosen)
            inisialDosen = itemView.findViewById(R.id.inisialDosen)
            namaDosen = itemView.findViewById(R.id.namaDosen)
            nikDosen = itemView.findViewById(R.id.nikDosen)
        }

    }
}