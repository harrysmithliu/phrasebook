package com.harry.phrasebook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Deck(
    val id: Long = 0,
    val name: String,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)