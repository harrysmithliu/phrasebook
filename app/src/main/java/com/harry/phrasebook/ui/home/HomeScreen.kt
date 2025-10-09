package com.harry.phrasebook.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.phrasebook.MainViewModel
import com.harry.phrasebook.AppNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: MainViewModel,
    navigator: AppNavigator
) {
    val cardList by vm.cardList.collectAsStateWithLifecycle(initialValue = emptyList())
    val query by vm.query.collectAsStateWithLifecycle(initialValue = "")

    var showAdd by remember { mutableStateOf(false) }
    var front by remember { mutableStateOf("") }
    var back by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phrasebook") },
                actions = {
                    TextButton(onClick = { vm.deleteAll() }) { Text("Clear") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = vm::onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Search…") }
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(Modifier.fillMaxSize()) {
                items(cardList) { c ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${c.frontText} -> ${c.backText}",
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navigator.toDetail(c.id)
                                },
                            style = MaterialTheme.typography.bodyLarge
                        )
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
        AlertDialog(
            onDismissRequest = { showAdd = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.addCard(front.trim(), back.trim())
                        showAdd = false
                        front = ""
                        back = ""
                    },
                    enabled = front.isNotBlank() && back.isNotBlank()
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showAdd = false }) { Text("Cancel") }
            },
            title = { Text("Add Card") },
            text = {
                Column {
                    OutlinedTextField(
                        value = front, onValueChange = { front = it },
                        modifier = Modifier.fillMaxWidth(), placeholder = { Text("Front") }
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = back, onValueChange = { back = it },
                        modifier = Modifier.fillMaxWidth(), placeholder = { Text("Back") }
                    )
                }
            }
        )
    }
}
