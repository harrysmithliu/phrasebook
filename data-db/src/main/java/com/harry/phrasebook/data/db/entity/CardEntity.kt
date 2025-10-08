package com.harry.phrasebook.data.db.entity
import androidx.room.*

@Entity(
    tableName = "cards",
    foreignKeys = [ForeignKey(
        entity = DeckEntity::class,
        parentColumns = ["id"],
        childColumns = ["deckId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("deckId")]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deckId: Long,
    val frontText: String,
    val backText: String,
    val imageUri: String? = null,
    val audioUri: String? = null,
    val tags: String
)
