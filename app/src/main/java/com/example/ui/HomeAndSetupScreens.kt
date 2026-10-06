package com.example.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.QuestionEntity
import com.example.data.QuizCategory
import com.example.data.StudentRegistrationEntity
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.CorrectEmerald
import com.example.ui.theme.RoyalNavyCard
import com.example.ui.theme.RoyalNavyDark

fun categoryIcon(category: QuizCategory): ImageVector = when (category) {
    QuizCategory.SCIENCE -> Icons.Default.Science
    QuizCategory.EGYPT_HISTORY -> Icons.Default.AccountBalance
    QuizCategory.ARABIC -> Icons.Default.MenuBook
    QuizCategory.MATH_LOGIC -> Icons.Default.Calculate
    QuizCategory.LOGIC_DEDUCTION -> Icons.Default.Extension
    QuizCategory.OBSERVATION_FOCUS -> Icons.Default.Visibility
    QuizCategory.GENERAL -> Icons.Default.Public
    QuizCategory.TECH_INNOVATION -> Icons.Default.Computer
    QuizCategory.MIXED -> Icons.Default.AutoAwesome
}

@Composable
fun HomeAndSetupScreens(
    studentName: String,
    selectedGrade: String,
    questionCount: Int,
    activeRegistration: StudentRegistrationEntity?,
    allQuestions: List<QuestionEntity>,
    onStudentNameChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onQuestionCountChange: (Int) -> Unit,
    onStartOfficialQualifierExam: () -> Unit,
    onOpenRegistration: () -> Unit,
    onStartSoloQuiz: (QuizCategory) -> Unit,
    onNavigateToTeams: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradeOptions = listOf(
        "الصف الرابع الابتدائي",
        "الصف الخامس الابتدائي",
        "الصف السادس الابتدائي",
        "كل الصفوف (٤ - ٦ ابتدائي)"
    )
    val countOptions = listOf(5, 8, 10, 15)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Hero Banner Card
            item {
                HeroStageBanner(
                    totalQuestionsCount = allQuestions.size,
                    onStartOfficialQualifier = onStartOfficialQualifierExam,
                    onNavigateToTeams = onNavigateToTeams
                )
            }

            // 2. Stage 1 Official Online Qualifier Callout Card (50 Questions / 30 Minutes)
            item {
                OfficialQualifierLauncherCard(
                    activeRegistration = activeRegistration,
                    onStartQualifierExam = onStartOfficialQualifierExam,
                    onOpenRegistration = onOpenRegistration
                )
            }

            // 3. Student Profile & Grade Selector Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.School,
                                            contentDescription = "بيانات الطالب",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "بطاقة المتسابق العبقري",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (activeRegistration != null) {
                                            "كود المشاركة الفعال: 🎟️ ${activeRegistration.participationCode}"
                                        } else {
                                            "اختر صفك الدراسي أو استخرج كود مشاركة رسمي"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ChampionshipGold.copy(alpha = 0.25f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(onClick = onOpenRegistration)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ConfirmationNumber,
                                        contentDescription = null,
                                        tint = Color(0xFF92400E),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "بطاقة الكود",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = studentName,
                            onValueChange = onStudentNameChange,
                            label = { Text("اسم الطالب / الطالبة") },
                            placeholder = { Text("مثال: عمر محمد - الصف الخامس") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("student_name_input")
                        )

                        Text(
                            text = "الصف الدراسي (الرابع / الخامس / السادس الابتدائي):",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(gradeOptions) { grade ->
                                val isSelected = selectedGrade == grade
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onGradeChange(grade) },
                                    label = {
                                        Text(
                                            text = grade,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier.testTag("grade_chip_$grade")
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "عدد أسئلة التدريب الفردي:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                countOptions.forEach { count ->
                                    FilterChip(
                                        selected = questionCount == count,
                                        onClick = { onQuestionCountChange(count) },
                                        label = { Text("$count أسئلة") },
                                        modifier = Modifier.testTag("count_chip_$count")
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Categories Section Header (All 8 Official Domains + Mixed)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "مجالات عباقرة عيون مصر",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "اضغط على أي مجال للتدريب السريع أو خوض جولة فردية",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "٨ مجالات + شامل",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 5. Category Cards
            items(QuizCategory.entries) { category ->
                val countForCategory = if (category == QuizCategory.MIXED) {
                    allQuestions.size
                } else {
                    allQuestions.count { it.categoryId == category.id }
                }
                CategoryChallengeCard(
                    category = category,
                    questionCount = countForCategory,
                    onClick = { onStartSoloQuiz(category) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun OfficialQualifierLauncherCard(
    activeRegistration: StudentRegistrationEntity?,
    onStartQualifierExam: () -> Unit,
    onOpenRegistration: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalNavyCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
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
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = ChampionshipGold
                    )
                    Text(
                        text = "المرحلة الأولى: التصفيات الأونلاين 🌐",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = CorrectEmerald.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "٥٠ سؤالاً • ٣٠ دقيقة",
                        style = MaterialTheme.typography.labelMedium,
                        color = CorrectEmerald,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "التوزيع الرسمي: العلوم (١٠) • الرياضيات (٨) • المنطق (٨) • اللغة العربية (٦) • مصر والعالم (٦) • الملاحظة والتركيز (٦) • الثقافة العامة (٤) • التكنولوجيا (٢).",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFCBD5E1)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onStartQualifierExam,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CorrectEmerald,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("start_official_qualifier_button")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "بدء اختبار الـ ٥٠ سؤالاً",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    onClick = onOpenRegistration,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.14f),
                        contentColor = ChampionshipGold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("open_registration_card_button")
                ) {
                    Text(
                        text = if (activeRegistration != null) {
                            "🎟️ ${activeRegistration.participationCode}"
                        } else {
                            "🎟️ استخراج كود"
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroStageBanner(
    totalQuestionsCount: Int,
    onStartOfficialQualifier: () -> Unit,
    onNavigateToTeams: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(RoyalNavyDark, RoyalNavyCard, Color(0xFF1D4ED8))
                    )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_oyoun_masr_1791269220451),
                contentDescription = "خلفية مسابقة عباقرة مدرسة عيون مصر",
                contentScale = ContentScale.Crop,
                alpha = 0.28f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = ChampionshipGold
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "كأس العباقرة",
                                tint = RoyalNavyDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "البطولة الرسمية • $totalQuestionsCount سؤالاً",
                                style = MaterialTheme.typography.labelMedium,
                                color = RoyalNavyDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "🏆 عباقرة عيون مصر",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )

                Text(
                    text = "«فكّر… أسرع. اعرف… أكثر. العب… كفريق!» — مسابقة الذكاء والمعرفة لطلاب الصفوف الرابع والخامس والسادس الابتدائي.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE2E8F0)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onStartOfficialQualifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChampionshipGold,
                            contentColor = RoyalNavyDark
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("quick_start_mixed_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "ابدأ",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اختبار التصفيات (٥٠ سؤالاً)",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Button(
                        onClick = onNavigateToTeams,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.16f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("hero_team_mode_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "تحدي الفرق",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "نهائيات الفرق",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChallengeCard(
    category: QuizCategory,
    questionCount: Int,
    onClick: () -> Unit
) {
    val accentColor = Color(category.colorHex)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("category_card_${category.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(
                        width = 1.5.dp,
                        color = accentColor.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(category),
                    contentDescription = category.titleAr,
                    tint = accentColor,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${category.emoji} ${category.titleAr}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = accentColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "$questionCount سؤال",
                            style = MaterialTheme.typography.labelMedium,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "${category.subtitleAr} • (حصة التصفيات: ${category.qualifierQuestionQuota} أسئلة)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = CircleShape,
                color = accentColor,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "ابدأ المسابقة",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TeamBattleSetupScreen(
    team1Name: String,
    team2Name: String,
    selectedCategory: QuizCategory,
    onTeam1NameChange: (String) -> Unit,
    onTeam2NameChange: (String) -> Unit,
    onCategorySelect: (QuizCategory) -> Unit,
    onStartTeamBattle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 700.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalNavyCard)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "مواجهة الفصول",
                                tint = ChampionshipGold,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "المرحلة الثانية: البطولة النهائية بالمدرسة 🏫",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "منافسة حماسية مباشرة بين الفرق المتأهلة داخل مدرسة عيون مصر! يتناوب الفريقان على الإجابة عن الأسئلة مع احتساب نقاط السرعة ومساعدات العباقرة.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "تسمية الفريقين المتنافسين",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = team1Name,
                            onValueChange = onTeam1NameChange,
                            label = { Text("اسم الفريق الأول (أو الفصل الأول)") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("team1_name_input")
                        )

                        OutlinedTextField(
                            value = team2Name,
                            onValueChange = onTeam2NameChange,
                            label = { Text("اسم الفريق الثاني (أو الفصل الثاني)") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("team2_name_input")
                        )

                        Text(
                            text = "اختر مجال مواجهة الفريقين:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(QuizCategory.entries) { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { onCategorySelect(cat) },
                                    label = { Text("${cat.emoji} ${cat.titleAr}") },
                                    modifier = Modifier.testTag("team_cat_chip_${cat.id}")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = { onStartTeamBattle(4) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("start_team_battle_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "انطلاق مواجهة الفريقين (٨ أسئلة بالتناوب)",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
