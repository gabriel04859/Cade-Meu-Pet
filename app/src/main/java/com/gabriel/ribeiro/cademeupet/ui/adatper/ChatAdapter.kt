package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Message

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private var messageList : MutableList<Message> = ArrayList()
    fun setMessages(messages : MutableList<Message>){
        messageList = messages
        notifyDataSetChanged()
    }
    private val MESSAGE_RIGHT = 1
    private val MESSAGE_LEFTH = 0

    inner class ChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textViewShowMessage : TextView = itemView.findViewById(R.id.textViewShowMessage)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == MESSAGE_RIGHT){
            ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.from_id,parent, false))
        }else{
            ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.to_id,parent, false))
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messageList[position]
        holder.itemView.apply {
            holder.textViewShowMessage.text = message.text
        }
    }

    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].uidFrom == FirebaseInstances.getFirebaseAuth().currentUser?.uid){
            MESSAGE_RIGHT
        }else{
            MESSAGE_LEFTH
        }
    }
}