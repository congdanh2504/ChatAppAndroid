package com.example.chat.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.example.chat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.profile_image
import kotlinx.android.synthetic.main.activity_main.username
import kotlinx.android.synthetic.main.fragment_profile.*



class ProfileFragment : Fragment() {
    var reference : DatabaseReference? = null
    var fuser : FirebaseUser? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        fuser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("User").child(fuser!!.uid)
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var txtImageURL = snapshot.child("imageURL").getValue().toString()
                var txtusername = snapshot.child("username").getValue().toString()
                username.setText(txtusername)
                if (txtImageURL.equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher)
                } else{
                    Glide.with(this@ProfileFragment).load(txtImageURL).into(profile_image)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }
}