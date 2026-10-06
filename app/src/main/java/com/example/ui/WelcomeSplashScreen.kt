package com.example.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

private val SkyCyanTop = Color(0xFF0284C7)
private val BrightAzureMid = Color(0xFF1D4ED8)
private val PlayfulIndigoBottom = Color(0xFF312E81)
private val SunshineYellow = Color(0xFFFFCA28)
private val WarmOrangeAccent = Color(0xFFFF7043)
private val MintEmerald = Color(0xFF10B981)
private val CandyPink = Color(0xFFEC4899)
private val DeepNavyText = Color(0xFF0F172A)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeSplashScreen(
    totalQuestionsCount: Int,
    onStartClicked: () -> Unit,
    onHowToPlayClicked: () -> Unit,
    onStagesClicked: () -> Unit,
    onResultsClicked: () -> Unit,
    onAboutClicked: () -> Unit,
    onQuickTeamMatchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome_animations")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_pulse"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_badge"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SkyCyanTop,
                        BrightAzureMid,
                        PlayfulIndigoBottom
                    )
                )
            )
            .testTag("welcome_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Decorative joyful background bubbles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = SunshineYellow.copy(alpha = 0.16f),
                radius = size.minDimension * 0.38f,
                center = Offset(size.width * 0.12f, size.height * 0.1f)
            )
            drawCircle(
                color = MintEmerald.copy(alpha = 0.15f),
                radius = size.minDimension * 0.42f,
                center = Offset(size.width * 0.9f, size.height * 0.25f)
            )
            drawCircle(
                color = WarmOrangeAccent.copy(alpha = 0.14f),
                radius = size.minDimension * 0.35f,
                center = Offset(size.width * 0.15f, size.height * 0.85f)
            )
            drawCircle(
                color = CandyPink.copy(alpha = 0.12f),
                radius = size.minDimension * 0.25f,
                center = Offset(size.width * 0.85f, size.height * 0.82f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 660.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top School Badge Pill
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 6.dp,
                modifier = Modifier.offset(y = floatOffset.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "شعار مدرسة عيون مصر",
                        tint = BrightAzureMid,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "مدرسة عيون مصر • الصفوف ٤ و ٥ و ٦ الابتدائي",
                        style = MaterialTheme.typography.labelLarge,
                        color = DeepNavyText,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WarmOrangeAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Visual Knowledge Elements Bar (مصباح فكرة، علامة استفهام، أرقام، كتب، كواكب، قطع ألغاز، نجمة البطولة)
            KnowledgeIconsOrbitRow(floatOffset = floatOffset)

            // Centerpiece Hero Illustration Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 3.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(SunshineYellow, Color.White, MintEmerald)
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(195.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_welcome_genie_stage_1791270936592),
                            contentDescription = "مسرح مسابقة عباقرة عيون مصر",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = SunshineYellow,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = DeepNavyText,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "التصفيات: ٥٠ سؤالاً • ٣٠ دقيقة",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DeepNavyText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🏆 عباقرة عيون مصر",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = 32.sp,
                                lineHeight = 40.sp
                            ),
                            color = BrightAzureMid,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("welcome_title_text")
                        )

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "«فكّر… أسرع. اعرف… أكثر. العب… كفريق!»",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF92400E),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .testTag("welcome_slogan_text")
                            )
                        }

                        Text(
                            text = "مسابقة الذكاء والمعرفة وسرعة البديهة والتفكير المنطقي لطلاب مدرسة عيون مصر ($totalQuestionsCount سؤالاً في ٨ مجالات).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF334155),
                            textAlign = TextAlign.Center
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MiniDomainTag("🔬 العلوم (١٠)", Color(0xFFE0F2FE), Color(0xFF0369A1))
                            MiniDomainTag("➗ الرياضيات (٨)", Color(0xFFEDE9FE), Color(0xFF6D28D9))
                            MiniDomainTag("🧩 المنطق (٨)", Color(0xFFE0E7FF), Color(0xFF3730A3))
                            MiniDomainTag("📚 العربي (٦)", Color(0xFFD1FAE5), Color(0xFF047857))
                            MiniDomainTag("🇪🇬 مصر والعالم (٦)", Color(0xFFFEF3C7), Color(0xFFB45309))
                            MiniDomainTag("👀 الملاحظة (٦)", Color(0xFFCCFBF1), Color(0xFF0F766E))
                            MiniDomainTag("🌍 الثقافة (٤)", Color(0xFFFCE7F3), Color(0xFFBE185D))
                            MiniDomainTag("💻 التكنولوجيا (٢)", Color(0xFFCFFAFE), Color(0xFF0E7490))
                        }
                    }
                }
            }

            // 1. Primary Action Button: 🚀 ابدأ المسابقة
            Button(
                onClick = onStartClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SunshineYellow,
                    contentColor = DeepNavyText
                ),
                shape = RoundedCornerShape(22.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .scale(pulseScale)
                    .border(
                        width = 2.5.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .testTag("welcome_start_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = "ابدأ المسابقة",
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "🚀 ابدأ المسابقة",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // 2. Four Official Navigation Buttons Grid:
            // 📖 كيف تلعب؟ | 🏆 مراحل البطولة | 📊 النتائج | ℹ️ عن المسابقة
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WelcomeActionTile(
                        title = "📖 كيف تلعب؟",
                        subtitle = "القواعد وطريقة الفوز",
                        icon = Icons.Default.MenuBook,
                        accentColor = Color(0xFF38BDF8),
                        onClick = onHowToPlayClicked,
                        testTag = "welcome_how_to_play_button",
                        modifier = Modifier.weight(1f)
                    )
                    WelcomeActionTile(
                        title = "🏆 مراحل البطولة",
                        subtitle = "التصفيات والنهائيات",
                        icon = Icons.Default.EmojiEvents,
                        accentColor = SunshineYellow,
                        onClick = onStagesClicked,
                        testTag = "welcome_stages_button",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WelcomeActionTile(
                        title = "📊 النتائج",
                        subtitle = "لوحة الشرف والمتأهلون",
                        icon = Icons.Default.Leaderboard,
                        accentColor = MintEmerald,
                        onClick = onResultsClicked,
                        testTag = "welcome_results_button",
                        modifier = Modifier.weight(1f)
                    )
                    WelcomeActionTile(
                        title = "ℹ️ عن المسابقة",
                        subtitle = "هوية عباقرة عيون مصر",
                        icon = Icons.Default.Info,
                        accentColor = WarmOrangeAccent,
                        onClick = onAboutClicked,
                        testTag = "welcome_about_button",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Secondary quick button for Stage 2 Classroom Team Battle
            Button(
                onClick = onQuickTeamMatchClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.18f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .testTag("welcome_team_mode_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = "تحدي الفرق والفصول",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🏫 البطولة النهائية: مواجهة الفرق والفصول",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun KnowledgeIconsOrbitRow(floatOffset: Float) {
    // مصباح فكرة، علامة استفهام، أرقام، كتب، كواكب، قطع ألغاز، ونجمة البطولة
    val items = listOf(
        Triple(Icons.Default.Lightbulb, "فكرة", Color(0xFFFBBF24)),
        Triple(Icons.Default.HelpOutline, "سؤال", Color(0xFFF472B6)),
        Triple(Icons.Default.Calculate, "أرقام", Color(0xFFA78BFA)),
        Triple(Icons.Default.MenuBook, "كتب", Color(0xFF34D399)),
        Triple(Icons.Default.Public, "كواكب", Color(0xFF38BDF8)),
        Triple(Icons.Default.Extension, "ألغاز", Color(0xFFFB923C)),
        Triple(Icons.Default.AutoAwesome, "نجمة", Color(0xFFFDE047))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, (icon, label, tint) ->
            val itemOffset = if (index % 2 == 0) floatOffset * 0.6f else -floatOffset * 0.6f
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = itemOffset.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, tint.copy(alpha = 0.7f), CircleShape)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun WelcomeActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .border(
                width = 1.5.dp,
                color = Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(18.dp)
            )
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.16f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.25f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color(0xFFE2E8F0),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun MiniDomainTag(
    label: String,
    bgColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
