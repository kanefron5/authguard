@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package dev.zabolotskikh.authentificator.ui.screen.services

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.authentificator.R
import dev.zabolotskikh.authentificator.domain.model.GenerationMethod
import dev.zabolotskikh.authentificator.domain.model.Service

@Composable
fun ServiceScreen() {
    val viewModel = hiltViewModel<ServiceViewModel>()
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
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
    AlertDialog(onDismissRequest = { /*TODO*/ }, title = { Text(text = "Add service") }, text = {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(value = state.name,
                onValueChange = { onEvent(ServiceEvent.SetName(it)) },
                placeholder = {
                    Text(text = "Service name")
                })
            TextField(value = state.privateKey,
                onValueChange = { onEvent(ServiceEvent.SetPrivateKey(it)) },
                placeholder = {
                    Text(text = "Private key")
                })

            MethodSelector(GenerationMethod.values().asList(),
                GenerationMethod.TIME,
                onSelectionChanged = {
                    onEvent(ServiceEvent.SetMethod(it))
                })
        }
    }, confirmButton = {
        Button(onClick = { onEvent(ServiceEvent.SaveService) }) {
            Text(text = "Save")
        }
    }, dismissButton = {
        Button(onClick = { onEvent(ServiceEvent.HideDialog) }) {
            Text(text = "Cancel")
        }
    }, modifier = modifier
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
            Text("Add")
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
                text = "Здесь пока ничего нет",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Image(
                painter = painterResource(id = R.drawable.empty_list),
                contentDescription = "Empty list"
            )
            Button(
                onClick = {
                    onEvent(ServiceEvent.ShowDialog)
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Добавьте свой первый ключ")
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
    var expanded by remember { mutableStateOf(false) } // initial value

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
                    modifier = Modifier.weight(1f).basicMarquee(),
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