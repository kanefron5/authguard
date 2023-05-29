package dev.zabolotskikh.authguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.zabolotskikh.authguard.ui.MyAppNavHost
import dev.zabolotskikh.authguard.ui.theme.AuthGuardTheme
import dev.zabolotskikh.passlock.ui.provider.PassLockProvider

@AndroidEntryPoint
@ExperimentalGetImage
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthGuardTheme {
                PassLockProvider {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MyAppNavHost()
                    }
                }
            }
        }
    }
}
