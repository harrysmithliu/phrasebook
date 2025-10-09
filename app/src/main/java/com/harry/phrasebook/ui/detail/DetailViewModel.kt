package com.harry.phrasebook.ui.detail

import androidx.lifecycle.ViewModel
import com.harry.phrasebook.domain.repository.PhraseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: PhraseRepository
) : ViewModel() {
    fun getCardById(id: Long) = repo.observeCard(id)
}
