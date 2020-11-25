package com.example.practice_demo.wall.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_demo.R
import com.example.practice_demo.wall.data.model.PostItem

class PostViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

class PostAdapter: RecyclerView.Adapter<PostViewHolder>() {
    var data =  listOf<PostItem>()
        set(value) {
            field = value
            //todo: Toto neskor prec, lebo to updatuje cely list pri zmene
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.username
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.post_view, parent, false) as TextView

        return PostViewHolder(view)
    }
}
