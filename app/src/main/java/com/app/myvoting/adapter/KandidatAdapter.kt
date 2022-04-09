package com.app.myvoting.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.myvoting.DetailKandidatActivity
import com.app.myvoting.retrofit.response.kandidat.ModelResult
import com.app.myvoting.R
import com.app.myvoting.databinding.ItemKandidatBinding
import com.bumptech.glide.Glide

class KandidatAdapter(private val context: Context, private val kandidat: List<ModelResult>, private val id: Int, private val noTps: String) : RecyclerView.Adapter<KandidatAdapter.KandidatViewHolder>(){
//    private var img = listOf<Int>(R.drawable.one, R.drawable.two, R.drawable.tree,)
    inner class KandidatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemKandidatBinding.bind(itemView)
        var baseUrl = "http://voting.kpupangkep.my.id/"
        fun bindView(kandidat: ModelResult, id: Int, position: Int, noTps: String){
            var img = baseUrl+kandidat.img
//            binding.imgItemPhoto.setImageResource(img[position])
            Glide.with(context)
                .load(img)
                .into(binding.imgItemPhoto)
            binding.tvItemName.text = kandidat.namaKandidat
            binding.tvNoUrut.text = "Nomor Urut: ${kandidat.noUrut}"
            itemView.setOnClickListener{
                Intent(itemView.context, DetailKandidatActivity::class.java).also {
                    it.putExtra(DetailKandidatActivity.KANDIDAT_ID, kandidat.id)
                    it.putExtra(DetailKandidatActivity.NAMA_KANDIDAT, kandidat.namaKandidat)
                    it.putExtra(DetailKandidatActivity.NO_URUT, kandidat.noUrut)
                    it.putExtra(DetailKandidatActivity.VISI_MISI, kandidat.visiMisi)
                    it.putExtra("ID", id)
                    it.putExtra("NO_TPS", noTps)
                    it.putExtra(DetailKandidatActivity.IMAGE, img)
                    itemView.context.startActivity(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KandidatViewHolder {
        return KandidatViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_kandidat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: KandidatViewHolder, position: Int) {
        holder.bindView(kandidat[position], id, position, noTps)
    }

    override fun getItemCount(): Int = kandidat.size
}
