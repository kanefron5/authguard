@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package dev.zabolotskikh.authenticator.ui.screen.services

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.authenticator.R
import dev.zabolotskikh.authenticator.domain.model.GenerationMethod
import dev.zabolotskikh.authenticator.domain.model.Service
import dev.zabolotskikh.authenticator.ui.Screen

@Composable
fun ServiceScreen(
    onNavigate: (screen: Screen, clear: Boolean) -> Unit = { _, _ -> }
) {
    val viewModel = hiltViewModel<ServiceViewModel>()
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { onNavigate(Screen.Settings, false) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "actionIconContentDescription",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            ServicesList(state, viewModel::onEvent, paddingValues)

            DisposableEffect(true) {
                viewModel.startGeneration()
                onDispose { viewModel.stopGeneration() }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { AddServiceButton(state, viewModel::onEvent) },
    )
}


@Composable
fun AddServiceDialog(
    state: ServiceState, onEvent: (ServiceEvent) -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { onEvent(ServiceEvent.HideDialog) },
        title = { Text(text = stringResource(id = R.string.add_service_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(value = state.name,
                    onValueChange = { onEvent(ServiceEvent.SetName(it)) },
                    placeholder = {
                        Text(text = stringResource(id = R.string.add_service_dialog_name))
                    })
                TextField(value = state.privateKey,
                    onValueChange = { onEvent(ServiceEvent.SetPrivateKey(it)) },
                    placeholder = {
                        Text(text = stringResource(id = R.string.add_service_dialog_key))
                    })

                MethodSelector(GenerationMethod.values().asList(),
                    GenerationMethod.TIME,
                    onSelectionChanged = {
                        onEvent(ServiceEvent.SetMethod(it))
                    })
            }
        },
        confirmButton = {
            Button(onClick = { onEvent(ServiceEvent.SaveService) }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(ServiceEvent.HideDialog) }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@Composable
fun AddServiceButton(
    state: ServiceState, onEvent: (ServiceEvent) -> Unit
) {

    if (state.services.isNotEmpty()) {
        FloatingActionButton(onClick = {
            onEvent(ServiceEvent.ShowDialog)
        }) {
            Text(stringResource(id = R.string.add_service))
        }
    }
}

@Composable
fun ServicesList(
    state: ServiceState,
    onEvent: (ServiceEvent) -> Unit,
    paddingValues: PaddingValues,
) {
    if (state.isAddingService) {
        AddServiceDialog(state, onEvent = onEvent)
    }

    if (state.services.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.service_list_empty),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Image(
                painter = painterResource(id = R.drawable.empty_list),
                contentDescription = stringResource(id = R.string.service_list_empty)
            )
            Button(
                onClick = {
                    onEvent(ServiceEvent.ShowDialog)
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(id = R.string.service_list_add_first))
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.services) { service ->
                ServiceListItem(service = service)
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
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(modifier = modifier.clickable {
        expanded = !expanded
    }) {


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
                                text = listEntry.name, modifier = Modifier
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

@Composable
fun ServiceListItem(
    modifier: Modifier = Modifier, service: Service
) {
    fun formatCode(code: String) = try {
        code.substring(0..2) + " " + code.substring(3..5)
    } catch (e: Exception) {
        ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    text = service.name,
                    color = MaterialTheme.colorScheme.secondary
                )
                IconToggleButton(
                    checked = false,
                    onCheckedChange = {},
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarRate,
                        contentDescription = "Star"
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = 42.sp,
                text = formatCode(service.currentCode),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
        if (service.timeoutTime > 0) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = ((service.codeTtl.toFloat() / service.timeoutTime))
            )
        }
    }
}