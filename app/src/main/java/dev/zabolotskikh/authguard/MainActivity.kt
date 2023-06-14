package dev.zabolotskikh.authguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.ui.MyAppNavHost
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeViewModel
import dev.zabolotskikh.authguard.ui.theme.AuthGuardTheme
import dev.zabolotskikh.passlock.ui.provider.PassLockProvider

@AndroidEntryPoint
@ExperimentalGetImage
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AuthGuardTheme {
                PassLockProvider {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val viewModel = hiltViewModel<WelcomeViewModel>()
                        val state by viewModel.state.collectAsState()
                        splashScreen.setKeepOnScreenCondition { state == null }

                        state?.apply {
                            MyAppNavHost(state = this)
                        }
                    }
                }
            }
        }
    }
}
