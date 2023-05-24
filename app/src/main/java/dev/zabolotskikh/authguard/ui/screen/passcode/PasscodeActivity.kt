package dev.zabolotskikh.authguard.ui.screen.passcode

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.zabolotskikh.authguard.ui.screen.passcode.components.EnterPasscodeScreen
import dev.zabolotskikh.authguard.ui.screen.passcode.components.SetupPasscodeScreen
import dev.zabolotskikh.authguard.ui.theme.AuthGuardTheme
import java.io.Serializable

@AndroidEntryPoint
class PasscodeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options =
            intent.getExtra<PasscodeAction>(ACTION_EXTRA) ?: throw IllegalArgumentException()

        setContent {
            AuthGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<PasscodeViewModel>()
                    val state by viewModel.state.collectAsState()

                    LaunchedEffect(state) {
                        if (state.isSucceed) {
                            setResult(RESULT_OK, Intent().apply {
                                putExtra(STATUS_EXTRA, PasscodeResult.Succeed)
                            })
                            finish()
                        } else if (state.isConfirmed) {
                            setResult(RESULT_OK, Intent().apply {
                                putExtra(STATUS_EXTRA, PasscodeResult.Confirmed)
                            })
                            finish()
                        } else if (state.isLimitReached) {
                            setResult(RESULT_OK, Intent().apply {
                                putExtra(STATUS_EXTRA, PasscodeResult.LimitReached)
                            })
                            finish()
                        } else if (state.isCancelled) {
                            setResult(RESULT_OK, Intent().apply {
                                putExtra(STATUS_EXTRA, PasscodeResult.Cancelled)
                            })
                            finish()
                        }
                    }

                    when (options) {
                        is PasscodeAction.EnterPasscode -> EnterPasscodeScreen(
                            options = options, onEvent = viewModel::onEvent
                        )

                        is PasscodeAction.SetupPasscode -> SetupPasscodeScreen(
                            state = state, options = options, onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }

    class PasscodeResultContract : ActivityResultContract<PasscodeAction, PasscodeResult?>() {
        @CallSuper
        override fun createIntent(context: Context, input: PasscodeAction): Intent {
            return Intent(context, PasscodeActivity::class.java).apply {
                putExtra(ACTION_EXTRA, input)
            }
        }

        override fun getSynchronousResult(
            context: Context, input: PasscodeAction
        ): SynchronousResult<PasscodeResult?>? = null

        override fun parseResult(resultCode: Int, intent: Intent?): PasscodeResult? {
            return intent?.takeIf { resultCode == Activity.RESULT_OK }
                ?.getExtra<PasscodeResult>(STATUS_EXTRA)
        }
    }

    sealed interface PasscodeAction : Serializable {
        data class SetupPasscode(val count: Int, val length: Int?) : PasscodeAction
        data class EnterPasscode(val maxAttemptCount: Int) : PasscodeAction
    }

    enum class PasscodeResult : Serializable {
        Succeed, Confirmed, LimitReached, Cancelled
    }

    companion object {
        private const val STATUS_EXTRA = "status"
        private const val ACTION_EXTRA = "action"

        private inline fun <reified T : Serializable> Intent.getExtra(name: String): T? {
            @Suppress("DEPRECATION") return if (Build.VERSION.SDK_INT >= 33) getSerializableExtra(
                name, T::class.java
            )
            else getSerializableExtra(name) as T?
        }
    }
}