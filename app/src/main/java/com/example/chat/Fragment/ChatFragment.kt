package com.example.chat.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chat.Adapter.CustomAdapter
import com.example.chat.Model.Chat
import com.example.chat.Model.User
import com.example.chat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChatFragment : Fragment() {
    var mUsers : ArrayList<User>? = null
    var adapter : CustomAdapter? = null
    var userList : ArrayList<String>? = null
    var fuser : FirebaseUser? = null
    var reference : DatabaseReference? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_chat, container, false)
        fuser = FirebaseAuth.getInstance().currentUser
        userList = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList?.let { it.clear() }
                for (dsnapshot : DataSnapshot in snapshot.children){
                    var sender = dsnapshot.child("sender").getValue().toString()
                    var receiver = dsnapshot.child("receiver").getValue().toString()
                    var message = dsnapshot.child("message").getValue().toString()
                    var chat = Chat(sender, receiver, message)
                    if (sender.equals(fuser?.uid)){
                        userList!!.add(receiver)
                    }
                    if (receiver.equals(fuser?.uid)){
                        userList!!.add(sender)
                    }
                }
                readChats()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }
    fun readChats(){
        mUsers = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("User")
        reference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mUsers?.clear()
                for (dsnapshot : DataSnapshot in snapshot.children){
                    var txtid = dsnapshot.child("id").getValue().toString()
                    var txtImageURL = dsnapshot.child("imageURL").getValue().toString()
                    var txtusername = dsnapshot.child("username").getValue().toString()
                    var user = User(txtid,txtusername,txtImageURL)
                    for (id : String in userList!!){
                        if (txtid.equals(id)){
                            if (mUsers?.indexOf(user)==-1){
                                mUsers?.add(user)
                            }
                        }
                    }
                }
                adapter = activity?.let { CustomAdapter(it, mUsers!!) }
                adapter?.notifyDataSetChanged()
                list_view.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}