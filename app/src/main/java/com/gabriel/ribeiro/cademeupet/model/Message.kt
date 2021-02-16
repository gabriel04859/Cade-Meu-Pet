package com.gabriel.ribeiro.cademeupet.model

data class Message(val text : String? = "", val timestamp: Long? = -1, val uidFrom : String? = "", val uidTo : String? = "") {
}