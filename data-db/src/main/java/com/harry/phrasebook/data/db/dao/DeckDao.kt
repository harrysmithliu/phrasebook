package com.harry.phrasebook.data.db.dao
import androidx.room.*
import com.harry.phrasebook.data.db.entity.DeckEntity

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(deck: DeckEntity): Long
    @Query("SELECT * FROM decks ORDER BY createdAt DESC") suspend fun all(): List<DeckEntity>
}
