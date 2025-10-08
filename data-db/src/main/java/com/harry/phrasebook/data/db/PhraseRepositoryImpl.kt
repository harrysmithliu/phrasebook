package com.harry.phrasebook.data.db

import com.harry.phrasebook.core.model.*
import com.harry.phrasebook.data.db.entity.*
import com.harry.phrasebook.domain.repository.PhraseRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Singleton
class PhraseRepositoryImpl @Inject constructor(
    private val db: PhrasebookDatabase
) : PhraseRepository {

    // ---------- 公共：IO helper ----------
    private suspend fun <T> io(block: suspend () -> T): T =
        withContext(Dispatchers.IO) { block() }

    // ---------- 映射：Entity <-> Model ----------
    private fun CardEntity.toModel(): Card = Card(
        id = id,
        deckId = deckId,
        frontText = frontText,
        backText = backText,
        imageUri = imageUri,
        audioUri = audioUri,
        tags = parseTagsCompat(tags) // 兼容 JSON / 逗号 / 空字符串
    )

    private fun Card.toEntity(): CardEntity = CardEntity(
        id = id,
        deckId = deckId,
        frontText = frontText,
        backText = backText,
        imageUri = imageUri,
        audioUri = audioUri,
        // 统一以 JSON 方式写入
        tags = Json.encodeToString(ListSerializer(String.serializer()), tags)
    )

    private fun ReviewStateEntity.toModel() = ReviewState(
        cardId = cardId,
        ease = ease,
        intervalDays = intervalDays,
        repetitions = repetitions,
        dueAt = dueAt
    )

    private fun ReviewState.toEntity() = ReviewStateEntity(
        cardId = cardId,
        ease = ease,
        intervalDays = intervalDays,
        repetitions = repetitions,
        dueAt = dueAt
    )

    /** 向后兼容解析：优先按 JSON，失败再按逗号，最后兜底空列表 */
    private fun parseTagsCompat(raw: String?): List<String> {
        if (raw.isNullOrBlank()) return emptyList()
        // 简单判断：像 JSON 数组就走 JSON
        val looksJson = raw.trim().let { it.startsWith('[') && it.endsWith(']') }
        if (looksJson) {
            runCatching {
                return Json.decodeFromString(ListSerializer(String.serializer()), raw)
            }
        }
        // 逗号分隔兜底
        return raw.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    // ---------- 仓库接口实现 ----------
    override suspend fun upsertDeck(deck: Deck): Result<Long> = io {
        val id = db.deckDao().upsert(
            DeckEntity(
                id = deck.id,
                name = deck.name,
                // 也统一 JSON 存储，保持一致性
                tags = Json.encodeToString(ListSerializer(String.serializer()), deck.tags),
                createdAt = deck.createdAt
            )
        )
        Result.Ok(id)
    }

    override suspend fun upsertCard(card: Card): Result<Long> = runCatching {
        io { db.cardDao().upsert(card.toEntity()) }
    }.fold(onSuccess = { Result.Ok(it) }, onFailure = { Result.Err(it) })

    override suspend fun searchCards(query: String): Result<List<Card>> = io {
        val list = db.cardDao().search(query).map { it.toModel() }
        Result.Ok(list)
    }

    /** 新增：可观察的搜索结果，供界面实时刷新 */
    override fun observeCards(query: String): Flow<List<Card>> =
        db.cardDao().observeSearch(query)
            .map { it.map { e -> e.toModel() } }
            .catch { emit(emptyList()) } // 出错时不终止流

    /** 新增：按 id 观察单条卡片（详情页用） */
    override fun observeCard(id: Long): Flow<Card?> =
        db.cardDao().observeById(id).map { it?.toModel() }

    override suspend fun getTodayDueCards(now: Long): Result<List<Pair<Card, ReviewState>>> = io {
        val due = db.reviewDao().due(now) // List<ReviewStateEntity>
        val cards = db.cardDao().byIds(due.map { it.cardId })
        val stateById = due.associateBy { it.cardId }
        val pairs = cards.mapNotNull { c ->
            stateById[c.id]?.let { rs -> c.toModel() to rs.toModel() }
        }
        Result.Ok(pairs)
    }

    override suspend fun updateReview(state: ReviewState): Result<Unit> = runCatching {
        io { db.reviewDao().upsert(state.toEntity()) }
    }.fold(onSuccess = { Result.Ok(Unit) }, onFailure = { Result.Err(it) })

    override suspend fun exportJson(): Result<String> =
        Result.Err(UnsupportedOperationException("TODO"))

    override suspend fun importJson(json: String): Result<Unit> =
        Result.Err(UnsupportedOperationException("TODO"))

    override suspend fun deleteCard(id: Long): Result<Unit> = runCatching {
        io { db.cardDao().deleteById(id) }
    }.fold(onSuccess = { Result.Ok(Unit) }, onFailure = { Result.Err(it) })

    override suspend fun deleteAll(): Result<Unit> = runCatching {
        io { db.cardDao().deleteAll() }
    }.fold(onSuccess = { Result.Ok(Unit) }, onFailure = { Result.Err(it) })
}
