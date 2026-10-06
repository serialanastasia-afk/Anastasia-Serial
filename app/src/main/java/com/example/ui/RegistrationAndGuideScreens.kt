package com.example.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.QuizCategory
import com.example.data.StudentRegistrationEntity
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.CorrectEmerald
import com.example.ui.theme.RoyalNavyCard
import com.example.ui.theme.RoyalNavyDark

@Composable
fun StudentRegistrationScreen(
    activeRegistration: StudentRegistrationEntity?,
    savedRegistrations: List<StudentRegistrationEntity>,
    onRegisterStudent: (
        fullName: String,
        gradeLevel: String,
        classroom: String,
        studentNumberOrCode: String,
        teamName: String,
        parentPhone: String,
        onGenerated: (StudentRegistrationEntity) -> Unit
    ) -> Unit,
    onActivateExistingRegistration: (StudentRegistrationEntity) -> Unit,
    onLookupCode: (String, (StudentRegistrationEntity?) -> Unit) -> Unit,
    onStartOfficialQualifierExam: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradeOptions = listOf(
        "الصف الرابع الابتدائي",
        "الصف الخامس الابتدائي",
        "الصف السادس الابتدائي"
    )

    var fullName by remember { mutableStateOf(activeRegistration?.studentName ?: "") }
    var selectedGrade by remember { mutableStateOf(activeRegistration?.gradeLevel ?: gradeOptions[1]) }
    var classroom by remember { mutableStateOf(activeRegistration?.classroom ?: "٥/أ") }
    var studentNumber by remember { mutableStateOf(activeRegistration?.studentNumberOrId ?: "") }
    var teamName by remember { mutableStateOf(activeRegistration?.teamName ?: "") }
    var parentContact by remember { mutableStateOf(activeRegistration?.parentContact ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var lookupCodeInput by remember { mutableStateOf("") }
    var lookupFeedback by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Active Participation Ticket Banner (if registered)
            if (activeRegistration != null) {
                item {
                    ParticipationCodeTicketCard(
                        registration = activeRegistration,
                        onStartQualifier = onStartOfficialQualifierExam
                    )
                }
            }

            // Registration Form Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "تسجيل طالب جديد في البطولة",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "سجّل بياناتك للحصول على 🎟️ كود المشاركة الرسمي للدخول إلى التصفيات",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // 1. اسم الطالب
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = {
                                fullName = it
                                errorMessage = null
                            },
                            label = { Text("اسم الطالب ثلاثياً *") },
                            placeholder = { Text("مثال: عمر أحمد محمود") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_student_name_input")
                        )

                        // 2. الصف الدراسي (الرابع / الخامس / السادس الابتدائي)
                        Text(
                            text = "الصف الدراسي (المرحلة الابتدائية العليا) *:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            gradeOptions.forEach { grade ->
                                val isSelected = selectedGrade == grade
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { selectedGrade = grade }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.outlineVariant
                                            },
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .testTag("reg_grade_option_$grade"),
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.School,
                                                contentDescription = null,
                                                tint = if (isSelected) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                },
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = grade,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "محدد",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 3. الفصل & 4. رقم الطالب أو كود المدرسة
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = classroom,
                                onValueChange = {
                                    classroom = it
                                    errorMessage = null
                                },
                                label = { Text("الفصل *") },
                                placeholder = { Text("مثال: ٥/أ") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reg_classroom_input")
                            )

                            OutlinedTextField(
                                value = studentNumber,
                                onValueChange = { studentNumber = it },
                                label = { Text("رقم الطالب بالمدرسة") },
                                placeholder = { Text("مثال: 1042") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reg_student_number_input")
                            )
                        }

                        // 5. اسم الفريق (اختياري)
                        OutlinedTextField(
                            value = teamName,
                            onValueChange = { teamName = it },
                            label = { Text("اسم الفريق (اختياري - إن تم تكوين الفرق)") },
                            placeholder = { Text("مثال: فريق صقور عيون مصر") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_team_name_input")
                        )

                        // 6. رقم هاتف ولي الأمر أو وسيلة تواصل اختيارية
                        OutlinedTextField(
                            value = parentContact,
                            onValueChange = { parentContact = it },
                            label = { Text("رقم هاتف ولي الأمر / وسيلة تواصل (اختياري)") },
                            placeholder = { Text("حسب نظام المدرسة لمتابعة التأهل") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_parent_contact_input")
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Button(
                            onClick = {
                                if (fullName.trim().length < 2) {
                                    errorMessage = "يرجى إدخال اسم الطالب ثلاثياً أو ثنائياً بوضوح."
                                } else if (classroom.trim().isEmpty()) {
                                    errorMessage = "يرجى كتابة اسم أو رقم الفصل الدراسي."
                                } else {
                                    onRegisterStudent(
                                        fullName,
                                        selectedGrade,
                                        classroom,
                                        studentNumber,
                                        teamName,
                                        parentContact
                                    ) { generated ->
                                        lookupFeedback = "تم إصدار كود المشاركة بنجاح: ${generated.participationCode}"
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("submit_registration_button")
                        ) {
                            Icon(imageVector = Icons.Default.ConfirmationNumber, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "إصدار 🎟️ كود المشاركة في المسابقة",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            // Quick Login by existing Participation Code
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "لديك 🎟️ كود مشاركة بالفعل؟ أدخله هنا:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = lookupCodeInput,
                                onValueChange = {
                                    lookupCodeInput = it
                                    lookupFeedback = null
                                },
                                label = { Text("كود المشاركة (مثال: OM-5-4821)") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("lookup_code_input")
                            )
                            Button(
                                onClick = {
                                    if (lookupCodeInput.isNotBlank()) {
                                        onLookupCode(lookupCodeInput) { found ->
                                            lookupFeedback = if (found != null) {
                                                "مرحباً بعودتك يا ${found.studentName}!"
                                            } else {
                                                "لم يتم العثور على هذا الكود، تأكد من كتابته بشكل صحيح أو سجّل كطالب جديد."
                                            }
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.height(54.dp)
                            ) {
                                Text("تفعيل الكود")
                            }
                        }
                        if (lookupFeedback != null) {
                            Text(
                                text = lookupFeedback!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Saved Student Cards List
            if (savedRegistrations.isNotEmpty()) {
                item {
                    Text(
                        text = "بطاقات الطلاب المسجلين على هذا الجهاز (${savedRegistrations.size}):",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items(savedRegistrations, key = { it.id }) { reg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onActivateExistingRegistration(reg) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = reg.studentName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${reg.gradeLevel} • فصل ${reg.classroom}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ChampionshipGold.copy(alpha = 0.22f)
                            ) {
                                Text(
                                    text = "🎟️ ${reg.participationCode}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color(0xFF92400E),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
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
fun ParticipationCodeTicketCard(
    registration: StudentRegistrationEntity,
    onStartQualifier: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("participation_code_ticket"),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(RoyalNavyDark, RoyalNavyCard, Color(0xFF1E3A8A))
                    )
                )
                .padding(20.dp),
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
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = CorrectEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "بطاقة دخول المتسابق المعتمدة",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = ChampionshipGold
                ) {
                    Text(
                        text = "🎟️ ${registration.participationCode}",
                        style = MaterialTheme.typography.titleMedium,
                        color = RoyalNavyDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "الطالب: ${registration.studentName}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = ChampionshipGold
                )
                Text(
                    text = "${registration.gradeLevel} • الفصل: ${registration.classroom}" +
                        if (registration.teamName.isNotBlank()) " • الفريق: ${registration.teamName}" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE2E8F0)
                )
            }

            Button(
                onClick = onStartQualifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CorrectEmerald,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("ticket_start_qualifier_button")
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "دخول اختبار التصفيات الأونلاين الآن (٥٠ سؤالاً • ٣٠ دقيقة)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TournamentGuideScreen(
    selectedSection: GuideSubSection,
    onSelectSection: (GuideSubSection) -> Unit,
    onStartQualifierExam: () -> Unit,
    onGoToRegistration: () -> Unit,
    onGoToTeamBattle: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section Switcher Pills (📖 كيف تلعب؟ | 🏆 مراحل البطولة | ℹ️ عن المسابقة)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedSection == GuideSubSection.HOW_TO_PLAY,
                        onClick = { onSelectSection(GuideSubSection.HOW_TO_PLAY) },
                        label = { Text("📖 كيف تلعب؟") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedSection == GuideSubSection.TOURNAMENT_STAGES,
                        onClick = { onSelectSection(GuideSubSection.TOURNAMENT_STAGES) },
                        label = { Text("🏆 مراحل البطولة") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedSection == GuideSubSection.ABOUT_COMPETITION,
                        onClick = { onSelectSection(GuideSubSection.ABOUT_COMPETITION) },
                        label = { Text("ℹ️ عن المسابقة") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            when (selectedSection) {
                GuideSubSection.TOURNAMENT_STAGES -> {
                    item {
                        StageCard(
                            stageBadge = "المرحلة الأولى 🌐",
                            title = "التصفيات الأونلاين (Online Qualifiers)",
                            description = "يدخل طلاب الصفوف الرابع والخامس والسادس الابتدائي من الهاتف أو الكمبيوتر باستخدام كود المشاركة لخوض اختبار فردي شامل.",
                            bulletPoints = listOf(
                                "عدد الأسئلة: ٥٠ سؤالاً متنوعاً في ٨ مجالات معرفية ومنطقية.",
                                "مدة الاختبار: ٣٠ دقيقة مع عداد تنازلي واضح أعلى الشاشة.",
                                "حرية التنقل: يمكن للطالب الانتقال بين الأسئلة الـ ٥٠ ومراجعتها قبل التسليم النهائي.",
                                "التأهل: يتم اختيار أعلى الطلاب درجات وأسرعهم زمناً للانتقال إلى المرحلة النهائية بالمدرسة."
                            ),
                            buttonText = "خوض اختبار التصفيات الأونلاين (٥٠ سؤالاً)",
                            onButtonClick = onStartQualifierExam,
                            accentColor = Color(0xFF0284C7)
                        )
                    }

                    item {
                        StageCard(
                            stageBadge = "المرحلة الثانية 🏫",
                            title = "البطولة النهائية داخل مدرسة عيون مصر",
                            description = "يتم تكوين فرق من الطلاب المتأهلين لتمثيل فصولهم الدراسية في مواجهات مباشرة وحماسية داخل المدرسة.",
                            bulletPoints = listOf(
                                "تكوين الفرق: يتنافس فريقان في كل مباراة بروح العمل الجماعي.",
                                "تحدي السرعة والبديهة: أسئلة متناوبة مع وسائل مساعدة ذكية (حذف إجابتين، وقت إضافي، حكمة العبقري).",
                                "التتويج: الفوز بكأس «عباقرة عيون مصر» وأوسمة التفوق الذهبية والفضية والبرونزية."
                            ),
                            buttonText = "بدء مواجهة الفرق والفصول داخل المدرسة",
                            onButtonClick = onGoToTeamBattle,
                            accentColor = ChampionshipGold
                        )
                    }
                }

                GuideSubSection.HOW_TO_PLAY -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "📖 خطوات المشاركة واللعب في عباقرة عيون مصر",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                StepInstructionRow(
                                    stepNumber = "١",
                                    title = "سجّل بياناتك واحصل على كود المشاركة 🎟️",
                                    desc = "اختر صفك الدراسي (الرابع أو الخامس أو السادس الابتدائي) واكتب اسمك وفصلك لتحصل على كود مشاركة فوري."
                                )
                                StepInstructionRow(
                                    stepNumber = "٢",
                                    title = "ابدأ اختبار التصفيات الأونلاين (٥٠ سؤالاً في ٣٠ دقيقة)",
                                    desc = "أجب عن أسئلة الاختيار من متعدد وأسئلة التفكير والملاحظة والتركيز، وتنقل بحرية بين الأسئلة من شريط الأرقام العلوي."
                                )
                                StepInstructionRow(
                                    stepNumber = "٣",
                                    title = "تدرّب في الجولات السريعة وتحدي الفرق",
                                    desc = "يمكنك التدرب في أي مجال منفرداً أو خوض مواجهة مباشرة بين فصلين دراسيين."
                                )

                                Button(
                                    onClick = onGoToRegistration,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("الانتقال إلى شاشة تسجيل الطالب وإصدار الكود")
                                }
                            }
                        }
                    }

                    // Official 50-Question Breakdown Table
                    item {
                        OfficialQuotaBreakdownCard()
                    }
                }

                GuideSubSection.ABOUT_COMPETITION -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = RoyalNavyCard)
                        ) {
                            Column(
                                modifier = Modifier.padding(22.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "🏆 عن مسابقة عباقرة عيون مصر",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = ChampionshipGold
                                )
                                Text(
                                    text = "«فكّر… أسرع. اعرف… أكثر. العب… كفريق!»",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                                Text(
                                    text = "نظام مسابقة مدرسية تفاعلية لطلاب الصفوف الرابع والخامس والسادس الابتدائي بمدرسة عيون مصر. تجمع المسابقة بين المعرفة العلمية، سرعة البديهة، التفكير المنطقي، التركيز والملاحظة، الثقافة العامة، وحب الوطن في إطار تنافسي ممتع يعزز العمل الجماعي.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFE2E8F0)
                                )
                            }
                        }
                    }

                    item {
                        OfficialQuotaBreakdownCard()
                    }
                }
            }
        }
    }
}

@Composable
private fun StageCard(
    stageBadge: String,
    title: String,
    description: String,
    bulletPoints: List<String>,
    buttonText: String,
    onButtonClick: () -> Unit,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = accentColor.copy(alpha = 0.16f)
            ) {
                Text(
                    text = stageBadge,
                    style = MaterialTheme.typography.labelLarge,
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            bulletPoints.forEach { point ->
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CorrectEmerald,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Button(
                onClick = onButtonClick,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = buttonText, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun StepInstructionRow(
    stepNumber: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = ChampionshipGold,
            modifier = Modifier.size(34.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stepNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = RoyalNavyDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OfficialQuotaBreakdownCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "توزيع أسئلة اختبار التصفيات الأونلاين (٥٠ سؤالاً • ٣٠ دقيقة)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            QuizCategory.officialQualifierDomains.forEach { domain ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = domain.emoji, style = MaterialTheme.typography.titleMedium)
                        Column {
                            Text(
                                text = domain.titleAr,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = domain.subtitleAr,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(domain.colorHex).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${domain.qualifierQuestionQuota} أسئلة",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(domain.colorHex),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
