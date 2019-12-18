package com.example.colorbuddy.Groups

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.DeleteItemAdapter
import com.example.colorbuddy.classes.Group
import com.example.colorbuddy.classes.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_delete_items.*
import kotlinx.android.synthetic.main.item_row_delete.view.*

class DeleteItemsActivity : AppCompatActivity() {

    private lateinit var paletteView: LinearLayout
    private lateinit var itemsView: RecyclerView
    private lateinit var items: MutableList<Item>
    private lateinit var ref: DatabaseReference
    private lateinit var groupRef: DatabaseReference
    private lateinit var groupName: String
    private lateinit var groupId: String
    private lateinit var selected: MutableList<Boolean>
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_items)

        ref = FirebaseDatabase.getInstance().getReference("Items")
        groupRef = FirebaseDatabase.getInstance().getReference("Groups")
        items = mutableListOf()
        itemsView = findViewById(R.id.recyclerView_delete_items)
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        selected = mutableListOf()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser




        this.title = groupName

        groupRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(i in p0.children){
                        val item = i.getValue(Group::class.java)
                        if(item?.groupName == groupName && item.userID == user?.uid){
                            groupId = i.ref.key!!
                        }

                    }
                }
            }
        })

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    items.clear()
                    var j = 0
                    for(i in p0.children){
                        val item = i.getValue(Item::class.java)
                        if(item?.groupName == groupName){
                            items.add(item)
                            selected.add(false)
                            j++
                        }
                    }
                    itemsView.layoutManager = LinearLayoutManager(applicationContext)
                    itemsView.adapter = DeleteItemAdapter(items)
                }
            }
        })

        btnDeleteItem.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("Do you want to delete item(s)?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, id ->
                deleteItems()
            })
            builder.setNegativeButton("No",{_,_ -> })
            builder.show()
        }

        btnDeleteRoom.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("Do you want to delete Room?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, id ->
                deleteRoom()
            })
            builder.setNegativeButton("No",{_,_ -> })
            builder.show()
        }
    }

    private fun deleteItems(){
        val itemsToDelete = mutableListOf<Item>()
        var i = 0
        for (item in itemsView.children){
            if(item.checkBox.isChecked){
                itemsToDelete.add(items[i])
            }
            i++
        }
        for(item in itemsToDelete){
            val dref = FirebaseDatabase.getInstance().getReference("Items").child(item.itemId)
            dref.removeValue()
        }
    }

    private fun deleteRoom(){
        val deleteGroupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId)
        for(i in items){
            val deleteRef = FirebaseDatabase.getInstance().getReference("Items").child(i.itemId)
            deleteRef.removeValue()
        }
        deleteGroupRef.removeValue()
        finish()

    }
}
