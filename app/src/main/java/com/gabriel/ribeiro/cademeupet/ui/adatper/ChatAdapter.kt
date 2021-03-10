package com.gabriel.ribeiro.cademeupet.ui.adatper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.Message
import org.w3c.dom.Text

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.Holder>() {
    private var messageList: MutableList<Message> = ArrayList()

    fun setMessages(messages: MutableList<Message>) {
        messageList = messages
        notifyDataSetChanged()
    }

    private val MESSAGE_RIGHT = 1
    private val MESSAGE_LEFTH = 0

    abstract class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(message: Message)
    }

    inner class ToIdViewHolder(itemView: View) : Holder(itemView) {
        private val textViewToIdMessage: TextView = itemView.findViewById(R.id.textViewToIdMessage)
        override fun bind(message: Message) {
            textViewToIdMessage.text = message.text
        }
    }

    inner class FromIdViewHolder(itemView: View) : Holder(itemView) {
        private val textViewFromIdMessage: TextView = itemView.findViewById(R.id.textViewFromIdMessage)
        override fun bind(message: Message) {
            textViewFromIdMessage.text = message.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == MESSAGE_RIGHT) {
            FromIdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.from_id, parent, false))
        } else {
            ToIdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.to_id, parent, false))
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val message = messageList[position]
        holder.bind(message)

    }

    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].uidFrom == FirebaseInstances.getFirebaseAuth().currentUser?.uid) {
            MESSAGE_RIGHT
        } else {
            MESSAGE_LEFTH
        }
    }
}