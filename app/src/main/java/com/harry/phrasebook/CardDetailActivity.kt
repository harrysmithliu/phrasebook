package com.harry.phrasebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@AndroidEntryPoint
class CardDetailActivity : ComponentActivity() {
    private val vm: CardDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val card by vm.card.collectAsStateWithLifecycle()

                var showBack by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showBack = !showBack },
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // 上半：正面
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = card?.frontText ?: "…",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    HorizontalDivider()

                    // 下半：背面（首次隐藏，点屏显/隐）
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showBack && card != null
                        ) {
                            Text(
                                text = card?.backText.orEmpty(),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        if (!showBack) {
                            Text(
                                text = "Tap to reveal",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}