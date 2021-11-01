package com.gultendogan.cafeapplication

import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gultendogan.cafeapplication.databinding.RecyclerRowBinding
import java.util.logging.Handler

class RecyclerRowAdapter(private val tableList: ArrayList<Table>):RecyclerView.Adapter<RecyclerRowAdapter.PostHolder>() {

    var number = 0
    var runnable : Runnable = Runnable {  }
    var handler : android.os.Handler = android.os.Handler(Looper.getMainLooper())

    class PostHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerTableNoText.text=tableList.get(position).tableNo
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,TableDetails::class.java)
            intent.putExtra("table",tableList.get(position))
            intent.putExtra("id",tableList.get(position))
            holder.itemView.context.startActivity(intent)
        }

        /*
        holder.binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                //buton açık ise
                number=0
                runnable = object : Runnable{
                    override fun run() {
                        number=number+1
                        holder.binding.textView.text = "${number} dk."

                        handler.postDelayed(this,60000)
                    }
                }

                handler.post(runnable)

            }else{
                //buton kapalı ise
                holder.binding.textView.text=""
            }
        }
         */
    }

    override fun getItemCount(): Int {
        return tableList.size
    }
}


