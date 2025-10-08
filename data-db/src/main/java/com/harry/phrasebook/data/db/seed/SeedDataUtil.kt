package com.harry.phrasebook.data.db.seed

import com.harry.phrasebook.data.db.PhrasebookDatabase
import com.harry.phrasebook.data.db.entity.CardEntity
import com.harry.phrasebook.data.db.entity.DeckEntity
import com.harry.phrasebook.data.db.entity.ReviewStateEntity

/**
 * 集中管理数据库种子数据（演示/默认数据）
 */
object SeedDataUtil {

    /**
     * 仅当库内无卡片时写入一套默认数据。
     * 应在 Room Database onCreate 或应用首次启动的后台任务中调用。
     */
    suspend fun seedIfEmpty(db: PhrasebookDatabase) {
        val cardDao = db.cardDao()
        if (cardDao.count() > 0) return  // 已有数据，直接返回

        val deckDao = db.deckDao()
        val reviewDao = db.reviewDao()

        val now = System.currentTimeMillis()

        // 1) 新建一个 Deck
        val deckId = deckDao.upsert(
            DeckEntity(
                id = 0,
                name = "Small Talk",
                tags = "en,zh",
                createdAt = now
            )
        )

        // 2) 插入两张 Card
        val helloId = cardDao.upsert(
            CardEntity(
                id = 0,
                deckId = deckId,
                frontText = "Hello",
                backText  = "你好",
                imageUri = null,
                audioUri = null,
                tags = "greeting"
            )
        )
        val howId = cardDao.upsert(
            CardEntity(
                id = 0,
                deckId = deckId,
                frontText = "How are you?",
                backText  = "你好吗？",
                imageUri = null,
                audioUri = null,
                tags = "greeting"
            )
        )

        // 3) 为每张卡初始化复习状态
        reviewDao.upsert(
            ReviewStateEntity(
                cardId = helloId,
                ease = 2.5,
                intervalDays = 0,
                repetitions = 0,
                dueAt = now
            )
        )
        reviewDao.upsert(
            ReviewStateEntity(
                cardId = howId,
                ease = 2.5,
                intervalDays = 0,
                repetitions = 0,
                dueAt = now
            )
        )
    }
}
