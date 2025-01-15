package org.ic.tech.music.presentation.auth.widgets

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ic.tech.music.design.theme.AppTheme
import org.ic.tech.music.design.widgets.base.BaseButton


@Composable
fun SocialButton(
    text: String,
    size: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    BaseButton(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
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
fun SocialButtons() {
    SocialButton(
        text = "Continue with Google",
        icon = org.ic.tech.music.R.drawable.google,
        size = 32,
        onClick = {}
    )

    SocialButton(
        text = "Continue with Apple",
        icon = org.ic.tech.music.R.drawable.apple,
        size = 36,
        onClick = {}
    )
}