package com.gultendogan.cafeapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultendogan.cafeapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var tableArrayList: ArrayList<Table>
    private lateinit var rowAdapter: RecyclerRowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db=Firebase.firestore

        tableArrayList= ArrayList<Table>()

        getData()


        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        rowAdapter= RecyclerRowAdapter(tableArrayList)
        binding.recyclerView.adapter=rowAdapter




    }

    private fun getData(){
        db.collection("Tables").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        for(document in documents){
                            val tableNo = document.get("tableNo") as String
                            val tableId = document.get("id") as String
                            val table=Table(tableNo,tableId)
                            tableArrayList.add(table)
                        }
                        rowAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


}


