package org.hinsun.music.presentation.swipe.more.widgets

import androidx.compose.foundation.lazy.LazyListScope
import org.hinsun.music.presentation.swipe.setting.music.widgets.SessionSwitch

fun LazyListScope.buildEnableBiometric(
    isEnableBiometric: Boolean,
    onChangeEnableBiometric: (Boolean) -> Unit
) {
    item {
        SessionSwitch(
            sessionName = "Biometric Authentication",
            nameSwitch = "Enable Biometric",
            description = "When you enable biometric, we'll use email merge with refresh token and generate a unique keypair for you, after we use this keypair to encrypt when sign in.",
            isActive = isEnableBiometric,
            onChange = onChangeEnableBiometric
        )
    }
}