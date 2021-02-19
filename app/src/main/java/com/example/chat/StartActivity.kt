package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    var firebaseUser : FirebaseUser? = null
    override fun onStart() {
        super.onStart()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser!=null){
            startActivity(Intent(this@StartActivity,MainActivity::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        login.setOnClickListener {
            startActivity(Intent(this@StartActivity,LoginActivity::class.java))
        }
        register.setOnClickListener{
            startActivity(Intent(this@StartActivity,RegisterActivity::class.java))
        }
    }
}