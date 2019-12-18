package com.example.colorbuddy.Groups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.GroupAdapter
import com.example.colorbuddy.classes.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_groups.*

class GroupsActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var groupsView: RecyclerView
    private lateinit var groupName: TextView
    private lateinit var groupSwitch: Switch
    private lateinit var groupList: MutableList<Group>
    private lateinit var roomList: MutableList<Group>
    private lateinit var wardrobeList: MutableList<Group>
    private lateinit var itemType: String
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        ref = FirebaseDatabase.getInstance().getReference("Groups")
        groupsView = findViewById(R.id.recyclerView_groups)
        groupName = findViewById(R.id.groupTitle)
        groupSwitch = findViewById(R.id.groupSwitch)
        groupList = mutableListOf()
        roomList = mutableListOf()
        wardrobeList = mutableListOf()
        itemType = "Clothing"
        groupSwitch.isChecked = false

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    groupList.clear()
                    wardrobeList.clear()
                    for(g in p0.children){
                        val group = g.getValue(Group::class.java)
                        if(group!!.groupType=="Wardrobe" && group.userID == user?.uid) {
                            wardrobeList.add(group)
                        }
                        groupList.add(group)
                    }
                    groupsView.layoutManager = LinearLayoutManager(applicationContext)
                    groupsView.adapter = GroupAdapter(wardrobeList,itemType)
                }
            }
        })

        groupSwitch.setOnCheckedChangeListener { _, _ ->
            if(groupSwitch.isChecked){
                loadRooms()
            }
            else{
                loadWardrobes()
            }
        }

        btnGroupAdd.setOnClickListener {
            val intent = Intent(this, AddGroupActivity::class.java)
            startActivityForResult(intent,1)
        }
    }

    override fun onResume() {
        super.onResume()
        groupSwitch.isChecked = false
        loadWardrobes()
    }

    private fun loadWardrobes(){
        val user = mAuth.currentUser
        wardrobeList.clear()
        itemType = "Clothing"
        for(g in groupList){
            if(g.groupType=="Wardrobe" && g.userID == user?.uid) {
                wardrobeList.add(g)
            }
        }
        groupsView.layoutManager = LinearLayoutManager(applicationContext)
        groupsView.adapter = GroupAdapter(wardrobeList,itemType)
    }

    private fun loadRooms(){
        val user = mAuth.currentUser
        roomList.clear()
        itemType = "Item"
        for(g in groupList){
            if(g.groupType=="Room" && g.userID == user?.uid) {
                roomList.add(g)
            }
        }
        groupsView.layoutManager = LinearLayoutManager(applicationContext)
        groupsView.adapter = GroupAdapter(roomList,itemType)
    }
}
