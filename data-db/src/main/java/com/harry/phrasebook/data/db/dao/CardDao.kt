package com.harry.phrasebook.data.db.dao
import androidx.room.*
import com.harry.phrasebook.data.db.entity.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(card: CardEntity): Long

    @Query("SELECT * FROM cards WHERE frontText LIKE '%'||:q||'%' OR backText LIKE '%'||:q||'%' ORDER BY id DESC")
    suspend fun search(q: String): List<CardEntity>

    @Query("""
        SELECT * FROM cards
        WHERE frontText LIKE '%'||:q||'%' OR backText LIKE '%'||:q||'%'
        ORDER BY id DESC
    """)
    fun observeSearch(q: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<CardEntity?>

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CardEntity?

    @Query("SELECT * FROM cards WHERE id IN(:ids)")
    suspend fun byIds(ids: List<Long>): List<CardEntity>
    @Query("SELECT COUNT(*) FROM cards")
    suspend fun count(): Int
    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteById(id: Long)
    @Query("DELETE FROM cards")
    suspend fun deleteAll()
}
