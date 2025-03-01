package com.example.androidtermprojectmotopedia.ui.backgrounds

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * 1) WaveBackground - UNCHANGED
 */
@Composable
fun WaveBackground(
    modifier: Modifier = Modifier
) {
    val startColor = MaterialTheme.colorScheme.surface
    val endColor = MaterialTheme.colorScheme.primary

    // Create an infinite transition for continuous animation
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")

    // Animate the control points of the cubic bezier curve
    val animationProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_progress"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Use the animation value to adjust control points
            val animatedOffset = 0.1f * sin(animationProgress.value * 2 * PI.toFloat())

            val path = Path().apply {
                moveTo(0f, 0f)
                cubicTo(
                    w * (0.2f + animatedOffset), h * (0.25f - animatedOffset),
                    w * (0.8f - animatedOffset), h * (0.75f + animatedOffset),
                    w, h
                )
                lineTo(0f, h)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(listOf(startColor, endColor))
            )
        }
    }
}

/**
 * 2) LayeredWavesBackground - UNCHANGED
 */
@Composable
fun LayeredWavesBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    accentColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Base golden gradient background
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor,
                        accentColor.copy(alpha = 0.7f)
                    ),
                    start = Offset(w * 0.2f, 0f),
                    end = Offset(w * 0.8f, h)
                )
            )

            // Top curved wave - black area with clean curve
            val topWave = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                quadraticBezierTo(w * 0.5f, h * 0.45f, 0f, h * 0.25f)
                close()
            }

            drawPath(
                path = topWave,
                color = primaryColor
            )

            // Bottom wave - subtle complementary wave
            val bottomWave = Path().apply {
                moveTo(0f, h * 0.75f)
                quadraticBezierTo(w * 0.5f, h * 0.6f, w, h * 0.85f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            drawPath(
                path = bottomWave,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.4f),
                        accentColor.copy(alpha = 0.6f)
                    ),
                    start = Offset(0f, h * 0.7f),
                    end = Offset(w, h)
                )
            )
        }
    }
}

/**
 * 3) DiagonalGradientBackground - new single wave shape w/ vertical gradient
 */
@Composable
fun DiagonalGradientBackground(
    modifier: Modifier = Modifier
) {
    val startColor = MaterialTheme.colorScheme.background
    val endColor = MaterialTheme.colorScheme.primary

    // Create infinite transition for animation
    val infiniteTransition = rememberInfiniteTransition(label = "diagonal_wave")

    // Primary animation for wave movement
    val waveProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_progress"
    )

    // Secondary animation for additional movement
    val secondaryProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secondary_progress"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Calculate animated offsets for control points
            val xOffset1 = 0.05f * sin(waveProgress.value * 2 * PI.toFloat())
            val yOffset1 = 0.04f * cos(secondaryProgress.value * 2 * PI.toFloat())
            val xOffset2 = 0.04f * sin((secondaryProgress.value + 0.25f) * 2 * PI.toFloat())
            val yOffset2 = 0.06f * cos((waveProgress.value + 0.5f) * 2 * PI.toFloat())

            // Create animated path with subtle movement
            val path = Path().apply {
                moveTo(0f, 0f)
                cubicTo(
                    w * (0.3f + xOffset1), h * (0.1f + yOffset1),
                    w * (0.7f - xOffset2), h * (0.5f - yOffset2),
                    w, h * (0.4f + yOffset1)
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            // Create a second subtle wave path for layered effect
            val secondaryPath = Path().apply {
                moveTo(0f, h * 0.1f)
                cubicTo(
                    w * (0.25f - xOffset2), h * (0.3f - yOffset1),
                    w * (0.6f + xOffset1), h * (0.6f + yOffset2),
                    w, h * (0.7f - yOffset2)
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            // Draw secondary path with partial transparency
            drawPath(
                path = secondaryPath,
                brush = Brush.verticalGradient(
                    listOf(
                        startColor.copy(alpha = 0.4f),
                        endColor.copy(alpha = 0.7f)
                    )
                )
            )

            // Draw main path
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    listOf(startColor, endColor),
                    startY = 0f,
                    endY = size.height * 0.8f
                )
            )
        }
    }
}

