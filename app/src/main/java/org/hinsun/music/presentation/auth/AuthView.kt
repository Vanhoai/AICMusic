package org.hinsun.music.presentation.auth

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.shared.SharedWaveAnimation
import org.hinsun.music.presentation.auth.widgets.AuthHeading
import org.hinsun.music.presentation.auth.widgets.BiometricButton
import org.hinsun.music.presentation.auth.widgets.SocialButtons

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthView(
    navHostController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>()
) {
    val context = LocalContext.current as FragmentActivity

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AuthHeading()
            SocialButtons(
                onGoogleSignIn = { viewModel.signIn(context) },
                onAppleSignIn = {}
            )

            BiometricButton(onPress = {
                viewModel.register(
                    context = context,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Register successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            })

            BiometricButton(onPress = {
                viewModel.authenticate(
                    context = context,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Authentication successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            })

            Box(
                modifier = Modifier.weight(1f)
            ) {
                SharedWaveAnimation()
            }
        }
    }
}