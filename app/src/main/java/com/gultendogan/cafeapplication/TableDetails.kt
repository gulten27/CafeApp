package com.gultendogan.cafeapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultendogan.cafeapplication.databinding.ActivityTableDetailsBinding
import kotlin.math.log


class TableDetails : AppCompatActivity() {

    private lateinit var binding: ActivityTableDetailsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: RecyclerDetailsAdapter
    private var siparisler = arrayListOf<Siparis>()

    var siparisFiyati : Float? = null
    var toplam = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val intent = intent
        val selectedTable=intent.getSerializableExtra("table") as Table
        val selectedTableId = intent.getSerializableExtra("id") as Table
        binding.tableNameText.text=selectedTable.tableNo

        firestore=Firebase.firestore

        adapter = RecyclerDetailsAdapter()
        binding.recyclerDetails.adapter=adapter
        binding.recyclerDetails.layoutManager=LinearLayoutManager(this)


        binding.save.setOnClickListener {
            if(binding.siparisText.text.toString().equals("") || binding.siparisText.text.toString().equals("")){
                Toast.makeText(this,"siparişi ve fiyatı girin",Toast.LENGTH_LONG).show()
            }else{
                val siparisText = binding.siparisText.text.toString()
                val fiyatText = binding.fiyatText.text.toString()
                val date = FieldValue.serverTimestamp()
                val tableId = selectedTableId.tableId

                val dataMap = HashMap<String,Any>()
                dataMap.put("siparis",siparisText)
                dataMap.put("fiyat",fiyatText)
                dataMap.put("date",date)
                dataMap.put("tableId",tableId)

                firestore.collection("Siparisler").add(dataMap).addOnSuccessListener {
                    binding.siparisText.setText("")
                    binding.fiyatText.setText("")
                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    binding.siparisText.setText("")
                    binding.fiyatText.setText("")
                }



            }
        }


        firestore.collection("Siparisler")
            .orderBy("date",Query.Direction.ASCENDING)
            .whereEqualTo("tableId",selectedTable.tableId)
            .addSnapshotListener { value, error ->
            if(error != null){
                /*
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
                */
                println(error.localizedMessage)

            }else{
                if(value != null){
                    siparisler.clear()
                    if(value.isEmpty){
                        Toast.makeText(this,"sipariş yok",Toast.LENGTH_LONG).show()
                    }else{
                        val documents = value.documents
                        toplam=0.0f
                        for (document in documents){
                            val siparis=document.get("siparis") as String
                            val fiyat=document.get("fiyat") as String
                            val siparisId = document.get("tableId") as String
                            val siparislerr=Siparis(siparis,fiyat,siparisId)

                            siparisFiyati = document.get("fiyat").toString().toFloatOrNull()
                            toplam =toplam + siparisFiyati!!
                            binding.toplamText.text="Toplam Fiyat : ${toplam}₺"

                            siparisler.add(siparislerr)
                            adapter.siparisler=siparisler
                        }


                        binding.delete.setOnClickListener {
                            for (document in documents){

                                firestore.collection("Siparisler").document(document.id).delete()
                                adapter.notifyDataSetChanged()
                            }
                            binding.toplamText.text="Toplam Fiyat : 0"

                        }

                    }

                    adapter.notifyDataSetChanged()
                }

            }
        }

    }
}



