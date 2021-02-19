package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.Adapter.MessageAdapter
import com.example.chat.Model.Chat
import com.example.chat.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_message.profile_image
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessageActivity : AppCompatActivity() {
    var fuser: FirebaseUser? = null
    var messageAdapter : MessageAdapter? =null
    var mchat : ArrayList<Chat>? = null
    var reference : DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (toolbar as androidx.appcompat.widget.Toolbar).setNavigationOnClickListener {
            finish()
        }
        recycler_vieww.setHasFixedSize(true)
        var linerLayout : LinearLayoutManager = LinearLayoutManager(applicationContext)
        linerLayout.stackFromEnd = true
        recycler_vieww?.layoutManager = linerLayout
        var intent : Intent = intent
        var userid : String = intent.getStringExtra("userid").toString()
        btn_send.setOnClickListener {
            var msg : String = text_send.text.toString()
            if (!msg.equals("")){ fuser?.let { it1 -> sendMessage(it1.uid,userid,msg) } }
            text_send.setText("")
        }
        fuser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid)
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var txtid = snapshot.child("id").getValue().toString()
                var txtImageURL = snapshot.child("imageURL").getValue().toString()
                val txtusername = snapshot.child("username").getValue().toString()
                var user : User = User(txtid,txtusername,txtImageURL)
                usernamee.setText(txtusername)
                if (txtImageURL.equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher)
                } else{
                    Glide.with(this@MessageActivity).load(txtImageURL).into(profile_image)
                }
                readMessage(fuser!!.uid,userid,user.immageURL)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun sendMessage(sender : String, receiver : String, message :String){
        var reference : DatabaseReference = FirebaseDatabase.getInstance().reference
        var map : HashMap<String, String> = HashMap();
        map.put("sender",sender)
        map.put("receiver",receiver)
        map.put("message",message)
        reference.child("Chats").push().setValue(map)
    }
    fun readMessage(myid : String, userid :String, imageurl : String){
        mchat = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mchat!!.clear()
                for (dsnapshot : DataSnapshot in snapshot.children){
                    var sender = dsnapshot.child("sender").getValue().toString();
                    var receiver = dsnapshot.child("receiver").getValue().toString();
                    var message = dsnapshot.child("message").getValue().toString();
                    var chat : Chat = Chat(sender,receiver,message)
                    if (receiver.equals(myid) && sender.equals(userid) || receiver.equals(userid) && sender.equals(myid)){
                        mchat!!.add(chat)
                    }
                    messageAdapter = MessageAdapter(this@MessageActivity, mchat,imageurl)
                    recycler_vieww.adapter = messageAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}