package com.example.pockit

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pockit.ui.theme.BrandGreen
import com.example.pockit.ui.theme.BrandGreenEnd
import com.example.pockit.ui.theme.BrandTealStart
import com.example.pockit.ui.theme.DarkBackground
import com.example.pockit.ui.theme.TextPrimary
import com.example.pockit.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {

    // ─── 1. ENTRANCE ANIMATIONS (run once) ──────────────────────────────────

    val logoScale = remember { Animatable(0.4f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(20f) }
    val subtitleAlpha = remember { Animatable(0f) }

    // ─── 2. FLOAT ANIMATION (loops forever after entrance) ──────────────────

    /**
     * floatOffset drives the continuous up/down bobbing of the logo.
     *
     * - infiniteRepeatable: loops forever until the composable leaves composition
     * - keyframes: gives us precise control — goes UP first, then DOWN, then back
     * - durationMillis 2000: one full bob cycle = 2 seconds
     * - easing EaseInOutSine: smooth acceleration/deceleration, feels organic
     *
     * The value oscillates between -10f (10dp up) and +10f (10dp down).
     * We use keyframes instead of tween so we can tune the midpoint timing.
     */
    val infiniteTransition = rememberInfiniteTransition(label = "logo_float")

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,   // Returns to 0 so the loop is seamless
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0f   at 0    using EaseInOutSine    // Start at rest
                -10f at 500  using EaseInOutSine    // Move UP 10dp
                0f   at 1000 using EaseInOutSine    // Return to center
                10f  at 1500 using EaseInOutSine    // Move DOWN 10dp
                0f   at 2000 using EaseInOutSine    // Back to rest → loops
            },
            repeatMode = RepeatMode.Restart         // Seamless restart
        ),
        label = "float_y"
    )

    // ─── 3. ENTRANCE SEQUENCE (coroutine) ───────────────────────────────────

    /**
     * LaunchedEffect(Unit): runs once when this composable enters composition.
     * All animations are sequential — each suspend call waits to complete
     * before the next one starts, giving us a choreographed entrance.
     */
    LaunchedEffect(Unit) {
        // Logo pops in with spring bounce
        logoAlpha.animateTo(1f, tween(200))
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )

        // Text slides up and fades in
        delay(150)
        textAlpha.animateTo(1f, tween(400))
        textOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(400, easing = EaseOutCubic)
        )

        // Subtitle fades in last
        delay(100)
        subtitleAlpha.animateTo(1f, tween(500))

        // Hold, then navigate
        delay(1800)
        onSplashComplete()
    }

    // ─── 4. UI LAYOUT ───────────────────────────────────────────────────────

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // ── Logo Box: entrance scale/alpha + continuous float ───────────
            /**
             * We layer TWO independent transforms on the logo:
             *
             * 1. .scale(logoScale.value) + .alpha(logoAlpha.value)
             *    → One-time entrance animation (pop in from 40% → 100%)
             *
             * 2. .offset(y = floatOffset.dp)
             *    → Continuous infinite float (up/down bobbing)
             *
             * The offset() modifier is applied OUTSIDE scale/alpha so the
             * floating happens in layout space — the logo physically moves
             * up and down in the column, which looks more natural than a
             * transform-only movement.
             */
            Box(
                modifier = Modifier
                    .offset(y = floatOffset.dp)          // 🔁 Continuous float
                    .scale(logoScale.value)              // ✨ Entrance scale
                    .alpha(logoAlpha.value)              // ✨ Entrance fade
                    .size(140.dp),   // increased from 90.dp
                contentAlignment = Alignment.Center
            ) {
                /**
                 * painterResource() loads your drawable.
                 * Replace R.drawable.pockit_logo with your actual file name.
                 *
                 * contentDescription = null is correct here — this is a
                 * decorative brand logo on a splash screen, not interactive.
                 */
                Image(
                    painter = painterResource(id = R.drawable.pockit_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .offset(y = floatOffset.dp)   // Float still works the same
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // ── App Name + Tagline ─────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = textOffsetY.value.dp)
            ) {
                Text(
                    text = "Pockit",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Track. Budget. Grow.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandGreen,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Subtitle ───────────────────────────────────────────────────
            Text(
                text = "Your personal finance operating system.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(subtitleAlpha.value)
                    .padding(horizontal = 48.dp),
                lineHeight = 20.sp
            )
        }
    }
}