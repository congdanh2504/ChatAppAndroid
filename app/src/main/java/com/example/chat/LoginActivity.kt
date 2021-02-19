package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.bar_layout.*

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setTitle("Login")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_login.setOnClickListener {
            var mail = maill.text.toString()
            var password = passwordl.text.toString()
            if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)){
                Toast.makeText(this@LoginActivity,"Error",Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        var intent : Intent = Intent(this@LoginActivity,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity,"Error",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}