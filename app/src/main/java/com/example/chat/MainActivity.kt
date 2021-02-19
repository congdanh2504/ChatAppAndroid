package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.chat.Fragment.ChatFragment
import com.example.chat.Fragment.ProfileFragment
import com.example.chat.Fragment.UserFragment
import com.example.chat.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.username
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_user.*
import androidx.fragment.app.FragmentPagerAdapter as FragmentPagerAdapter

class MainActivity : AppCompatActivity() {
    var firebaseUser : FirebaseUser?=null
    var reference : DatabaseReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setTitle("")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = firebaseUser?.let { FirebaseDatabase.getInstance().getReference("User").child(it.uid) }
        reference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var txtid = snapshot.child("id").getValue().toString()
                var txtImageURL = snapshot.child("imageURL").getValue().toString()
                var txtusername = snapshot.child("username").getValue().toString()
                username.setText(txtusername)
                if (txtImageURL.equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher)
                } else{
                    Glide.with(this@MainActivity).load(txtImageURL).into(profile_image)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        var viewPagerAdapter : ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(ChatFragment(),"Chat")
        viewPagerAdapter.addFragment(UserFragment(),"User")
        viewPagerAdapter.addFragment(ProfileFragment(),"Profile")
        view_pager.adapter = viewPagerAdapter
        tab_layout.setupWithViewPager(view_pager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(this@MainActivity,StartActivity::class.java))
            finish()
            return true
        }
        return false
    }
    class ViewPagerAdapter : FragmentPagerAdapter{
        var fragments : ArrayList<Fragment>? = null
        var titles : ArrayList<String>? = null
        constructor(fn : FragmentManager) : super(fn){
            fragments = ArrayList()
            titles = ArrayList()
        }
        override fun getCount(): Int {
            return fragments!!.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments!!.get(position)
        }

        fun addFragment(fn : Fragment, title: String){
            fragments!!.add(fn)
            titles!!.add(title)
        }
        override fun getPageTitle(position: Int): CharSequence? {
            return titles!!.get(position)
        }
    }
}