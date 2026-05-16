package com.kavyakanaja.model

data class Poem(
    val id: Int,
    val title: String,
    val poet: String,
    val text: String,
    val bhavartha: String,
    val audioFile: String,
    val difficultWords: List<WordMeaning>
)

data class WordMeaning(
    val word: String,
    val meaning: String
)
