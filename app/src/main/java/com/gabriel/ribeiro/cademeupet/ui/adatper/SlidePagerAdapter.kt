package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.model.Post
import com.squareup.picasso.Picasso

class SlidePagerAdapter(val images : MutableList<String>) : RecyclerView.Adapter<SlidePagerAdapter.SliderPagerView>(){

    inner class SliderPagerView(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageSlider : ImageView = itemView.findViewById(R.id.imageViewAnimalDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderPagerView {
        val v =  LayoutInflater.from(parent.context).inflate(R.layout.slider_pager_item, parent, false)
        return SliderPagerView(v)
    }

    override fun onBindViewHolder(holder: SliderPagerView, position: Int) {
        val imagePost = images[position]
        holder.itemView.apply {
            Picasso.with(context).load(imagePost).into(holder.imageSlider)
        }

    }

    override fun getItemCount(): Int = images.size
}