package com.gabriel.ribeiro.cademeupet.model

data class User(
        val uid : String? = "",
        val name : String? = "",
        val lastName : String? = "",
        val email : String? = "",
        val password : String? ="",
        val phone : String? = "",
        val imageProfile : String? = "") {
}