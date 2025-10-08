package com.harry.phrasebook.data.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.harry.phrasebook.data.db.dao.*
import com.harry.phrasebook.data.db.entity.*

@Database(
    entities = [DeckEntity::class, CardEntity::class, ReviewStateEntity::class],
    version = 1, exportSchema = false
)
abstract class PhrasebookDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun reviewDao(): ReviewDao
}
