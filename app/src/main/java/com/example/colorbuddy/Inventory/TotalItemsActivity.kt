package com.example.colorbuddy.Inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_total_items.*

class TotalItemsActivity : AppCompatActivity() {

    private lateinit var totalItemsView: RecyclerView
    private lateinit var itemRef: DatabaseReference
    private lateinit var clothesList: MutableList<Item>
    private lateinit var itemList: MutableList<Item>
    private lateinit var items: MutableList<Item>
    private lateinit var listSwitch: Switch
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_items)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        totalItemsView = recyclerView_total_items
        itemRef = FirebaseDatabase.getInstance().getReference("Items")
        items = mutableListOf()
        clothesList = mutableListOf()
        itemList = mutableListOf()
        listSwitch = findViewById(R.id.listSwitch)
        listSwitch.isChecked = false

        titleTextView.text = "Clothes"

        itemRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    items.clear()
                    for(w in p0.children){
                        val item = w.getValue(Item::class.java)
                        if(item!!.itemType == "Clothing"  && item.userID == user?.uid) {
                            clothesList.add(item)
                        }
                        items.add(item)
                    }
                    totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
                    totalItemsView.adapter = ItemAdapter(clothesList)
                }
            }
        })


        listSwitch.setOnCheckedChangeListener { _, _ ->
            if(listSwitch.isChecked){
                loadItems()
            }
            else{
                loadClothes()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        loadClothes()
    }

    private fun loadClothes() {
        val user = mAuth.currentUser
        titleTextView.text = getString(R.string.Clothes)
        clothesList.clear()
        for(g in items){
            if(g.itemType=="Clothing" && g.userID == user?.uid) {
                clothesList.add(g)
            }
        }
        totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
        totalItemsView.adapter = ItemAdapter(clothesList)
    }

    private fun loadItems(){
        val user = mAuth.currentUser
        titleTextView.text = getString(R.string.Items)
        itemList.clear()
        for(g in items){
            if(g.itemType=="Item" && g.userID == user?.uid) {
                itemList.add(g)
            }
        }
        totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
        totalItemsView.adapter = ItemAdapter(itemList)
    }
}
