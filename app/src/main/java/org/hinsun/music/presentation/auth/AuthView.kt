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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.providers.GlobalLoadingProvider
import org.hinsun.music.design.widgets.providers.LocalGlobalLoadingState
import org.hinsun.music.design.widgets.shared.SharedWaveAnimation
import org.hinsun.music.presentation.auth.widgets.AuthHeading
import org.hinsun.music.presentation.auth.widgets.BiometricButton
import org.hinsun.music.presentation.auth.widgets.SocialButtons
import org.hinsun.music.presentation.graphs.NavRoute

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthView(
    navHostController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>()
) {
    val loadingStateHolder = LocalGlobalLoadingState.current
    val context = LocalContext.current as FragmentActivity
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.value.isLoading) {
        loadingStateHolder.setGlobalLoading(uiState.value.isLoading)
    }

    LaunchedEffect(uiState.value.isSignInSuccess) {
        if (uiState.value.isSignInSuccess) {
            delay(1000)
            navHostController.navigate(NavRoute.SWIPE.path)
        }
    }

    fun signIn() {
        scope.launch {
            delay(1000)
            navHostController.navigate(NavRoute.SWIPE.path)
        }
    }

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AuthHeading()
            SocialButtons(
                onGoogleSignIn = { signIn() },
                onAppleSignIn = {
                    scope.launch {
                        loadingStateHolder.setGlobalLoading(true)
                        delay(2000)
                        loadingStateHolder.setGlobalLoading(false)
                    }
                }
            )

            if (uiState.value.isShowBiometric) {
                BiometricButton(onPress = {
                    viewModel.signInWithBiometric(
                        context = context,
                        onSuccess = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    )
                })
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                SharedWaveAnimation()
            }
        }
    }
}