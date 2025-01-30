package org.hinsun.music.presentation.auth

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.shared.SharedWaveAnimation
import org.hinsun.music.presentation.auth.widgets.AuthHeading
import org.hinsun.music.presentation.auth.widgets.BiometricButton
import org.hinsun.music.presentation.auth.widgets.SocialButtons
import org.hinsun.music.presentation.graphs.NavRoute
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
                onGoogleSignIn = { viewModel.signInWithGoogle(context) },
                onAppleSignIn = {}
            )

            BiometricButton(onPress = {
                viewModel.signInWithBiometric(
                    context = context,
                    onSuccess = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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