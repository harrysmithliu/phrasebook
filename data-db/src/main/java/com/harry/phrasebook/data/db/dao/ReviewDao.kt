package com.harry.phrasebook.data.db.dao
import androidx.room.*
import com.harry.phrasebook.data.db.entity.ReviewStateEntity

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(state: ReviewStateEntity)
    @Query("SELECT * FROM reviews WHERE dueAt <= :now") suspend fun due(now: Long): List<ReviewStateEntity>
}
