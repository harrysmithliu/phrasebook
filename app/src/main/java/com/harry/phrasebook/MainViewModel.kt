package com.harry.phrasebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.phrasebook.core.model.Card
import com.harry.phrasebook.core.model.Deck
import com.harry.phrasebook.core.model.Result
import com.harry.phrasebook.domain.repository.PhraseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: PhraseRepository
) : ViewModel() {

    // 搜索框输入
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // 只做一次种子写入的标记
    private val seeded = MutableStateFlow(false)

    // 对外暴露的卡片列表（带去抖与查询）
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val cardList: StateFlow<List<Card>> =
        query
            .debounce(300)                // 300ms 去抖
            .distinctUntilChanged()
            .flatMapLatest { repo.observeCards(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun onQueryChange(newValue: String) {
        _query.value = newValue
    }

    private fun ensureSeedOnce() {
        if (seeded.value) return
        viewModelScope.launch {
            // 查库为空则插入示例数据
            val r = repo.searchCards("")
            if (r is Result.Ok && r.value.isEmpty()) {
                val deckId = (repo.upsertDeck(Deck(name = "Small Talk")) as Result.Ok<Long>).value
                repo.upsertCard(Card(deckId = deckId, frontText = "Hello",        backText = "你好"))
                repo.upsertCard(Card(deckId = deckId, frontText = "How are you?", backText = "你好吗？"))
            }
            seeded.value = true
        }
    }

    fun addCard(front: String, back: String) {
        viewModelScope.launch {
            // 这里我用“第一个 Deck”或自动创建一个 Deck
            val exists = repo.searchCards("")               // 取一下已有数据
            val deckId = if (exists is Result.Ok && exists.value.isNotEmpty()) {
                exists.value.first().deckId
            } else {
                // 没有就建一个默认分组
                val r = repo.upsertDeck(Deck(name = "Default"))
                (r as Result.Ok<Long>).value
            }
            repo.upsertCard(Card(deckId = deckId, frontText = front, backText = back))
            // 刷新：直接改 query 触发搜索流
            _query.value = _query.value
        }
    }

    fun deleteCard(id: Long) {
        viewModelScope.launch {
            repo.deleteCard(id)
            // 触发一次查询流刷新
            _query.value = _query.value
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()        // 下面第 C 步会加这个仓库 API
            _query.value = _query.value
        }
    }
}
