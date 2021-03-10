package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.databinding.ItemLastMessagesBinding
import com.gabriel.ribeiro.cademeupet.databinding.ItemProfileBinding
import com.gabriel.ribeiro.cademeupet.model.Post
import com.squareup.picasso.Picasso

class ProfileAdapter(private val onPostClickListener: OnPostProfileClickListener) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    private var postList: MutableList<Post> = ArrayList()

    fun getPosts(posts: MutableList<Post>) {
        postList = posts
        notifyDataSetChanged()
    }

    interface OnPostProfileClickListener {
        fun onPostClick(post: Post)
    }

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                Picasso.with(itemView.context).load(post.animal?.imageUrl).into(imageViewPostProfile)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
        holder.itemView.setOnClickListener {
            onPostClickListener.onPostClick(post)
        }
    }

    override fun getItemCount() = postList.size
}