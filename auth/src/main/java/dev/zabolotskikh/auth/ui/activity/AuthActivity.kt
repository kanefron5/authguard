package dev.zabolotskikh.auth.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.zabolotskikh.auth.ui.theme.AuthTheme
import java.io.Serializable

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action =
            intent.getExtra<AuthAction>(ACTION_EXTRA) ?: throw IllegalArgumentException()


        setContent {
            AuthTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavHost(action = action)
                }
            }
        }
    }

    private fun setResultAndFinish(result: AuthResult?) {
        setResult(RESULT_OK, Intent().apply { putExtra(STATUS_EXTRA, result) })
        finish()
    }

    class AuthResultContract : ActivityResultContract<AuthAction, AuthResult?>() {
        @CallSuper
        override fun createIntent(context: Context, input: AuthAction): Intent {
            return Intent(context, AuthActivity::class.java).apply {
                putExtra(ACTION_EXTRA, input)
            }
        }

        override fun getSynchronousResult(
            context: Context, input: AuthAction
        ): SynchronousResult<AuthResult?>? = null

        override fun parseResult(resultCode: Int, intent: Intent?): AuthResult? {
            return intent?.takeIf { resultCode == Activity.RESULT_OK }
                ?.getExtra<AuthResult>(STATUS_EXTRA)
        }
    }

    sealed class AuthAction : Serializable {
        data class SignIn(val name: String = "SignIn") : AuthAction()
        data class SignUp(val name: String = "SignUp") : AuthAction()
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