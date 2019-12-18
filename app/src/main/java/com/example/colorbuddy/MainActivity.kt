package com.example.colorbuddy

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.colorbuddy.Data.ImageData
import com.example.colorbuddy.Groups.GroupsActivity
import com.example.colorbuddy.Inventory.TotalItemsActivity
import com.example.colorbuddy.adapters.WheelImageAdapter
import com.example.colorbuddy.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import github.hellocsl.cursorwheel.CursorWheelLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException



class MainActivity : AppCompatActivity(), CursorWheelLayout.OnMenuSelectedListener {

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mAuth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var wheelImage: CursorWheelLayout
    private lateinit var colorList: ArrayList<ImageData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth.currentUser
        ref = FirebaseDatabase.getInstance().getReference("/Users")

        wheelImage = findViewById(R.id.ColorWheel)
        colorList = ArrayList()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(u in p0.children){
                        var user = u.getValue(User::class.java)
                        if(user!!.userID != mUser!!.uid){
                            val newUser = User(mUser.uid)
                            ref.child(newUser.userID).setValue(newUser)
                        }
                    }
                }
            }

        })

        loadColors()

        itemButton.setOnClickListener{
            val intent = Intent(this,TotalItemsActivity::class.java)
            startActivity(intent)
        }
        roomsButton.setOnClickListener{
            val intent = Intent(this,GroupsActivity::class.java)
            startActivity(intent)
        }
        signoutButton.setOnClickListener{
            signout()
        }
    }

    private fun signout(){
        mAuth.signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadColors(){
        colorList.add(ImageData("#ff0000"))
        colorList.add(ImageData("#ff4000"))
        colorList.add(ImageData("#ff8000"))
        colorList.add(ImageData("#ffbf00"))
        colorList.add(ImageData("#ffff00"))
        colorList.add(ImageData("#bfff00"))
        colorList.add(ImageData("#80ff00"))
        colorList.add(ImageData("#40ff00"))
        colorList.add(ImageData("#00ff00"))
        colorList.add(ImageData("#00ff40"))
        colorList.add(ImageData("#00ff80"))
        colorList.add(ImageData("#00ffbf"))
        colorList.add(ImageData("#00ffff"))
        colorList.add(ImageData("#00bfff"))
        colorList.add(ImageData("#0080ff"))
        colorList.add(ImageData("#0040ff"))
        colorList.add(ImageData("#0000ff"))
        colorList.add(ImageData("#4000ff"))
        colorList.add(ImageData("#8000ff"))
        colorList.add(ImageData("#bf00ff"))
        colorList.add(ImageData("#ff00ff"))
        colorList.add(ImageData("#ff00bf"))
        colorList.add(ImageData("#ff0080"))
        colorList.add(ImageData("#ff0040"))

        val imgAdapter = WheelImageAdapter(baseContext,colorList)
        wheelImage.setAdapter(imgAdapter)
    }

    override fun onItemSelected(parent: CursorWheelLayout?, view: View?, pos: Int) {
        val view = this.window.decorView
        val color = colorList?.get(pos).imageDescription
        view?.setBackgroundDrawable(ColorDrawable(Color.parseColor(color)))
    }
}