/**
 * 4) CurlyLineBackground - new single wave shape w/ vertical gradient
 */
@Composable
fun CurlyLineBackground(
    modifier: Modifier = Modifier
) {
    val color1 = MaterialTheme.colorScheme.surface
    val color2 = MaterialTheme.colorScheme.tertiary

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // A "curly" wave
            val path = Path().apply {
                moveTo(0f, 0f)
                cubicTo(w * 0.2f, h * 0.3f, w * 0.4f, h * 0.1f, w, h * 0.4f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(listOf(color1, color2))
            )
        }
    }
}

/**
 * 5) CurlyLineBackgroundVariant1 - "bubble" version w/ vertical gradient
 */
@Composable
fun CurlyLineBackgroundVariant1(
    modifier: Modifier = Modifier,
    gradientStartColor: Color = MaterialTheme.colorScheme.background,
    gradientEndColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
) {
    // Create infinite transition for continuous animation
    val infiniteTransition = rememberInfiniteTransition(label = "waves_animation")

    // Create two animation values with different speeds for each wave
    val mainWaveAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "main_wave"
    )

    val secondaryWaveAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secondary_wave"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Create animated control points for main wave
            val mainWavePhase = mainWaveAnimation.value * 2 * PI.toFloat()
            val mainControlX1 = w * (0.25f + sin(mainWavePhase) * 0.05f)
            val mainControlY1 = h * (0.1f + cos(mainWavePhase) * 0.05f)
            val mainControlX2 = w * (0.7f - sin(mainWavePhase) * 0.05f)
            val mainControlY2 = h * (0.5f + cos(mainWavePhase) * 0.05f)

            // Create animated control points for secondary wave (different phase)
            val secondaryWavePhase = secondaryWaveAnimation.value * 2 * PI.toFloat()
            val secondaryControlX1 = w * (0.15f + sin(secondaryWavePhase) * 0.05f)
            val secondaryControlY1 = h * (0.35f - cos(secondaryWavePhase) * 0.05f)
            val secondaryControlX2 = w * (0.6f - sin(secondaryWavePhase) * 0.03f)
            val secondaryControlY2 = h * (0.6f - cos(secondaryWavePhase) * 0.04f)

            // Create multiple flowing wave paths with animation
            val mainWavePath = Path().apply {
                moveTo(0f, h * 0.2f)
                cubicTo(
                    mainControlX1, mainControlY1,
                    mainControlX2, mainControlY2,
                    w, h * 0.3f
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            val secondaryWavePath = Path().apply {
                moveTo(0f, h * 0.3f)
                cubicTo(
                    secondaryControlX1, secondaryControlY1,
                    secondaryControlX2, secondaryControlY2,
                    w, h * 0.5f
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            // Draw main gradient wave
            drawPath(
                path = mainWavePath,
                brush = Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor),
                    startY = 0f,
                    endY = h * 0.8f
                )
            )

            // Draw a subtle secondary wave with translucent color
            drawPath(
                path = secondaryWavePath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        gradientEndColor.copy(alpha = 0.2f),
                        gradientEndColor.copy(alpha = 0.4f)
                    ),
                    startY = h * 0.3f,
                    endY = h
                )
            )
        }
    }
}

/**
 * 6) CurlyLineBackgroundVariant2 - single wave path w/ vertical gradient
 */
