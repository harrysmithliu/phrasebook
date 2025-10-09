package com.harry.phrasebook.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Long,
    onBack: () -> Unit,
    vm: DetailViewModel = hiltViewModel()
) {
    val card by vm.getCardById(id).collectAsState(initial = null)
    var showBack by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (card != null) {
//            Column(
//                modifier = Modifier
//                    .padding(padding)
//                    .fillMaxSize()
//                    .clickable { showBack = !showBack },
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = card!!.frontText,
//                    style = MaterialTheme.typography.headlineLarge
//                )
//                if (showBack) {
//                    Text(
//                        text = card!!.backText,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//                } else {
//                    Text("Tap to reveal")
//                }
//            }
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 上半：正面
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = card!!.frontText,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }

                HorizontalDivider()

                // 下半：点击切换显示背面
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { showBack = !showBack },
                    contentAlignment = Alignment.Center
                ) {
                    if (showBack) {
                        Text(
                            text = card!!.backText,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "Tap to reveal",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
