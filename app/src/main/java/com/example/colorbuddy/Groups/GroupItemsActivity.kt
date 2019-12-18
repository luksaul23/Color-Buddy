package com.example.colorbuddy.Groups

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.EXTRA_ITEM_TYPE
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Group
import com.example.colorbuddy.classes.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_group_items.*
import kotlin.random.Random.Default.nextInt

const val EXTRA_GROUP_NAME:String = "EXTRA_GROUP_NAME"

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GroupItemsActivity : AppCompatActivity() {

    private lateinit var paletteLayout: LinearLayout
    private lateinit var itemView: RecyclerView
    private lateinit var items: MutableList<Item>
    private lateinit var itemType: String
    private lateinit var ref: DatabaseReference
    private lateinit var gref: DatabaseReference
    private lateinit var groupName: String
    private lateinit var groupID: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var c1: MutableList<String>
    private lateinit var c2: MutableList<String>
    private lateinit var c3: MutableList<String>
    private lateinit var c4: MutableList<String>
    private lateinit var c5: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_items)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        paletteLayout = findViewById(R.id.paletteLayout)
        itemView = findViewById(R.id.recyclerView_items)
        ref = FirebaseDatabase.getInstance().getReference("Items")
        gref = FirebaseDatabase.getInstance().getReference("Groups")
        items = mutableListOf()
        c1 = mutableListOf()
        c2 = mutableListOf()
        c3 = mutableListOf()
        c4 = mutableListOf()
        c5 = mutableListOf()
        itemType = intent.getStringExtra("EXTRA_ITEM_TYPE")
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")

        this.title = groupName

        gref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for( i in p0.children){
                        val group = i.getValue(Group::class.java)
                        if(group?.groupName == groupName && group.userID == user?.uid){
                            groupID = group.groupId
                        }
                    }
                }
            }

        })

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    items.clear()
                    for(i in p0.children){
                        val item = i.getValue(Item::class.java)
                        if(item?.groupName == groupName && item.userID == user?.uid){
                            items.add(item)
                        }
                    }
                    for(c in items){
                        c1.add(c.c1)
                        c2.add(c.c2)
                        c3.add(c.c3)
                        c4.add(c.c4)
                        c5.add(c.c5)
                    }
                    if(items.isNotEmpty()) {
                        groupColors()
                    }
                    
                    itemView.layoutManager = LinearLayoutManager(applicationContext)
                    itemView.adapter = ItemAdapter(items)
                }
            }
        })



        btnItemAdd.setOnClickListener {
            val intent = Intent(this,NewItemActivity::class.java)
            intent.putExtra(EXTRA_GROUP_NAME,groupName)
            intent.putExtra(EXTRA_ITEM_TYPE,itemType)
            startActivityForResult(intent,1)
        }

        btnItemDelete.setOnClickListener {
            val intent = Intent(this,DeleteItemsActivity::class.java)
            intent.putExtra(EXTRA_GROUP_NAME,groupName)
            startActivityForResult(intent,1)
        }
    }

    private fun groupColors(){

        var gc1 = mostPromColor(c1)
        var gc2 = mostPromColor(c2)
        var gc3 = mostPromColor(c3)
        var gc4 = mostPromColor(c4)
        var gc5 = mostPromColor(c5)

        gref.child(groupID).child("c1").setValue(gc1)
        gref.child(groupID).child("c2").setValue(gc2)
        gref.child(groupID).child("c3").setValue(gc3)
        gref.child(groupID).child("c4").setValue(gc4)
        gref.child(groupID).child("c5").setValue(gc5)


        val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
        )

        if(c1.isNotEmpty())
            groupC1.setBackgroundColor(Color.parseColor(gc1))
            groupC1.layoutParams = param
        if(c2.isNotEmpty())
            groupC2.setBackgroundColor(Color.parseColor(gc2))
            groupC2.layoutParams = param
        if(c3.isNotEmpty())
            groupC3.setBackgroundColor(Color.parseColor(gc3))
            groupC3.layoutParams = param
        if(c4.isNotEmpty())
            groupC4.setBackgroundColor(Color.parseColor(gc4))
            groupC4.layoutParams = param
        if(c5.isNotEmpty())
            groupC5.setBackgroundColor(Color.parseColor(gc5))
            groupC5.layoutParams = param

    }

    private fun mostPromColor(colors: MutableList<String>): String{
        val freqDist = mutableMapOf<String, Int>()

        for( c in colors){
            if(!freqDist.containsKey(c)){
                freqDist.put(c,1)
            }
            var count = freqDist.getValue(c)
            freqDist[c] = count + 1
        }

        val color = freqDist.maxBy { it.value }

        return color!!.key
    }
}
