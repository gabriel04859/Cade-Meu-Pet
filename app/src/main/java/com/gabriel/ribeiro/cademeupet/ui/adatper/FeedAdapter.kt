package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.model.Post
import com.squareup.picasso.Picasso

class FeedAdapter(private val onPostClickListener : OnPostClickListener) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageViewAnimalItemFeed : ImageView = itemView.findViewById(R.id.imageViewAnimalItemFeed)
        val textNameAnimalItemFeed : TextView = itemView.findViewById(R.id.textNameAnimalItemFeed)
        val textViewAddressItemFeed : TextView = itemView.findViewById(R.id.textViewAddressItemFeed)

    }

    interface OnPostClickListener{
        fun onPostClick(post : Post)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.idPost == newItem.idPost
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.itemView.apply {
            post?.let {post ->
                post.address?.let {
                    holder.textViewAddressItemFeed.text = it.street
                }
                post.animal?.let {
                    Picasso.with(context).load(it.images?.first()).placeholder(R.drawable.ic_loading).into(holder.imageViewAnimalItemFeed)
                    holder.textNameAnimalItemFeed.text = it.name
                }
            }
        }

        holder.itemView.setOnClickListener {
            onPostClickListener.onPostClick(post)
        }
    }

    override fun getItemCount() = differ.currentList.size

}