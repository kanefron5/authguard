@file:OptIn(ExperimentalMaterial3Api::class)

package dev.zabolotskikh.authentificator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import dagger.hilt.android.AndroidEntryPoint
import dev.zabolotskikh.authentificator.ui.theme.AuthentificatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthentificatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { Text(text = getString(R.string.app_name)) })
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
            }
        }
    }
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

                TextField(
                    value = state.method,
                    onValueChange = { onEvent(ServiceEvent.SetMethod(it)) },
                    placeholder = {
                        Text(text = "Method")
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