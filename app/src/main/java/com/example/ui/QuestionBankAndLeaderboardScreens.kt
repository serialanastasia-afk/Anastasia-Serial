package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.QuestionEntity
import com.example.data.QuizCategory
import com.example.data.ScoreRecordEntity
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.CorrectEmerald
import com.example.ui.theme.RoyalNavyCard
import com.example.ui.theme.RoyalNavyDark

@Composable
fun QuestionBankScreen(
    allQuestions: List<QuestionEntity>,
    selectedCategoryFilter: QuizCategory?,
    selectedGradeFilter: String?,
    onCategoryFilterChange: (QuizCategory?) -> Unit,
    onGradeFilterChange: (String?) -> Unit,
    onAddQuestion: (
        category: QuizCategory,
        gradeLevel: String,
        difficulty: Int,
        questionText: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctOptionIndex: Int,
        explanation: String
    ) -> Unit,
    onDeleteQuestion: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredQuestions = remember(allQuestions, selectedCategoryFilter, selectedGradeFilter) {
        allQuestions.filter { q ->
            val matchesCategory = selectedCategoryFilter == null || q.categoryId == selectedCategoryFilter.id
            val matchesGrade = selectedGradeFilter == null || q.gradeLevel == selectedGradeFilter
            matchesCategory && matchesGrade
        }
    }

    val grades = listOf("الصف الرابع", "الصف الخامس", "الصف السادس")
    val specificCategories = QuizCategory.entries.filter { it != QuizCategory.MIXED }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalNavyCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LibraryBooks,
                                    contentDescription = null,
                                    tint = ChampionshipGold
                                )
                                Text(
                                    text = "بنك أسئلة مدرسة عيون مصر",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "يمكن للمعلمين والطلاب استعراض الأسئلة وإضافة أسئلة جديدة للمسابقة (${filteredQuestions.size} سؤالاً معروضاً).",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            // Category Filter Row
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryFilter == null,
                            onClick = { onCategoryFilterChange(null) },
                            label = { Text("كل المجالات") },
                            modifier = Modifier.testTag("bank_filter_all_categories")
                        )
                    }
                    items(specificCategories) { cat ->
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = {
                                onCategoryFilterChange(if (selectedCategoryFilter == cat) null else cat)
                            },
                            label = { Text(cat.titleAr) },
                            modifier = Modifier.testTag("bank_filter_${cat.id}")
                        )
                    }
                }
            }

            // Grade Filter Row
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedGradeFilter == null,
                            onClick = { onGradeFilterChange(null) },
                            label = { Text("كل الصفوف") }
                        )
                    }
                    items(grades) { grade ->
                        FilterChip(
                            selected = selectedGradeFilter == grade,
                            onClick = {
                                onGradeFilterChange(if (selectedGradeFilter == grade) null else grade)
                            },
                            label = { Text(grade) }
                        )
                    }
                }
            }

            items(filteredQuestions, key = { it.id }) { question ->
                QuestionBankItemCard(
                    question = question,
                    onDelete = { onDeleteQuestion(question.id) }
                )
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = ChampionshipGold,
            contentColor = RoyalNavyDark,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("open_add_question_dialog_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة سؤال جديد")
                Text(
                    text = "إضافة سؤال للمسابقة",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    if (showAddDialog) {
        AddCustomQuestionDialog(
            onDismiss = { showAddDialog = false },
            onSave = { cat, grade, diff, qText, a, b, c, d, correctIdx, exp ->
                onAddQuestion(cat, grade, diff, qText, a, b, c, d, correctIdx, exp)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun QuestionBankItemCard(
    question: QuestionEntity,
    onDelete: () -> Unit
) {
    val category = QuizCategory.fromId(question.categoryId)
    val accentColor = Color(category.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bank_question_card_${question.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        color = accentColor.copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = category.titleAr,
                            style = MaterialTheme.typography.labelMedium,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = question.gradeLevel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    if (question.isCustom) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = ChampionshipGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "سؤال مضاف",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF92400E),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (question.isCustom) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "حذف السؤال",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "الإجابة الصحيحة",
                    tint = CorrectEmerald,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "الإجابة الصحيحة: ${question.options[question.correctOptionIndex]}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = CorrectEmerald
                )
            }

            Text(
                text = question.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AddCustomQuestionDialog(
    onDismiss: () -> Unit,
    onSave: (
        category: QuizCategory,
        gradeLevel: String,
        difficulty: Int,
        questionText: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctOptionIndex: Int,
        explanation: String
    ) -> Unit
) {
    val categories = QuizCategory.entries.filter { it != QuizCategory.MIXED }
    val grades = listOf("الصف الرابع", "الصف الخامس", "الصف السادس")

    var selectedCategory by remember { mutableStateOf(QuizCategory.SCIENCE) }
    var selectedGrade by remember { mutableStateOf("الصف الخامس") }
    var difficulty by remember { mutableIntStateOf(2) }
    var questionText by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctIndex by remember { mutableIntStateOf(0) }
    var explanation by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "إضافة سؤال جديد لمسابقة عيون مصر",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "المجال العلمي:",
                    style = MaterialTheme.typography.labelLarge
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.titleAr) }
                        )
                    }
                }

                Text(
                    text = "الصف الدراسي:",
                    style = MaterialTheme.typography.labelLarge
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    grades.forEach { g ->
                        FilterChip(
                            selected = selectedGrade == g,
                            onClick = { selectedGrade = g },
                            label = { Text(g) }
                        )
                    }
                }

                OutlinedTextField(
                    value = questionText,
                    onValueChange = {
                        questionText = it
                        errorMessage = null
                    },
                    label = { Text("نص السؤال") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_question_text_input")
                )

                val optionInputs = listOf(
                    Triple(0, "الاختيار الأول (أ)", optionA to { v: String -> optionA = v }),
                    Triple(1, "الاختيار الثاني (ب)", optionB to { v: String -> optionB = v }),
                    Triple(2, "الاختيار الثالث (ج)", optionC to { v: String -> optionC = v }),
                    Triple(3, "الاختيار الرابع (د)", optionD to { v: String -> optionD = v })
                )

                Text(
                    text = "اكتب الاختيارات وحدد الإجابة الصحيحة:",
                    style = MaterialTheme.typography.labelLarge
                )

                optionInputs.forEach { (idx, label, pair) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = correctIndex == idx,
                            onClick = { correctIndex = idx }
                        )
                        OutlinedTextField(
                            value = pair.first,
                            onValueChange = {
                                pair.second(it)
                                errorMessage = null
                            },
                            label = { Text(label) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("new_option_input_$idx")
                        )
                    }
                }

                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("معلومة إثرائية أو تفسير الإجابة (اختياري)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_explanation_input")
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (questionText.isBlank() || optionA.isBlank() || optionB.isBlank() || optionC.isBlank() || optionD.isBlank()) {
                        errorMessage = "يرجى كتابة نص السؤال وجميع الاختيارات الأربعة."
                    } else {
                        onSave(
                            selectedCategory,
                            selectedGrade,
                            difficulty,
                            questionText,
                            optionA,
                            optionB,
                            optionC,
                            optionD,
                            correctIndex,
                            explanation
                        )
                    }
                },
                modifier = Modifier.testTag("save_new_question_button")
            ) {
                Text("حفظ السؤال")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
fun LeaderboardScreen(
    scores: List<ScoreRecordEntity>,
    onClearScores: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalNavyCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "لوحة الشرف",
                                    tint = ChampionshipGold,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "لوحة شرف عباقرة مدرسة عيون مصر",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "سجل أبطال المسابقات الفردية ومواجهات الفصول الدراسية",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }

                        if (scores.isNotEmpty()) {
                            IconButton(
                                onClick = onClearScores,
                                modifier = Modifier.testTag("clear_leaderboard_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "مسح السجل",
                                    tint = Color(0xFFCBD5E1)
                                )
                            }
                        }
                    }
                }
            }

            if (scores.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = ChampionshipGold,
                                modifier = Modifier.size(56.dp)
                            )
                            Text(
                                text = "لا توجد نتائج مسجلة بعد",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "ابدأ جولة فردية أو تحدي الفصول ليظهر اسمك في لوحة شرف عباقرة مدرسة عيون مصر!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(scores, key = { _, item -> item.id }) { index, record ->
                    LeaderboardRankCard(rank = index + 1, record = record)
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRankCard(
    rank: Int,
    record: ScoreRecordEntity
) {
    val rankColor = when (rank) {
        1 -> ChampionshipGold
        2 -> Color(0xFF94A3B8)
        3 -> Color(0xFFD97706)
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_$rank"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = rankColor,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "#$rank",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (rank <= 3) RoyalNavyDark else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (record.isTeamMatch) Icons.Default.Groups else Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = record.playerOrWinnerName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${record.categoryTitle} • ${record.gradeOrClassroom}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (record.isTeamMatch) {
                    Text(
                        text = "${record.team1Name} (${record.team1Score}) ضد ${record.team2Name} (${record.team2Score})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Text(
                        text = "${record.badgeTitle} (${record.correctAnswers}/${record.totalQuestions} إجابات صحيحة)",
                        style = MaterialTheme.typography.labelMedium,
                        color = CorrectEmerald
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ChampionshipGold.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${record.score} نقطة",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFB45309),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
