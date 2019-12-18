package com.example.colorbuddy.Groups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_group.*

class AddGroupActivity : AppCompatActivity() {

    private lateinit var groupName: EditText
    private lateinit var groupTypeSwitch: Switch
    private lateinit var ref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Groups")
        groupName = findViewById(R.id.addGroupName)
        groupTypeSwitch = findViewById(R.id.groupTypeSwitch)

        btnAddGroup.setOnClickListener {
            addGroup(ref,groupName)
        }
    }

    private fun addGroup(ref: DatabaseReference,name: EditText){
        var mName = name.text
        val user = mAuth.currentUser
        lateinit var type: String
        if(mName.isEmpty()){
            name.error = "Please enter the name of a Group"
            return
        }

        if(groupTypeSwitch.isChecked){
            type = "Room"
        }
        else{
            type = "Wardrobe"
        }

        var gID = ref.push().key
        var group = Group(user!!.uid,gID.toString(),mName.toString(),type,"","","","","")
        ref.child(gID.toString()).setValue(group).addOnCompleteListener {
            Toast.makeText(applicationContext, "Group saved successfully", Toast.LENGTH_LONG).show()
        }
        finish()
    }
}
