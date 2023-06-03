package com.example.khotim29

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import java.util.concurrent.Executors

class SiswaAdapter(private val data: ArrayList<Siswa>?): RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {
    class SiswaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nama = itemView.findViewById<TextView>(R.id.NamaSiswa)
        private val nis = itemView.findViewById<TextView>(R.id.NisSiswa)
        private val jk = itemView.findViewById<TextView>(R.id.JkSiswa)
        private val kelas = itemView.findViewById<TextView>(R.id.KelasSiswa)
        private val editBtn = itemView.findViewById<TextView>(R.id.btnEdit)
        private val hapusBtn = itemView.findViewById<TextView>(R.id.btnHapus)
        fun bind(get: Siswa) {
            nama.text = get.nama
            nis.text = get.nis
            jk.text = get.jk
            kelas.text = get.kelas

            editBtn.setOnClickListener() {
                val intent = Intent(itemView.context, EditActivity::class.java)
                intent.putExtra("nama", get.nama)
                intent.putExtra("nis", get.nis)
                intent.putExtra("jk", get.jk)
                intent.putExtra("kelas", get.kelas)
                itemView.context.startActivity(intent)
            }

            hapusBtn.setOnClickListener() {
                val dialogBuilder = AlertDialog.Builder(itemView.context)
                dialogBuilder.setTitle("Hapus Data")
                dialogBuilder.setMessage("Hapus Siswa "+get.nama)
                dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
                    val executor = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())

                    executor.execute {
                        val url = URL("http://192.168.42.167/server_siswa/tampil.php?nis=" + get.nis).readText()

                        handler.post {
                            Toast.makeText(itemView.context, url, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->

                })
                dialogBuilder.create()
                dialogBuilder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        return SiswaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_siswa, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return  data?.size ?: 0
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        holder.bind( data?.get(position) ?: Siswa( "", "", "", ""))
    }
}