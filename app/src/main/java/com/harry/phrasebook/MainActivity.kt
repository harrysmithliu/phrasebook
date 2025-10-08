package com.harry.phrasebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                // DI 获取 ViewModel
                val vm: MainViewModel = hiltViewModel()
                // 订阅 UI 状态（提供初值以避免首次为 null）
                val cardList by vm.cardList.collectAsStateWithLifecycle(initialValue = emptyList())
                val query by vm.query.collectAsStateWithLifecycle(initialValue = "")

                var showAdd by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        androidx.compose.material3.TopAppBar(
                            title = { Text("Phrasebook") },
                            actions = {
                                TextButton(onClick = { vm.deleteAll() }) { Text("Clear") }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showAdd = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) { padding ->
                    Column(
                        Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = vm::onQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Search…") },
                            singleLine = true
                        )

                        Spacer(Modifier.height(12.dp))

                        LazyColumn(Modifier.fillMaxSize()) {
                            items(cardList) { c ->
                                androidx.compose.foundation.layout.Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                        .clickable {                    //item点击跳转
                                            startActivity(
                                                Intent(this@MainActivity, CardDetailActivity::class.java)
                                                    .putExtra("card_id", c.id)
                                            )
                                        }
                                ) {
                                    Text(
                                        text = "${c.frontText} -> ${c.backText}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 12.dp)
                                    )
                                    // 右侧删除
                                    TextButton(onClick = { vm.deleteCard(c.id) }) {
                                        Text("Delete")
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }

                if (showAdd) {
                    AddCardDialog(
                        onDismiss = { showAdd = false },
                        onConfirm = { front, back ->
                            vm.addCard(front, back)
                            showAdd = false
                        }
                    )
                }
            }
        }
    }
}

/** 新增卡片对话框（极简） */
@Composable
private fun AddCardDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var front by remember { mutableStateOf("") }
    var back by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Card") },
        text = {
            Column {
                OutlinedTextField(front, { front = it }, label = { Text("Front") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(back, { back = it }, label = { Text("Back") })
            }
        },
        confirmButton = {
            TextButton(
                enabled = front.isNotBlank() && back.isNotBlank(),
                onClick = { onConfirm(front, back) }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
