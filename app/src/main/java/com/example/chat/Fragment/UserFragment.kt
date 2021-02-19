package com.example.chat.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.chat.Adapter.CustomAdapter
import com.example.chat.Model.User
import com.example.chat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {
    var userAdapter : CustomAdapter? = null
    var mUser : java.util.ArrayList<User> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.fragment_user, container, false)
        readUser()
        return view;
    }

    private fun readUser() {
        var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        var reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("User")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mUser?.clear()
                for (dsnapshot : DataSnapshot in snapshot.children){
                    var txtid = dsnapshot.child("id").getValue().toString()
                    var txtImageURL = dsnapshot.child("imageURL").getValue().toString()
                    var txtusername = dsnapshot.child("username").getValue().toString()
                    if (!txtid.equals(firebaseUser?.uid)){
                        mUser?.add(User(txtid,txtusername,txtImageURL))
                    }
                }
                userAdapter = activity?.let { CustomAdapter(it,mUser) }
                userAdapter?.notifyDataSetChanged()
                list_view.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}