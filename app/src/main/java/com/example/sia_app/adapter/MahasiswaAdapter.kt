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
import com.example.sia_app.admin.DetailMhsActivity
import com.example.sia_app.admin.MahasiswaActivity
import com.example.sia_app.model.MahasiswaModel

class MahasiswaAdapter(val context: Context, val listMahasiswa: ArrayList<MahasiswaModel>) : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mhs_card, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        val currentMhs = listMahasiswa[position]

        holder.inisialMhs.text = currentMhs.namaMhs.substring(0, 1)
        holder.namaMhs.text = currentMhs.namaMhs
        holder.nimMhs.text = currentMhs.nimMhs

        holder.cardMhs.setOnClickListener {
            val intent = Intent(context, DetailMhsActivity::class.java)
            intent.putExtra("id", currentMhs.id)
            intent.putExtra("nim", currentMhs.nimMhs)
            intent.putExtra("nama", currentMhs.namaMhs)
            intent.putExtra("email", currentMhs.emailMhs)
            intent.putExtra("telepon", currentMhs.teleponMhs)
            intent.putExtra("alamat", currentMhs.alamatMhs)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listMahasiswa.size
    }

    class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardMhs: LinearLayout
        var inisialMhs: TextView
        var namaMhs: TextView
        var nimMhs: TextView

        init {
            cardMhs = itemView.findViewById(R.id.cardMhs)
            inisialMhs = itemView.findViewById(R.id.inisialMhs)
            namaMhs = itemView.findViewById(R.id.namaMhs)
            nimMhs = itemView.findViewById(R.id.nimMhs)
        }

    }
}