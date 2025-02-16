package com.example.androidtermprojectmotopedia.model

import kotlinx.serialization.Serializable

//data class Brand (
//    val name: String,
//    val models: List<Motorcycle>
//)

@Serializable
data class Brand (
    val brand : String = "",
    val founded : Int = 0,
    val logo : String = "",
    val headquarters : String = "",
    val detail : String = "",
    val stores : List<Store> = listOf()
)