@Composable
fun CurlyLineBackgroundVariant2(
    modifier: Modifier = Modifier
) {
    // Create multiple animation parameters with slower timing
    val infiniteTransition = rememberInfiniteTransition(label = "curly_variant2")

    // Primary wave animation - slowed down
    val primaryWave = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "primary_wave"
    )

    // Secondary wave animation - slowed down
    val secondaryWave = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secondary_wave"
    )

    // Tertiary wave animation - slowed down
    val tertiaryWave = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tertiary_wave"
    )

    val startColor = MaterialTheme.colorScheme.surface
    val endColor = MaterialTheme.colorScheme.secondary

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Calculate more gentle dynamic offsets
            val xOffset1 = 0.08f * sin(primaryWave.value * 2 * PI.toFloat())
            val yOffset1 = 0.06f * cos(secondaryWave.value * 2 * PI.toFloat())
            val xOffset2 = 0.05f * sin((tertiaryWave.value + 0.5f) * 2 * PI.toFloat())
            val yOffset2 = 0.08f * cos((primaryWave.value + 0.25f) * 2 * PI.toFloat())

            // Create multiple paths for layered effect
            val mainPath = Path().apply {
                moveTo(0f, h * (0.2f + yOffset1))
                cubicTo(
                    w * (0.3f + xOffset1), h * (0.1f - yOffset1),
                    w * (0.6f - xOffset2), h * (0.4f + yOffset2),
                    w, h * (0.3f - yOffset2)
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            val secondaryPath = Path().apply {
                moveTo(0f, h * (0.4f - yOffset2))
                cubicTo(
                    w * (0.25f - xOffset2), h * (0.2f + yOffset1),
                    w * (0.5f + xOffset1), h * (0.5f - yOffset1),
                    w, h * (0.6f + yOffset2)
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            // Draw background layers with different opacities
            drawPath(
                path = secondaryPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        startColor.copy(alpha = 0.3f),
                        endColor.copy(alpha = 0.6f)
                    )
                )
            )

            // Draw main path
            drawPath(
                path = mainPath,
                brush = Brush.verticalGradient(
                    colors = listOf(startColor, endColor),
                    startY = 0f,
                    endY = size.height * 0.8f
                )
            )

            // Add very subtle ripple effect with slower animation
            val rippleSize = 40f + 10f * sin(tertiaryWave.value * PI.toFloat())
            val rippleCount = 3 // Reduced number of ripples
            val rippleOpacity = 0.08f + 0.03f * sin(primaryWave.value * 2 * PI.toFloat())

            for (i in 0 until rippleCount) {
                val rippleX = w * (0.3f + 0.4f * i / rippleCount + 0.03f * sin((primaryWave.value + i * 0.2f) * 2 * PI.toFloat()))
                val rippleY = h * (0.3f + 0.03f * cos((secondaryWave.value + i * 0.2f) * 2 * PI.toFloat()))

                drawCircle(
                    color = endColor.copy(alpha = rippleOpacity * (1f - i.toFloat() / rippleCount)),
                    radius = rippleSize * (1f + i * 0.4f),
                    center = Offset(rippleX, rippleY),
                    style = Stroke(width = 1.5f)
                )
            }
        }
    }
}
/**
 * 7) CurlyLineBackgroundVariant3 - single wave path w/ vertical gradient
 */
@Composable
fun CurlyLineBackgroundVariant3(
    modifier: Modifier = Modifier
) {
    val startColor = MaterialTheme.colorScheme.background
    val endColor = MaterialTheme.colorScheme.tertiary

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val path = Path().apply {
                moveTo(0f, 0f)
                cubicTo(w * 0.25f, h * 0.3f, w * 0.75f, h * 0.1f, w, h * 0.3f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(listOf(startColor, endColor))
            )
        }
    }
}

@Composable
fun AnimatedLayeredWavesBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    accentColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
    animationDurationMillis: Int = 10000
) {
    // Animation progress
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    val waveAnimationProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_progress"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Animation offset for waves
            val animOffset = waveAnimationProgress.value * w * 0.4f

            // Base golden gradient background
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor,
                        accentColor.copy(alpha = 0.7f)
                    ),
                    start = Offset(w * 0.2f, 0f),
                    end = Offset(w * 0.8f, h)
                )
            )

            // Top curved wave - black area with clean curve
            val topWave = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                // Animate control point X position for top wave
                val controlX = w * 0.5f + (sin(waveAnimationProgress.value * 2 * PI.toFloat()) * w * 0.1f)
                quadraticBezierTo(controlX, h * 0.45f, 0f, h * 0.25f)
                close()
            }

            drawPath(
                path = topWave,
                color = primaryColor
            )

            // Bottom wave - subtle complementary wave with different animation
            val bottomWave = Path().apply {
                moveTo(0f, h * 0.75f)
                // Animate control point X position for bottom wave, offset phase from top wave
                val controlX = w * 0.5f + (sin((waveAnimationProgress.value + 0.5f) * 2 * PI.toFloat()) * w * 0.15f)
                quadraticBezierTo(controlX.toFloat(), h * 0.6f, w, h * 0.85f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            drawPath(
                path = bottomWave,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.4f),
                        accentColor.copy(alpha = 0.6f)
                    ),
                    start = Offset(animOffset, h * 0.7f), // Animate gradient start position
                    end = Offset(w - animOffset, h) // Animate gradient end position
                )
            )

            // Additional subtle wave that moves in the middle section
            val middleWave = Path().apply {
                val baseY = h * 0.5f
                val amplitude = h * 0.05f
                val waveOffset = sin(waveAnimationProgress.value * 2 * PI.toFloat()) * amplitude

                moveTo(0f, (baseY + waveOffset).toFloat())

                // Create a smooth sine-like wave across the width
                for (i in 1..20) {
                    val x = w * (i / 20f)
                    val phase = waveAnimationProgress.value * 2 * PI.toFloat() + (i / 5f)
                    val y = baseY + sin(phase) * amplitude
                    lineTo(x, y)
                }

                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            drawPath(
                path = middleWave,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.2f),
                        accentColor.copy(alpha = 0.3f)
                    ),
                    start = Offset(w - animOffset, h * 0.5f),
                    end = Offset(animOffset, h * 0.8f)
                )
            )
        }
    }
}

