package com.example.khotim29

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    lateinit var siswaView: RecyclerView
    lateinit var siswaAdapter: SiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTambah = findViewById<Button>(R.id.btnTambah)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        siswaView = findViewById(R.id.rvSiswa)
        siswaView.layoutManager = LinearLayoutManager(this)

        getSiswa()

        btnTambah.setOnClickListener() {
            val intent = Intent( this, TambahActivity::class.java)
            startActivity(intent)
        }

        btnRefresh.setOnClickListener() {
            getSiswa()
        }
    }

    fun getSiswa() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        val list = ArrayList<Siswa>()

        executor.execute {
            val url = URL( "http://192.168.42.167/server_siswa/tampil.php").readText()
            val jsonData = JSONObject(url).getJSONArray( "result")

            for (i in 0 until jsonData.length()) {
                val data = JSONObject(jsonData[i].toString())
                list.add(Siswa(
                    data.getString( "nis"), data.getString( "nama"),
                    data.getString( "jk"), data.getString( "kelas")
                ))
            }

            handler.post {
                siswaAdapter = SiswaAdapter(list)
                siswaView.adapter = siswaAdapter
            }
        }
    }
}