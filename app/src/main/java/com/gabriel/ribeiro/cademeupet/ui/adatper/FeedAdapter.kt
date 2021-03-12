package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.databinding.ItemFeedBinding
import com.gabriel.ribeiro.cademeupet.model.Post
import com.squareup.picasso.Picasso

class FeedAdapter(private val onPostClickListener: OnPostClickListener, val context  : Context) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                Picasso.with(context).load(post.animal?.imageUrl).placeholder(R.drawable.d_placeholder).into(imageViewAnimalItemFeed)
                textNameAnimalItemFeed.text = post.animal?.name
                val addressText = "${post.address?.street} - ${post.address?.street}"
                textViewAddressItemFeed.text = addressText
            }

        }

    }

    interface OnPostClickListener {
        fun onPostClick(post: Post)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.idPost == newItem.idPost
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.bind(post)

        holder.itemView.setOnClickListener {
            onPostClickListener.onPostClick(post)
        }
    }

    override fun getItemCount() = differ.currentList.size

}