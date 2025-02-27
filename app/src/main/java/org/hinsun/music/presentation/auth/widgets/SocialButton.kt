package org.hinsun.music.presentation.auth.widgets

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import org.hinsun.music.BuildConfig
import org.hinsun.music.core.ui.design.theme.AppTheme
import org.hinsun.music.core.ui.design.widgets.base.BaseButton


@Composable
fun SocialButton(
    text: String,
    size: Int,
    @DrawableRes icon: Int,
    onPress: () -> Unit
) {
    BaseButton(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        onPress = onPress
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(size.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                style = AppTheme.typography.normal,
                fontSize = 16.sp,
                color = AppTheme.colors.backgroundPrimary
            )
        }
    }
}

@Composable
fun SocialButtons(
    onGoogleSignIn: () -> Unit,
    onAppleSignIn: () -> Unit
) {
    SocialButton(
        text = "Continue with Google",
        icon = org.hinsun.music.R.drawable.google,
        size = 32,
        onPress = onGoogleSignIn
    )

    SocialButton(
        text = "Continue with Apple",
        icon = org.hinsun.music.R.drawable.apple,
        size = 36,
        onPress = onAppleSignIn
    )
}