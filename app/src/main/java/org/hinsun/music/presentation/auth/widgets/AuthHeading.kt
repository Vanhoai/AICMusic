package org.hinsun.music.presentation.auth.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme

@Composable
fun AuthHeading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_huasun_music),
            contentDescription = null,
            modifier = Modifier
                .width(260.dp)
                .height(60.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome Back",
            style = AppTheme.typography.normal,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Log in your account using social networks or biometrics",
            style = AppTheme.typography.italic,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.textPrimary.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}