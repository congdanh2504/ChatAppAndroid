package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.bar_layout.*

class RegisterActivity : AppCompatActivity() {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var reference : DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setTitle("Register")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_register.setOnClickListener {
            var username = username.text.toString()
            var mail = mail.text.toString()
            var password = password.text.toString()
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)){
                Toast.makeText(this@RegisterActivity,"Error",Toast.LENGTH_SHORT).show()
            } else if (password.length<6){
                Toast.makeText(this@RegisterActivity,"Error",Toast.LENGTH_SHORT).show()
            } else {
                register(username,mail,password)
            }
        }
    }
    fun register(username:String, email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                var firebaseUser : FirebaseUser? = auth.currentUser
                var userid = firebaseUser?.uid
                reference = userid?.let { it1 -> FirebaseDatabase.getInstance().getReference("User").child(it1) }
                var hashMap : HashMap<String,String> = HashMap()
                userid?.let { it1 -> hashMap.put("id", it1) };
                hashMap.put("username",username)
                hashMap.put("imageURL","default")
                reference?.setValue(hashMap)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        var intent : Intent = Intent(this@RegisterActivity,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity,"You can't register",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}