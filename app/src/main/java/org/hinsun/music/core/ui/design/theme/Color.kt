package org.hinsun.music.core.ui.design.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import timber.log.Timber

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ColorProperty(
    val duration: Int = 500,
    val label: String = "ColorProperty"
)

class AnimatedPropertyProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ColorProperty::class.qualifiedName!!)
            .filterIsInstance<KSPropertyDeclaration>()

        symbols.forEach { property ->
            val packageName = property.packageName.asString()
            val className = property.parentDeclaration?.qualifiedName?.asString() ?: return@forEach

            val fileName = "${className}_${property.simpleName.asString()}_Generated"

            val str = "Package: $packageName\n" +
                    "Class: $className\n" +
                    "Property: ${property.simpleName.asString()}\n" +
                    "File: $fileName"

            logger.info(str)

            codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = packageName,
                fileName = fileName
            ).use { output ->
                output.write(str.toByteArray())
            }
        }

        return emptyList()
    }
}

class AnimatedPropertyProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AnimatedPropertyProcessor(environment.codeGenerator, environment.logger)
    }
}

// Colors
val darkColors = Color(0xFF242424)

@Stable
class ICMusicColors(
    backgroundPrimary: Color = darkColors,
    textPrimary: Color = Color.White,
    isAnimated: Boolean = false
) {
    private var isAnimated by mutableStateOf(isAnimated)

    private var _backgroundPrimary by mutableStateOf(backgroundPrimary)
    private var _textPrimary by mutableStateOf(textPrimary)

    @ColorProperty(duration = 500, label = "PrimaryColor")
    val primaryColor: Color = Color.Yellow

    val backgroundPrimary: Color
        @Composable
        get() = if (isAnimated) getAnimatedBackgroundColor() else _backgroundPrimary

    val textPrimary: Color
        @Composable
        get() = if (isAnimated) getAnimatedTextColor() else _textPrimary

    @Composable
    fun getAnimatedBackgroundColor(): Color {
        return animateColorAsState(
            label = "Animate Background Color",
            targetValue = _backgroundPrimary,
            animationSpec = tween(durationMillis = 500)
        ).value
    }

    @Composable
    fun getAnimatedTextColor(): Color {
        return animateColorAsState(
            label = "Animate Text Color",
            targetValue = _textPrimary,
            animationSpec = tween(durationMillis = 500)
        ).value
    }


    fun copy(): ICMusicColors {
        return ICMusicColors(_backgroundPrimary, _textPrimary)
    }

    fun updateLightTheme() {
        _backgroundPrimary = Color.White
        _textPrimary = darkColors
    }

    fun updateDarkTheme() {
        _backgroundPrimary = darkColors
        _textPrimary = Color.White
    }

    fun updateIsAnimated(isAnimated: Boolean) {
        this.isAnimated = isAnimated
    }
}

val LocalColors = staticCompositionLocalOf<ICMusicColors> {
    error("Colors not initialized")
}