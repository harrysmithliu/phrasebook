package com.harry.phrasebook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: Long = 0,
    val deckId: Long,
    val frontText: String,
    val backText: String,
    val imageUri: String? = null,
    val audioUri: String? = null,
    val tags: List<String> = emptyList()
)