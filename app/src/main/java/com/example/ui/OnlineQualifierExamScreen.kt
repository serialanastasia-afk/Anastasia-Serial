package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.CorrectEmerald
import com.example.ui.theme.RoyalNavyCard
import com.example.ui.theme.RoyalNavyDark
import com.example.ui.theme.WrongCoral

@Composable
fun OnlineQualifierExamScreen(
    state: ActiveSessionState.OnlineQualifierExam,
    onSelectAnswer: (Int) -> Unit,
    onToggleFlag: () -> Unit,
    onJumpToQuestion: (Int) -> Unit,
    onFinishExam: () -> Unit,
    onExitExam: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmSubmitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showConfirmSubmitDialog = true
    }

    val question = state.currentQuestion
    val category = question.category
    val selectedOption = state.selectedAnswers[state.currentIndex]
    val isFlagged = state.flaggedQuestions.contains(state.currentIndex)
    val optionLetters = listOf("أ", "ب", "ج", "د")

    val minutes = state.remainingSeconds / 60
    val seconds = state.remainingSeconds % 60
    val formattedTimer = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Top Sticky Status & 30-Minute Countdown Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalNavyDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { showConfirmSubmitDialog = true },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.12f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "إنهاء أو خروج",
                                        tint = Color.White
                                    )
                                }
                                Column {
                                    Text(
                                        text = "اختبار التصفيات الأونلاين 🌐",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = ChampionshipGold
                                    )
                                    Text(
                                        text = "${state.studentName} • 🎟️ ${state.participationCode}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                            }

                            // 30-Minute Prominent Countdown Clock
                            val timerUrgent = state.remainingSeconds < 300
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (timerUrgent) WrongCoral else ChampionshipGold
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = "العداد التنازلي",
                                        tint = if (timerUrgent) Color.White else RoyalNavyDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = formattedTimer,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (timerUrgent) Color.White else RoyalNavyDark,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.testTag("qualifier_countdown_timer")
                                    )
                                }
                            }
                        }

                        // Progress bar for answered questions out of 50
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "تمت الإجابة عن ${state.answeredCount} من ${state.questions.size} سؤالاً",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                            Text(
                                text = "السؤال الحالي: ${state.currentIndex + 1} / ${state.questions.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = ChampionshipGold
                            )
                        }

                        LinearProgressIndicator(
                            progress = {
                                (state.answeredCount.toFloat() / state.questions.size.toFloat())
                                    .coerceIn(0f, 1f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(50)),
                            color = CorrectEmerald,
                            trackColor = Color.White.copy(alpha = 0.16f)
                        )
                    }
                }
            }

            // 2. Question Navigator Strip (1 .. 50) for seamless jumping between questions
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(state.questions) { idx, _ ->
                        val isCurrent = idx == state.currentIndex
                        val isAnswered = state.selectedAnswers.containsKey(idx)
                        val isItemFlagged = state.flaggedQuestions.contains(idx)

                        val bgColor = when {
                            isCurrent -> MaterialTheme.colorScheme.primary
                            isItemFlagged -> ChampionshipGold
                            isAnswered -> CorrectEmerald
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                        val textColor = when {
                            isCurrent || isAnswered -> Color.White
                            isItemFlagged -> RoyalNavyDark
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }

                        Surface(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .clickable { onJumpToQuestion(idx) }
                                .testTag("qualifier_q_nav_${idx + 1}"),
                            shape = CircleShape,
                            color = bgColor
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = textColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 3. Question Card (with Domain Badge & Optional Visual Observation Box)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalNavyCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = "${category.emoji} ${category.titleAr}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ChampionshipGold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            IconButton(
                                onClick = onToggleFlag,
                                modifier = Modifier.testTag("flag_question_button")
                            ) {
                                Icon(
                                    imageVector = if (isFlagged) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "تعليم السؤال للمراجعة",
                                    tint = if (isFlagged) ChampionshipGold else Color.White
                                )
                            }
                        }

                        Text(
                            text = "س${state.currentIndex + 1}: ${question.questionText}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        // Visual Pattern / Observation Box if present
                        if (question.visualClueText.isNotBlank()) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.95f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = RoyalNavyDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "لوحة التركيز والملاحظة البصرية",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = RoyalNavyDark
                                        )
                                    }
                                    Text(
                                        text = question.visualClueText,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = RoyalNavyDark,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Four Options
            itemsIndexed(question.options) { optIdx, optText ->
                val isSelected = selectedOption == optIdx
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            },
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable { onSelectAnswer(optIdx) }
                        .testTag("qualifier_option_$optIdx"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = optionLetters[optIdx],
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }

                        Text(
                            text = optText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "تم الاختيار",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // 5. Previous / Next / Submit Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (state.currentIndex > 0) {
                                onJumpToQuestion(state.currentIndex - 1)
                            }
                        },
                        enabled = state.currentIndex > 0,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("السؤال السابق")
                    }

                    if (state.currentIndex + 1 < state.questions.size) {
                        Button(
                            onClick = { onJumpToQuestion(state.currentIndex + 1) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("qualifier_next_button")
                        ) {
                            Text("السؤال التالي")
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    } else {
                        Button(
                            onClick = { showConfirmSubmitDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CorrectEmerald),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("qualifier_finish_button")
                        ) {
                            Icon(imageVector = Icons.Default.DoneAll, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تسليم الاختبار")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { showConfirmSubmitDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChampionshipGold,
                        contentColor = RoyalNavyDark
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_qualifier_anytime_button")
                ) {
                    Icon(imageVector = Icons.Default.DoneAll, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إنهاء وتسليم إجابات التصفيات (${state.answeredCount} / ${state.questions.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showConfirmSubmitDialog) {
        val unansweredCount = state.questions.size - state.answeredCount
        AlertDialog(
            onDismissRequest = { showConfirmSubmitDialog = false },
            title = {
                Text("تأكيد تسليم اختبار التصفيات الأونلاين")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (unansweredCount == 0) {
                            "لقد أجبت عن جميع الأسئلة الـ ٥٠! هل أنت مستعد لعرض النتيجة النهائية ودرجاتك في كل مجال؟"
                        } else {
                            "لقد أجبت عن ${state.answeredCount} سؤالاً، ويتبقى لديك $unansweredCount سؤالاً بدون إجابة. هل ترغب في تسليم الاختبار الآن؟"
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmSubmitDialog = false
                        onFinishExam()
                    }
                ) {
                    Text("تسليم وعرض النتيجة")
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = {
                            showConfirmSubmitDialog = false
                            onExitExam()
                        }
                    ) {
                        Text("خروج بدون حفظ", color = MaterialTheme.colorScheme.error)
                    }
                    TextButton(onClick = { showConfirmSubmitDialog = false }) {
                        Text("مواصلة الحل")
                    }
                }
            }
        )
    }
}