@Composable
fun ProfilePageAnimatedBackground(
    modifier: Modifier = Modifier
) {
    // Custom color scheme optimized for profile page
    val goldColor = MaterialTheme.colorScheme.primary
    val creamColor = MaterialTheme.colorScheme.background
    val accentColor = Color(0xFFE25822) // Orange accent inspired by KTM

    // Create animation controllers
    val infiniteTransition = rememberInfiniteTransition(label = "profile_bg")

    // Primary animation - slow wave movement
    val primaryWave = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "primary_profile_wave"
    )

    // Secondary subtle pulsing effect
    val pulseEffect = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "profile_pulse"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Calculate smooth wave offsets
            val waveOffset = 0.05f * sin(primaryWave.value * 2 * PI.toFloat())
            val pulseScale = 0.02f * sin(pulseEffect.value * PI.toFloat() * 2) + 1f

            // Create main curved background
            val mainPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w, h * 0.2f)
                cubicTo(
                    w * (0.8f - waveOffset), h * (0.18f + waveOffset),
                    w * (0.5f + waveOffset), h * (0.22f - waveOffset),
                    0f, h * 0.2f
                )
                close()
            }

            // Create content area background with subtle animation
            val contentPath = Path().apply {
                moveTo(0f, h * 0.2f)
                cubicTo(
                    w * (0.5f + waveOffset), h * (0.22f - waveOffset),
                    w * (0.8f - waveOffset), h * (0.18f + waveOffset),
                    w, h * 0.2f
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            // Draw decorative motorcycle-inspired accent lines
            val accentPath1 = Path().apply {
                moveTo(0f, h * (0.8f - waveOffset * 0.5f))
                cubicTo(
                    w * 0.2f, h * (0.78f + waveOffset),
                    w * 0.5f, h * (0.82f - waveOffset),
                    w, h * (0.8f + waveOffset * 0.5f)
                )
            }

            val accentPath2 = Path().apply {
                moveTo(0f, h * (0.85f + waveOffset * 0.5f))
                cubicTo(
                    w * 0.3f, h * (0.83f - waveOffset),
                    w * 0.7f, h * (0.87f + waveOffset),
                    w, h * (0.85f - waveOffset * 0.5f)
                )
            }

            // Draw header background
            drawPath(
                path = mainPath,
                brush = Brush.linearGradient(
                    colors = listOf(goldColor, goldColor.copy(alpha = 0.9f)),
                    start = Offset(w * 0.5f, 0f),
                    end = Offset(w * 0.5f, h * 0.2f)
                )
            )

            // Draw content area background
            drawPath(
                path = contentPath,
                color = creamColor
            )

            // Draw accent curves that look like motorcycle race track or speed lines
            drawPath(
                path = accentPath1,
                color = accentColor.copy(alpha = 0.1f),
                style = Stroke(width = 12f * pulseScale)
            )

            drawPath(
                path = accentPath2,
                color = accentColor.copy(alpha = 0.07f),
                style = Stroke(width = 16f * pulseScale)
            )

            // Draw subtle circular highlights that could represent headlights or wheels
            val circle1Center = Offset(w * 0.15f, h * 0.9f)
            val circle2Center = Offset(w * 0.85f, h * 0.92f)

            drawCircle(
                color = accentColor.copy(alpha = 0.06f),
                radius = 60f * pulseScale,
                center = circle1Center
            )

            drawCircle(
                color = accentColor.copy(alpha = 0.05f),
                radius = 70f * pulseScale,
                center = circle2Center
            )
        }
    }
}

