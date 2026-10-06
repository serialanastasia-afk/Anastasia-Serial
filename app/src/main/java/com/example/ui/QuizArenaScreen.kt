package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Filter2
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.CorrectEmerald
import com.example.ui.theme.CorrectEmeraldContainer
import com.example.ui.theme.RoyalNavyCard
import com.example.ui.theme.RoyalNavyDark
import com.example.ui.theme.WrongCoral
import com.example.ui.theme.WrongCoralContainer

@Composable
fun ActiveQuizArenaScreen(
    state: ActiveSessionState.Playing,
    onSelectOption: (Int) -> Unit,
    onUseFiftyFifty: () -> Unit,
    onUseExtraTime: () -> Unit,
    onUseSkipQuestion: () -> Unit,
    onNextQuestion: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onExitQuiz()
    }

    val question = state.currentQuestion
    val optionLetters = listOf("أ", "ب", "ج", "د")

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Top Arena Bar (Progress, Score/Teams, Exit)
            item {
                ArenaTopHeader(
                    state = state,
                    onExitQuiz = onExitQuiz
                )
            }

            // 2. Timer & Question Metadata Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RoyalNavyCard
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = ChampionshipGold.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = question.gradeLevel,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ChampionshipGold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = Color.White.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = question.difficultyLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Countdown Pill
                            val timerColor = when {
                                state.timeLeftSeconds <= 7 -> WrongCoral
                                state.timeLeftSeconds <= 14 -> ChampionshipGold
                                else -> CorrectEmerald
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = timerColor.copy(alpha = 0.2f),
                                modifier = Modifier.border(
                                    width = 1.dp,
                                    color = timerColor,
                                    shape = RoundedCornerShape(50)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = "الوقت المتبقي",
                                        tint = timerColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "${state.timeLeftSeconds} ثانية",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        LinearProgressIndicator(
                            progress = {
                                (state.timeLeftSeconds.toFloat() / state.totalTimeSeconds.toFloat())
                                    .coerceIn(0f, 1f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(50)),
                            color = if (state.timeLeftSeconds <= 7) WrongCoral else ChampionshipGold,
                            trackColor = Color.White.copy(alpha = 0.15f)
                        )

                        Text(
                            text = question.questionText,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .testTag("active_question_text")
                        )
                    }
                }
            }

            // 3. Lifelines Bar (وسائل مساعدة العباقرة)
            item {
                LifelinesRow(
                    usedFiftyFifty = state.usedFiftyFifty,
                    usedExtraTime = state.usedExtraTime,
                    usedSkip = state.usedSkipQuestion,
                    isAnswerRevealed = state.isAnswerRevealed,
                    onUseFiftyFifty = onUseFiftyFifty,
                    onUseExtraTime = onUseExtraTime,
                    onUseSkip = onUseSkipQuestion
                )
            }

            // 4. Four Option Cards
            items(question.options.size) { index ->
                val optionText = question.options[index]
                val isEliminated = state.eliminatedOptionIndices.contains(index)
                val isSelected = state.selectedOptionIndex == index
                val isCorrectOption = index == question.correctOptionIndex

                OptionChoiceCard(
                    letter = optionLetters[index],
                    text = optionText,
                    index = index,
                    isEliminated = isEliminated,
                    isSelected = isSelected,
                    isCorrectOption = isCorrectOption,
                    isAnswerRevealed = state.isAnswerRevealed,
                    onClick = {
                        if (!isEliminated && !state.isAnswerRevealed) {
                            onSelectOption(index)
                        }
                    }
                )
            }

            // 5. Explanation & Next Button Card (when answer is revealed)
            item {
                AnimatedVisibility(
                    visible = state.isAnswerRevealed,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    val isCorrect = state.selectedOptionIndex == question.correctOptionIndex
                    val isTimeOut = state.selectedOptionIndex == -1
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("explanation_card"),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect) {
                                CorrectEmeraldContainer
                            } else {
                                WrongCoralContainer
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (isCorrect) Color(0xFF047857) else Color(0xFFB91C1C),
                                    modifier = Modifier.size(26.dp)
                                )
                                Text(
                                    text = when {
                                        isCorrect -> "إجابة عبقرية صحيحة! (+${state.pointsEarnedThisTurn} نقطة)"
                                        isTimeOut -> "انتهى الوقت المخصص للسؤال!"
                                        else -> "إجابة غير صحيحة، حاول في السؤال القادم!"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isCorrect) Color(0xFF065F46) else Color(0xFF991B1B)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "معلومة إثرائية",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = question.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Button(
                                onClick = onNextQuestion,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RoyalNavyDark,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("next_question_button")
                            ) {
                                Text(
                                    text = if (state.currentIndex + 1 < state.questions.size) {
                                        "السؤال التالي (${state.currentIndex + 2} من ${state.questions.size})"
                                    } else {
                                        "عرض النتيجة النهائية ووسام العباقرة"
                                    },
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArenaTopHeader(
    state: ActiveSessionState.Playing,
    onExitQuiz: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                    onClick = onExitQuiz,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("exit_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إنهاء المسابقة",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column {
                    Text(
                        text = state.category.titleAr,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "السؤال ${state.currentIndex + 1} من ${state.questions.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!state.isTeamMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.streak >= 2) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFFFEDD5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "سلسلة إجابات صحيحة",
                                    tint = Color(0xFFEA580C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${state.streak} متتالية",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF9A3412)
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = ChampionshipGold
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "النقاط",
                                tint = RoyalNavyDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "${state.score} نقطة",
                                style = MaterialTheme.typography.labelLarge,
                                color = RoyalNavyDark
                            )
                        }
                    }
                }
            }
        }

        // Team Turn Banner if in Team Competition Mode
        if (state.isTeamMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TeamScorePill(
                    teamName = state.team1Name,
                    score = state.team1Score,
                    isCurrentTurn = state.currentTeamTurn == 1,
                    modifier = Modifier.weight(1f)
                )
                TeamScorePill(
                    teamName = state.team2Name,
                    score = state.team2Score,
                    isCurrentTurn = state.currentTeamTurn == 2,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TeamScorePill(
    teamName: String,
    score: Int,
    isCurrentTurn: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isCurrentTurn) ChampionshipGold else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (isCurrentTurn) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isCurrentTurn) "دور الإجابة الآن: $teamName" else teamName,
                style = MaterialTheme.typography.labelMedium,
                color = if (isCurrentTurn) RoyalNavyDark else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Text(
                text = "$score نقطة",
                style = MaterialTheme.typography.titleMedium,
                color = if (isCurrentTurn) RoyalNavyDark else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LifelinesRow(
    usedFiftyFifty: Boolean,
    usedExtraTime: Boolean,
    usedSkip: Boolean,
    isAnswerRevealed: Boolean,
    onUseFiftyFifty: () -> Unit,
    onUseExtraTime: () -> Unit,
    onUseSkip: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LifelineButton(
            label = "حذف إجابتين",
            icon = Icons.Default.Filter2,
            isUsed = usedFiftyFifty,
            enabled = !usedFiftyFifty && !isAnswerRevealed,
            onClick = onUseFiftyFifty,
            testTag = "lifeline_50_50",
            modifier = Modifier.weight(1f)
        )
        LifelineButton(
            label = "+١٥ ثانية",
            icon = Icons.Default.MoreTime,
            isUsed = usedExtraTime,
            enabled = !usedExtraTime && !isAnswerRevealed,
            onClick = onUseExtraTime,
            testTag = "lifeline_extra_time",
            modifier = Modifier.weight(1f)
        )
        LifelineButton(
            label = "حكمة العبقري",
            icon = Icons.Default.AutoFixHigh,
            isUsed = usedSkip,
            enabled = !usedSkip && !isAnswerRevealed,
            onClick = onUseSkip,
            testTag = "lifeline_skip",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LifelineButton(
    label: String,
    icon: ImageVector,
    isUsed: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = if (isUsed) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isUsed) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                },
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isUsed) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                }
            )
        }
    }
}

