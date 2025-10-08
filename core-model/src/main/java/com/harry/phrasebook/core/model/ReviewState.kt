package com.harry.phrasebook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewState(
    val cardId: Long,
    val ease: Double = 2.5,
    val intervalDays: Int = 0,
    val repetitions: Int = 0,
    val dueAt: Long = System.currentTimeMillis()
)