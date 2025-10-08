package com.harry.phrasebook

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import com.harry.phrasebook.core.model.Card
import com.harry.phrasebook.domain.repository.PhraseRepository

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    repo: PhraseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cardId: Long =
        checkNotNull(savedStateHandle.get<Long>("card_id")) {
            "Missing 'card_id' in SavedStateHandle / Intent extras"
        }

    val card: StateFlow<Card?> =
        repo.observeCard(cardId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
