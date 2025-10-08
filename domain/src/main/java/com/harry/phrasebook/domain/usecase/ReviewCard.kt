package com.harry.phrasebook.domain.usecase

import com.harry.phrasebook.core.model.ReviewState
import com.harry.phrasebook.domain.repository.PhraseRepository

class ReviewCard(private val repo: PhraseRepository) {
    suspend operator fun invoke(state: ReviewState) = repo.updateReview(state)
}
