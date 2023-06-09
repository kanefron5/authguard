package dev.zabolotskikh.passlock.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
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
import dev.zabolotskikh.passlock.ui.activity.components.DeletePasscodeScreen
import dev.zabolotskikh.passlock.ui.activity.components.EditPasscodeScreen
import dev.zabolotskikh.passlock.ui.activity.components.EnterPasscodeScreen
import dev.zabolotskikh.passlock.ui.activity.components.SetupPasscodeScreen
import dev.zabolotskikh.passlock.ui.theme.PassLockTheme
import java.io.Serializable

@AndroidEntryPoint
class PasscodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options =
            intent.getExtra<PasscodeAction>(ACTION_EXTRA) ?: throw IllegalArgumentException()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (options.cancellable) {
                    finish()
                    this.remove()
                } else {
                    finishAffinity()
                }
            }
        })

        setContent {
            PassLockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<PasscodeViewModel>()
                    val state by viewModel.state.collectAsState()
                    val fallbackOnError =
                        (options is PasscodeAction.EnterPasscode) && options.fallbackOnError

                    LaunchedEffect(state.passcodeCheckStatus) {
                        when (state.passcodeCheckStatus) {
                            PasscodeResult.CONFIRMED, PasscodeResult.CANCELLED -> {
                                setResultAndFinish(state.passcodeCheckStatus)
                            }

                            PasscodeResult.SUCCEED -> {
                                if (options !is PasscodeAction.EditPasscode) {
                                    setResultAndFinish(state.passcodeCheckStatus)
                                }
                            }

                            PasscodeResult.BLOCKED -> {
                                if (fallbackOnError) {
                                    setResultAndFinish(state.passcodeCheckStatus)
                                }
                            }

                            else -> {}
                        }
                    }

                    when (options) {
                        is PasscodeAction.EnterPasscode -> EnterPasscodeScreen(
                            state = state, options = options, onEvent = viewModel::onEvent
                        )

                        is PasscodeAction.SetupPasscode -> SetupPasscodeScreen(
                            state = state, options = options, onEvent = viewModel::onEvent
                        )

                        is PasscodeAction.EditPasscode -> EditPasscodeScreen(
                            state = state, onEvent = viewModel::onEvent
                        )

                        is PasscodeAction.DeletePasscode -> DeletePasscodeScreen(
                            state = state, onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }

    private fun setResultAndFinish(result: PasscodeResult?) {
        setResult(RESULT_OK, Intent().apply { putExtra(STATUS_EXTRA, result) })
        finish()
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

    sealed class PasscodeAction(open val cancellable: Boolean = true) : Serializable {
        data class SetupPasscode(
            val count: Int, val length: Int?, override val cancellable: Boolean = true
        ) : PasscodeAction(cancellable)

        data class EnterPasscode(
            override val cancellable: Boolean = true, val fallbackOnError: Boolean = false
        ) : PasscodeAction(cancellable)

        data class EditPasscode(
            override val cancellable: Boolean = true
        ) : PasscodeAction(cancellable)

        data class DeletePasscode(
            override val cancellable: Boolean = true
        ) : PasscodeAction(cancellable)
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