package org.hinsun.music.presentation.swipe.more.authentication

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.hinsun.music.core.ui.design.widgets.base.BaseScaffold
import org.hinsun.music.core.ui.design.widgets.shared.SharedTopBar
import org.hinsun.music.presentation.swipe.more.widgets.buildEnableBiometric

@Composable
fun AuthenticationView(
    navHostController: NavHostController,
    viewModel: AuthenticationViewModel = hiltViewModel<AuthenticationViewModel>()
) {
    val scrollState = rememberScrollState()
    val isEnableBiometric = remember { mutableStateOf(viewModel.loadIsEnableBiometric()) }

    BaseScaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            item {
                SharedTopBar(
                    name = "Authentication",
                    onBackPress = { navHostController.popBackStack() }
                )
            }

            buildEnableBiometric(
                isEnableBiometric = isEnableBiometric.value,
                onChangeEnableBiometric = {
                    isEnableBiometric.value = it
                }
            )
        }
    }
}