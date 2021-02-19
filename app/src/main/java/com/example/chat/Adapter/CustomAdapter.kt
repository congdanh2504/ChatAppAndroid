package com.example.chat.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.chat.MessageActivity
import com.example.chat.Model.User
import com.example.chat.R
import de.hdodenhof.circleimageview.CircleImageView

class CustomAdapter(var context: Context, var list: ArrayList<User>) : BaseAdapter () {
    class ViewHolder (row : View){
        var username : TextView
        var profile_user : CircleImageView
        init {
            username = row.findViewById(R.id.username)
            profile_user = row.findViewById(R.id.profile_image)
        }
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
        var view : View?
        var viewHolder : ViewHolder
        if (p1 == null){
            var layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.user_item,null)
            viewHolder = view?.let { ViewHolder(it) }!!
            view?.tag = viewHolder
        } else {
            view = p1
            viewHolder = p1.tag as ViewHolder
        }
        var user : User = getItem(p0) as User
        viewHolder.username.text = user.username
        if (user.immageURL.equals("default")){
            viewHolder.profile_user.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.immageURL).into(viewHolder.profile_user);
        }
        view.setOnClickListener {
            var intent : Intent = Intent(context,MessageActivity::class.java)
            intent.putExtra("userid",user.id)
            context.startActivities(arrayOf(intent))
        }
        return view
    }

}