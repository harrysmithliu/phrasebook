package com.harry.phrasebook.domain.repository

import com.harry.phrasebook.core.model.*
import kotlinx.coroutines.flow.Flow

interface PhraseRepository {
    suspend fun upsertDeck(deck: Deck): Result<Long>
    suspend fun upsertCard(card: Card): Result<Long>
    suspend fun getTodayDueCards(now: Long = System.currentTimeMillis()): Result<List<Pair<Card, ReviewState>>>
    suspend fun updateReview(state: ReviewState): Result<Unit>
    suspend fun searchCards(query: String): Result<List<Card>>
    fun observeCards(query: String): Flow<List<Card>>
    fun observeCard(id: Long): Flow<Card?>
    suspend fun exportJson(): Result<String>
    suspend fun importJson(json: String): Result<Unit>
    suspend fun deleteCard(id: Long): Result<Unit>
    suspend fun deleteAll(): Result<Unit>
}