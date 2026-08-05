package com.techun.dev.todoapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.techun.dev.todoapp.core.nav3.NavWrapper
import com.techun.dev.todoapp.splash.SplashScreenViewModel
import com.techun.dev.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    private val splashScreenViewModel: SplashScreenViewModel by lazy {
        ViewModelProvider(this@MainActivity)[SplashScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition { splashScreenViewModel.isSplashScreenVisible.value }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splash.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.view.animate()
                    .alpha(0f)
                    .scaleX(1.4f)
                    .scaleY(1.4f)
                    .setDuration(300)
                    .withEndAction { splashScreenView.remove() }
                    .start()
            }
        }

        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                NavWrapper()
            }
        }
    }
}
