package com.gultendogan.cafeapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class RecyclerDetailsAdapter : RecyclerView.Adapter<RecyclerDetailsAdapter.SiparisHolder>() {

    class SiparisHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Siparis>(){
        override fun areItemsTheSame(oldItem: Siparis, newItem: Siparis): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Siparis, newItem: Siparis): Boolean {
            return oldItem==newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var siparisler : List<Siparis>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiparisHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_details_row,parent,false)
        return SiparisHolder(view)
    }

    override fun onBindViewHolder(holder: SiparisHolder, position: Int) {
        val siparisText = holder.itemView.findViewById<TextView>(R.id.siparisText)
        siparisText.text = "${siparisler.get(position).siparis}"
        val fiyatText = holder.itemView.findViewById<TextView>(R.id.fiyatText)
        fiyatText.text="${siparisler.get(position).fiyat}"
    }

    override fun getItemCount(): Int {
        return siparisler.size
    }
}