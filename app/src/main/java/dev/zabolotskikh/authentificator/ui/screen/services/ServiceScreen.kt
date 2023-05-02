@file:OptIn(ExperimentalMaterial3Api::class)

package dev.zabolotskikh.authentificator.ui.screen.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.authentificator.R
import dev.zabolotskikh.authentificator.domain.model.GenerationMethod
import java.nio.channels.Selector

@Composable
fun ServiceScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
        content = { paddingValues ->
            val viewModel = hiltViewModel<ServiceViewModel>()
            val state by viewModel.state.collectAsState()

            ServicesList(state, viewModel::onEvent, paddingValues)

            DisposableEffect(true) {
                viewModel.startGeneration()

                onDispose { viewModel.stopGeneration() }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { AddServiceButton() },
    )
}


@Composable
fun AddServiceDialog(
    state: ServiceState,
    onEvent: (ServiceEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = { Text(text = "Add service") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { onEvent(ServiceEvent.SetName(it)) },
                    placeholder = {
                        Text(text = "Service name")
                    }
                )
                TextField(
                    value = state.privateKey,
                    onValueChange = { onEvent(ServiceEvent.SetPrivateKey(it)) },
                    placeholder = {
                        Text(text = "Private key")
                    }
                )

                MethodSelector(
                    GenerationMethod.values().asList(),
                    GenerationMethod.TIME,
                    onSelectionChanged = {
                        onEvent(ServiceEvent.SetMethod(it))
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onEvent(ServiceEvent.SaveService) }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(ServiceEvent.HideDialog) }) {
                Text(text = "Cancel")
            }
        },
        modifier = modifier
    )
}

@Composable
fun AddServiceButton() {
    val viewModel = hiltViewModel<ServiceViewModel>()

    FloatingActionButton(onClick = {
        viewModel.onEvent(ServiceEvent.ShowDialog)
    }) {
        Text("Add")
    }
}

@Composable
fun ServicesList(
    state: ServiceState,
    onEvent: (ServiceEvent) -> Unit,
    paddingValues: PaddingValues,
) {
    fun formatCode(code: String) = try {
        code.substring(0..2) + " " + code.substring(3..5)
    } catch (e: Exception) {
        ""
    }

    if (state.isAddingService) {
        AddServiceDialog(state, onEvent = onEvent)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = paddingValues.calculateBottomPadding(),
                top = paddingValues.calculateTopPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.services) { service ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Text(text = service.name)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(fontSize = 42.sp, text = formatCode(service.currentCode))
                    if (service.timeoutTime > 0)
                        CircularProgressIndicator(progress = ((service.codeTtl.toFloat() / service.timeoutTime)))
                }
            }
        }
    }
}

@Composable
fun MethodSelector(
    list: List<GenerationMethod>,
    preselected: GenerationMethod,
    onSelectionChanged: (method: GenerationMethod) -> Unit,
    modifier: Modifier = Modifier
) {

    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {

            Text(
                text = selected.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.wrapContentWidth()
            ) {
                list.forEach { listEntry ->

                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry.name,
                                modifier = Modifier
                                    //.wrapContentWidth()  //optional instad of fillMaxWidth
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }

        }
    }
}