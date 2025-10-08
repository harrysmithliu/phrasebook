package com.harry.phrasebook.domain.usecase

import com.harry.phrasebook.core.model.Card
import com.harry.phrasebook.core.model.Result
import com.harry.phrasebook.domain.repository.PhraseRepository

class AddCard(private val repo: PhraseRepository) {
    suspend operator fun invoke(deckId: Long, front: String, back: String): Result<Long> =
        repo.upsertCard(Card(deckId = deckId, frontText = front, backText = back))
}
