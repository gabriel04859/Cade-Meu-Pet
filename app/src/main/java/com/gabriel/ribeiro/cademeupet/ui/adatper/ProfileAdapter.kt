package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.model.Post
import com.squareup.picasso.Picasso

class ProfileAdapter(private val onPostClickListener: OnPostProfileClickListener) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    private var postList : MutableList<Post> = ArrayList()
    fun getPosts(posts : MutableList<Post>){
        postList = posts
        notifyDataSetChanged()
    }
    interface OnPostProfileClickListener{
        fun onPostClick(post : Post)
    }

    inner class ProfileViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageViewPost : ImageView = itemView.findViewById(R.id.imageViewPostProfile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false))
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val post = postList[position]
        holder.itemView.apply {
            Picasso.with(context).load(post.animal?.imageUrl).into(holder.imageViewPost)
        }
        holder.itemView.setOnClickListener {
            onPostClickListener.onPostClick(post)
        }
    }

    override fun getItemCount() = postList.size
}