package com.example.ta_papb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class Post(val user: String, val content: String, val likes: Int, val comments: Int, val shares: Int)

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPostUser: TextView = view.findViewById(R.id.tvPostUser)
        val tvPostContent: TextView = view.findViewById(R.id.tvPostContent)
        val tvLikes: TextView = view.findViewById(R.id.tvLikes)
        val tvComments: TextView = view.findViewById(R.id.tvComments)
        val tvShares: TextView = view.findViewById(R.id.tvShares)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.tvPostUser.text = post.user
        holder.tvPostContent.text = post.content
        holder.tvLikes.text = post.likes.toString()
        holder.tvComments.text = post.comments.toString()
        holder.tvShares.text = post.shares.toString()
    }

    override fun getItemCount(): Int = posts.size
}