@Composable
private fun OptionChoiceCard(
    letter: String,
    text: String,
    index: Int,
    isEliminated: Boolean,
    isSelected: Boolean,
    isCorrectOption: Boolean,
    isAnswerRevealed: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isEliminated -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        isAnswerRevealed && isCorrectOption -> CorrectEmeraldContainer
        isAnswerRevealed && isSelected && !isCorrectOption -> WrongCoralContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        isEliminated -> Color.Transparent
        isAnswerRevealed && isCorrectOption -> CorrectEmerald
        isAnswerRevealed && isSelected && !isCorrectOption -> WrongCoral
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val badgeColor = when {
        isEliminated -> MaterialTheme.colorScheme.surfaceVariant
        isAnswerRevealed && isCorrectOption -> CorrectEmerald
        isAnswerRevealed && isSelected && !isCorrectOption -> WrongCoral
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val badgeTextColor = when {
        isAnswerRevealed && (isCorrectOption || isSelected) -> Color.White
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(18.dp))
            .clickable(enabled = !isEliminated && !isAnswerRevealed, onClick = onClick)
            .testTag("option_card_$index"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isEliminated) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = badgeColor,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = letter,
                        style = MaterialTheme.typography.titleMedium,
                        color = badgeTextColor
                    )
                }
            }

            Text(
                text = if (isEliminated) "تم استبعاد هذا الاختيار" else text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isEliminated) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                } else if (isAnswerRevealed && (isCorrectOption || isSelected)) {
                    Color(0xFF0F172A)
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.weight(1f)
            )

            if (isAnswerRevealed && isCorrectOption) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "إجابة صحيحة",
                    tint = CorrectEmerald,
                    modifier = Modifier.size(26.dp)
                )
            } else if (isAnswerRevealed && isSelected && !isCorrectOption) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "إجابة خاطئة",
                    tint = WrongCoral,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun CelebrationResultScreen(
    state: ActiveSessionState.Completed,
    onPlayAgainSameCategory: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackToHome()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 640.dp),
            contentPadding = PaddingValues(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("result_summary_card"),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(RoyalNavyDark, RoyalNavyCard)
                                )
                            )
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ChampionshipGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(88.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "كأس البطولة",
                                    tint = ChampionshipGold,
                                    modifier = Modifier.size(52.dp)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ChampionshipGold
                        ) {
                            Text(
                                text = state.badgeTitle,
                                style = MaterialTheme.typography.labelLarge,
                                color = RoyalNavyDark,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = if (state.isTeamMode) {
                                "الفائز: ${state.winnerOrPlayerName}"
                            } else {
                                "أحسنت يا ${state.winnerOrPlayerName}!"
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "${state.category.titleAr} • ${state.gradeOrClassroom}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFCBD5E1)
                        )

                        if (state.isTeamMode) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = state.team1Name,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${state.team1Score} نقطة",
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = ChampionshipGold
                                        )
                                    }
                                }
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = state.team2Name,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${state.team2Score} نقطة",
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = ChampionshipGold
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "${state.finalScore} نقطة",
                                style = MaterialTheme.typography.displayMedium,
                                color = ChampionshipGold
                            )
                        }

                        Text(
                            text = "الإجابات الصحيحة: ${state.correctCount} من ${state.totalQuestions} أسئلة",
                            style = MaterialTheme.typography.titleMedium,
                            color = CorrectEmerald
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = onPlayAgainSameCategory,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ChampionshipGold,
                                contentColor = RoyalNavyDark
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("play_again_button")
                        ) {
                            Icon(imageVector = Icons.Default.Replay, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "جولة جديدة في نفس المجال",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        OutlinedButton(
                            onClick = onBackToHome,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("back_to_home_button")
                        ) {
                            Text(
                                text = "العودة إلى منصة مدرسة عيون مصر",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
