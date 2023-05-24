package dev.zabolotskikh.authguard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.domain.model.PasscodeOptions
import dev.zabolotskikh.authguard.domain.model.PasscodeResult
import dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.PasscodeButtons
import dev.zabolotskikh.authguard.ui.theme.AuthGuardTheme
import java.io.Serializable

class PasscodeActivity : ComponentActivity() {
    private inline fun <reified T : Serializable> Intent.getExtra(name: String): T? {
        return if (Build.VERSION.SDK_INT >= 33) getSerializableExtra(name, T::class.java)
        else getSerializableExtra(name) as T?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = intent.getExtra<PasscodeOptions>(OPTIONS_EXTRA)
            ?: throw IllegalArgumentException()


        val code = "123456"
        setContent {
            AuthGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PasscodeButtons(
                        modifier = Modifier.fillMaxSize(),
                        title = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Введите код: $code",
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Options: $options",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                        },
                        onSubmit = {
                            if (it == code) {
                                setResult(Activity.RESULT_OK, Intent().apply {
                                    putExtra(STATUS_EXTRA, PasscodeResult.Succeed)
                                })
                                finish()
                            } else {
                                setResult(Activity.RESULT_OK, Intent().apply {
                                    putExtra(STATUS_EXTRA, PasscodeResult.LimitReached)
                                })
                                finish()
                            }
                        }
                    )
                }
            }
        }
    }

    class PasscodeResultContract : ActivityResultContract<PasscodeOptions, PasscodeResult?>() {
        @CallSuper
        override fun createIntent(context: Context, input: PasscodeOptions): Intent {
            return Intent(context, PasscodeActivity::class.java).apply {
                putExtra(OPTIONS_EXTRA, input)
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: PasscodeOptions
        ): SynchronousResult<PasscodeResult?>? = null

        override fun parseResult(resultCode: Int, intent: Intent?): PasscodeResult? {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                val serializableExtra = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getSerializableExtra(
                        STATUS_EXTRA,
                        PasscodeResult::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    intent.getSerializableExtra(STATUS_EXTRA) as PasscodeResult
                }
                return serializableExtra
            }
            return null
        }
    }

    companion object {
        private const val STATUS_EXTRA = "status"
        private const val OPTIONS_EXTRA = "options"


    }
}