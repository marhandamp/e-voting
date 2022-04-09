package com.app.myvoting

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.app.myvoting.adapter.KandidatAdapter
import com.app.myvoting.retrofit.RetrofitClient
import com.app.myvoting.databinding.ActivityDetailKandidatBinding
import com.app.myvoting.databinding.DialogItemBinding
import com.app.myvoting.retrofit.response.hasil.ModelResultHasilData
import com.app.myvoting.retrofit.response.kandidat.ModelResult
import com.app.myvoting.retrofit.response.kandidat.ModelResultData
import com.app.myvoting.retrofit.response.pemilihan.UserRequest
import com.app.myvoting.retrofit.response.pemilihan.UserResponse
import com.app.myvoting.retrofit.response.tps.ModelResultTps
import com.app.myvoting.retrofit.response.tps.ModelResultTpsData
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class DetailKandidatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKandidatBinding

    private var kandidatId: Int? = null
    private lateinit var namaKandidat: String
    private var noUrut: Int? = null
    private lateinit var visiMisi: String
    private var id by Delegates.notNull<Int>()
    private var img: String = ""
    private var checkId = ""
    private var noTps = ""
    private val listTps = ArrayList<ModelResultTps>()
//    private lateinit var getIdkodeTps : TextInputEditText

    companion object {
        const val KANDIDAT_ID = "kandidat_id"
        const val NAMA_KANDIDAT = "nama_kandidat"
        const val NO_URUT = "no_urut"
        const val VISI_MISI = "visi_misi"
        const val IMAGE = "image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKandidatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar.also {
            it?.setDisplayHomeAsUpEnabled(true)
            it?.setDisplayShowHomeEnabled(true)
        }

        kandidatId = intent.getIntExtra(KANDIDAT_ID, 0)!!
        namaKandidat = intent.getStringExtra(NAMA_KANDIDAT)!!
        noUrut = intent.getIntExtra(NO_URUT, 0)
        visiMisi = intent.getStringExtra(VISI_MISI)!!
        id = intent.getIntExtra("ID", 0)
        noTps = intent.getStringExtra("NO_TPS")!!
        img = intent.getStringExtra(IMAGE)!!

        showDataKandidat()
        getHasil()
        getTps()

        binding.btnVote.setOnClickListener {
            Log.d("Kode Tps", listTps.toString())
            val view = DialogItemBinding.inflate(layoutInflater)

                if (id.toString() == checkId){
                    Toast.makeText(this, "Anda Sudah Memilih", Toast.LENGTH_SHORT).show()
                } else {
                    val alertDialog = AlertDialog.Builder(this@DetailKandidatActivity)
                    alertDialog.setView(view.root)
                    alertDialog.setTitle("Kode TPS")
                    alertDialog.setCancelable(false)
                    alertDialog.setPositiveButton("Vote", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val kodeTps = view.tiEdtKodeTps.text.toString().trim()
                            var kodeBenar = ""
                            var ab = ""
                            for(a in listTps.indices){
                                if (kodeTps == listTps[a].kodeTps){
                                    kodeBenar = kodeTps
                                    ab = listTps[a].tps
                                }
                            }

                            if(noTps == ab){
                                if (kodeBenar == kodeTps){

                                    postPemilihan()

                                    Handler().postDelayed({
                                        onBackPressed()
                                    }, 1000)

//                                Toast.makeText(this@DetailKandidatActivity, "$kodeBenar $kodeTps", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@DetailKandidatActivity, "Kode Tps Salah", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@DetailKandidatActivity, "Anda Terdaftar Pada Tps $noTps", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                    alertDialog.setNegativeButton("Keluar", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface, p1: Int) {
                            p0.dismiss()
                        }
                    })
                    alertDialog.create()
                    alertDialog.show()
                }
        }
    }

    private fun showDataKandidat(){
        Glide.with(this)
            .load(img)
            .into(binding.imageView2)
        binding.tvNamaCalon.text = "Nama Calon: $namaKandidat"
        binding.tvNoUrutDetail.text = "No Urut: $noUrut"
        binding.tvVisiMisi.text = "$visiMisi"
    }

    private fun postPemilihan() {
        val uR = UserRequest()
        uR.kandidat_id = kandidatId.toString()
        uR.pemilih_id = id.toString()

        RetrofitClient.instance.postPemilihan(uR
        ).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful){
                    Snackbar.make(binding.root, "Voting Berhasil", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Anda Yang Tidak Beres", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Snackbar.make(binding.root, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun getHasil(){
        RetrofitClient.instance.getHasil().enqueue(object : Callback<ModelResultHasilData> {
            override fun onResponse(
                call: Call<ModelResultHasilData>,
                response: Response<ModelResultHasilData>
            ) {
                val size = response.body()?.modelResultHasil!!.size
                for (i in 0 until size){
                    val data = response.body()!!.modelResultHasil[i].pemilihId
                    if (data == id.toString()){
                        checkId = data
                    }
                }
            }

            override fun onFailure(call: Call<ModelResultHasilData>, t: Throwable) {
                Toast.makeText(this@DetailKandidatActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTps(){
        RetrofitClient.instance.getTps().enqueue(object : Callback<ModelResultTpsData> {
            override fun onResponse(
                call: Call<ModelResultTpsData>,
                response: Response<ModelResultTpsData>
            ) {

                if (response.isSuccessful){
                    val size = response.body()?.modelResultTps!!.size
                    for (i in 0 until size){
                        val data = response.body()!!.modelResultTps[i]

                        listTps.add(data)
                    }
                } else {
                    Toast.makeText(this@DetailKandidatActivity, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelResultTpsData>, t: Throwable) {
                Log.d("Ica-Error", t.message.toString())
                Toast.makeText(this@DetailKandidatActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}