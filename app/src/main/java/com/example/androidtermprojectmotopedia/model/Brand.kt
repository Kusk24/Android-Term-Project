package com.example.androidtermprojectmotopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class Brand (
    val brand : String = "",
    val founded : Int = 0,
    val founder : String = "",
    val logo : String = "",
    val headquarters : String = "",
    val detail : String = "",
    val stores : List<Store> = listOf()
)