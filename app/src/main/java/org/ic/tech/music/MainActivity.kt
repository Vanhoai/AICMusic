package org.ic.tech.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import org.ic.tech.music.design.theme.AICMusicTheme
import org.ic.tech.music.design.theme.AppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AICMusicTheme {
                MainComposable()
            }
        }
    }
}

@Composable
fun MainComposable() {
    val isDarkTheme = AppTheme.isDarkTheme
    val isDarkThemeSystem = isSystemInDarkTheme()

    val darkTheme = remember { mutableStateOf(isDarkTheme) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.getAnimatedBackgroundColor())
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                AppTheme.updateTheme(isDarkTheme = isDarkThemeSystem)
            }) {
                Text(
                    text = "System",
                    style = AppTheme.typography.normal,
                    color = AppTheme.colors.getAnimatedTextColor()
                )
            }

            Text(
                text = "Enable Dark Mode",
                style = AppTheme.typography.normal,
                color = AppTheme.colors.getAnimatedTextColor()
            )

            Switch(
                checked = darkTheme.value,
                onCheckedChange = {
                    darkTheme.value = it
                    AppTheme.updateTheme(isDarkTheme = it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppTheme.colors.getAnimatedTextColor(),
                    uncheckedThumbColor = AppTheme.colors.getAnimatedTextColor()
                )
            )
        }
    }
}