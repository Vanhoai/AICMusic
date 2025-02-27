package org.hinsun.music.core.ui.design.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.hinsun.music.R

val fontFamily = FontFamily(
    Font(R.font.ibm_flex_mono_light, weight = FontWeight.Light),
    Font(R.font.ibm_flex_mono_regular, weight = FontWeight.Normal),
    Font(R.font.ibm_flex_mono_medium, weight = FontWeight.Medium),
    Font(R.font.ibm_flex_mono_bold, weight = FontWeight.Bold),
    Font(R.font.ibm_flex_mono_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
)

val Typography = ICMusicTypography(
    normal = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
    ),
    italic = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic
    ),
)

@Immutable
data class ICMusicTypography(
    val normal: TextStyle,
    val italic: TextStyle
)

inline val LocalTypography
    get() = staticCompositionLocalOf { Typography }