@Composable
fun UploadPageAnimatedBackground(
    modifier: Modifier = Modifier
) {
    // Custom color scheme optimized for upload page
    val goldColor = MaterialTheme.colorScheme.primary
    val creamColor = MaterialTheme.colorScheme.background
    val accentBlue = Color(0xFF1E3F66) // Deep blue accent for upload theme

    // Create animation controllers
    val infiniteTransition = rememberInfiniteTransition(label = "upload_bg")

    // Slow gradient shift animation
    val gradientShift = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_shift"
    )

    // Upload progress animation effect
    val uploadEffect = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "upload_effect"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Calculate animated values
            val waveOffset = 0.03f * sin(gradientShift.value * 2 * PI.toFloat())
            val progressY = h * uploadEffect.value

            // Create header area
            val headerPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w, h * 0.2f)
                cubicTo(
                    w * (0.7f - waveOffset), h * (0.19f + waveOffset),
                    w * (0.3f + waveOffset), h * (0.21f - waveOffset),
                    0f, h * 0.2f
                )
                close()
            }

            // Create content area with subtle curve
            val contentPath = Path().apply {
                moveTo(0f, h * 0.2f)
                cubicTo(
                    w * (0.3f + waveOffset), h * (0.21f - waveOffset),
                    w * (0.7f - waveOffset), h * (0.19f + waveOffset),
                    w, h * 0.2f
                )
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }

            // Create vertical "upload progress" indicator paths
            val uploadLine1 = Path().apply {
                moveTo(w * 0.2f, h * 0.25f)
                lineTo(w * 0.2f, h * 0.25f + progressY * 0.7f)
            }

            val uploadLine2 = Path().apply {
                moveTo(w * 0.8f, h * 0.25f)
                lineTo(w * 0.8f, h * 0.25f + progressY * 0.7f)
            }

            // Draw header background with gold gradient
            drawPath(
                path = headerPath,
                brush = Brush.linearGradient(
                    colors = listOf(goldColor, goldColor.copy(alpha = 0.9f)),
                    start = Offset(w * gradientShift.value, 0f),
                    end = Offset(w * (1f - gradientShift.value), h * 0.2f)
                )
            )

            // Draw content area with cream color
            drawPath(
                path = contentPath,
                color = creamColor
            )

            // Draw vertical upload indicator lines with animated progress
            drawPath(
                path = uploadLine1,
                color = accentBlue.copy(alpha = 0.1f),
                style = Stroke(width = 8f)
            )

            drawPath(
                path = uploadLine2,
                color = accentBlue.copy(alpha = 0.1f),
                style = Stroke(width = 8f)
            )

            // Draw connecting diagonal lines - motorcycle inspired elements
            for (i in 0 until 5) {
                val progress = (uploadEffect.value + i * 0.2f) % 1f
                val startY = h * 0.3f + progress * h * 0.6f

                if (startY < h * 0.9f) {
                    drawLine(
                        color = accentBlue.copy(alpha = 0.05f * (1f - progress)),
                        start = Offset(w * 0.2f, startY),
                        end = Offset(w * 0.8f, startY - h * 0.05f),
                        strokeWidth = 3f
                    )
                }
            }

            // Draw subtle grid pattern for form fields
            val formFieldY = listOf(0.35f, 0.45f, 0.65f, 0.8f)
            formFieldY.forEach { y ->
                drawLine(
                    color = accentBlue.copy(alpha = 0.03f),
                    start = Offset(w * 0.15f, h * y),
                    end = Offset(w * 0.85f, h * y),
                    strokeWidth = 2f
                )
            }
        }
    }
}