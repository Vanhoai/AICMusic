package org.ic.tech.music.presentation.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import org.ic.tech.music.R
import org.ic.tech.music.design.widgets.base.BaseScaffold
import org.ic.tech.music.design.widgets.shared.SharedWaveAnimation
import org.ic.tech.music.presentation.auth.widgets.AuthHeading
import org.ic.tech.music.presentation.auth.widgets.BiometricButton
import org.ic.tech.music.presentation.auth.widgets.SocialButtons

@Composable
fun AuthView(navHostController: NavHostController) {
    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AuthHeading()
            SocialButtons()
            BiometricButton()

            Box(
                modifier = Modifier.weight(1f)
            ) {
                SharedWaveAnimation()
            }
        }
    }
}