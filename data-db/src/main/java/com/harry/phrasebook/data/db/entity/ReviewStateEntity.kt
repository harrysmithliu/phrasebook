package com.harry.phrasebook.data.db.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewStateEntity(
    @PrimaryKey val cardId: Long,
    val ease: Double,
    val intervalDays: Int,
    val repetitions: Int,
    val dueAt: Long
)
