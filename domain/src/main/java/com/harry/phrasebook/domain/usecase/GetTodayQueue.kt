package com.harry.phrasebook.domain.usecase

import com.harry.phrasebook.domain.repository.PhraseRepository

class GetTodayQueue(private val repo: PhraseRepository) {
    suspend operator fun invoke(now: Long = System.currentTimeMillis()) =
        repo.getTodayDueCards(now)
}
