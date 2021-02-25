package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.model.Contact
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class LastMessageAdapter(private val onContactClickListener : OnContactClickListener ) : RecyclerView.Adapter<LastMessageAdapter.LastMessageViewHolder>() {

    inner class LastMessageViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageViewUserLastMessages : CircleImageView = itemView.findViewById(R.id.imageViewUserLastMessages)
        val textViewNameUserLastMessage : TextView = itemView.findViewById(R.id.textViewNameUserLastMessage)
        val textViewTextLastMessage : TextView = itemView.findViewById(R.id.textViewTextLastMessage)

    }

    interface OnContactClickListener{
        fun onContactClick(contact : Contact)
    }
    private val differCallBack = object : DiffUtil.ItemCallback<Contact>(){
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
           return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastMessageViewHolder {
       return LastMessageViewHolder(LayoutInflater.from(parent.context)
               .inflate(R.layout.item_last_messages, parent, false))
    }

    override fun onBindViewHolder(holder: LastMessageViewHolder, position: Int) {
        val contact = differ.currentList[position]
        holder.itemView.apply {
            Picasso.with(context).load(contact.userPhoto).placeholder(R.drawable.ic_profile).into(holder.imageViewUserLastMessages)
            holder.textViewNameUserLastMessage.text = contact.name
            holder.textViewTextLastMessage.text = contact.lastMessage
        }
        holder.itemView.setOnClickListener {
            onContactClickListener.onContactClick(contact)
        }
    }

    override fun getItemCount() = differ.currentList.size
}