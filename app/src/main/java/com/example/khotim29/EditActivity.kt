package com.example.khotim29

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val nama = findViewById<EditText>(R.id.namaEdit)
        val nis = findViewById<EditText>(R.id.nisEdit)
        val jk = findViewById<EditText>(R.id.jkEdit)
        val kelas = findViewById<EditText>(R.id.kelasEdit)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)

        nama.setText(intent?.getStringExtra("nama"))
        nis.setText(intent?.getStringExtra("nis"))
        jk.setText(intent?.getStringExtra("jk"))
        kelas.setText(intent?.getStringExtra("kelas"))

        updateBtn.setOnClickListener() {
            updateSiswa(Siswa(nis.text.toString(), nama.text.toString(), jk.text.toString(), kelas.text.toString()))
        }
    }

    fun updateSiswa(siswa: Siswa) {
        val handler = Handler(Looper.getMainLooper())
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val url = URL("http://192.168.42.167/server_siswa/update.php")
            val client: HttpURLConnection
            val uriBuilder = Uri.Builder()
                .appendQueryParameter("nis", siswa.nis)
                .appendQueryParameter("nama", siswa.nama)
                .appendQueryParameter("jk", siswa.jk)
                .appendQueryParameter("kelas", siswa.kelas)
                .build()
            val params = uriBuilder.toString().replace( "?", "")
            val postData = params.toByteArray(StandardCharsets.UTF_8)

            try {
                client = url.openConnection() as HttpURLConnection
                client.requestMethod = "POST"
                client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                client.setRequestProperty("Accept", "application/json")
                client.doInput = true
                client.doOutput = true
                val dataOutputStream = DataOutputStream(client.outputStream)
                dataOutputStream.write(postData)

                Log.e(ContentValues.TAG, "hasil GET: " + client.responseCode)
                if (client.responseCode == HttpURLConnection.HTTP_OK) {
                    finish()
                }

            } catch (e: java.lang.Exception) {
                Log.e(ContentValues.TAG, "onCreate: ", e)
            }

            handler.post {
                Toast.makeText( this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
            }
        }
    }
}