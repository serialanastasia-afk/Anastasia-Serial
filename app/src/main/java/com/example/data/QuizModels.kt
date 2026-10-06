package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuizCategory(
    val id: String,
    val titleAr: String,
    val emoji: String,
    val subtitleAr: String,
    val qualifierQuestionQuota: Int,
    val colorHex: Long
) {
    SCIENCE("SCIENCE", "العلوم والفضاء", "🔬", "الطاقة، الفضاء، الكائنات الحية، وأجهزة الجسم", 10, 0xFF0284C7),
    MATH_LOGIC("MATH_LOGIC", "الرياضيات والذكاء الحسابي", "➗", "الحساب الذهني، الكسور، الهندسة، والمعادلات", 8, 0xFF7C3AED),
    ARABIC("ARABIC", "لغتنا الجميلة (اللغة العربية)", "📚", "النحو، الإملاء، المفردات، والشعر العربي", 6, 0xFF0D9488),
    EGYPT_HISTORY("EGYPT_HISTORY", "مصر والعالم (تاريخ وجغرافيا)", "🇪🇬", "حضارة مصر، المحافظات، القارات، ومعالم العالم", 6, 0xFFD97706),
    LOGIC_DEDUCTION("LOGIC_DEDUCTION", "المنطق والاستنتاج", "🧩", "الأنماط المنطقية، الاستنباط، وحل المشكلات", 8, 0xFF4F46E5),
    OBSERVATION_FOCUS("OBSERVATION_FOCUS", "الملاحظة والتركيز", "👀", "قوة الملاحظة، الاختلافات الدقيقة، والترتيب", 6, 0xFF059669),
    GENERAL("GENERAL", "الثقافة والمعلومات العامة", "🌍", "علماء، اختراعات، فنون، ومعلومات إثرائية", 4, 0xFFE11D48),
    TECH_INNOVATION("TECH_INNOVATION", "التكنولوجيا والابتكار", "💻", "الحاسب الآلي، البرمجة، الإنترنت الآمن، والروبوتات", 2, 0xFF0891B2),
    MIXED("MIXED", "تحدي العباقرة الشامل", "🏆", "أسئلة متنوعة من كافة المجالات الثمانية", 50, 0xFF1D4ED8);

    companion object {
        fun fromId(id: String): QuizCategory = entries.find { it.id == id } ?: GENERAL

        val officialQualifierDomains: List<QuizCategory>
            get() = listOf(
                SCIENCE,
                MATH_LOGIC,
                ARABIC,
                EGYPT_HISTORY,
                LOGIC_DEDUCTION,
                OBSERVATION_FOCUS,
                GENERAL,
                TECH_INNOVATION
            )
    }
}

@Entity(tableName = "student_registrations")
data class StudentRegistrationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val gradeLevel: String, // الصف الرابع الابتدائي, الصف الخامس الابتدائي, الصف السادس الابتدائي
    val classroom: String,  // مثال: 5/أ
    val studentNumberOrId: String,
    val teamName: String = "",
    val parentContact: String = "",
    val participationCode: String, // e.g., OM-5-4821
    val registeredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val gradeLevel: String, // "الصف الرابع", "الصف الخامس", "الصف السادس", "عام"
    val difficulty: Int, // 1 = سهل (10 نقاط), 2 = متوسط (20 نقطة), 3 = عبقري (30 نقطة)
    val questionText: String,
    val visualClueText: String = "", // لوحة بصرية اختيارية لأسئلة الملاحظة والتركيز والمنطق
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int, // 0..3
    val explanation: String,
    val isCustom: Boolean = false
) {
    val options: List<String>
        get() = listOf(optionA, optionB, optionC, optionD)

    val points: Int
        get() = when (difficulty) {
            1 -> 10
            2 -> 20
            else -> 30
        }

    val difficultyLabel: String
        get() = when (difficulty) {
            1 -> "مستوى أساسي (١٠ نقاط)"
            2 -> "مستوى متقدم (٢٠ نقطة)"
            else -> "سؤال العباقرة (٣٠ نقطة)"
        }

    val category: QuizCategory
        get() = QuizCategory.fromId(categoryId)
}

@Entity(tableName = "score_records")
data class ScoreRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isTeamMatch: Boolean,
    val playerOrWinnerName: String,
    val gradeOrClassroom: String,
    val participationCode: String = "",
    val team1Name: String = "",
    val team1Score: Int = 0,
    val team2Name: String = "",
    val team2Score: Int = 0,
    val score: Int,
    val totalPossibleScore: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeSpentSeconds: Int = 0,
    val categoryTitle: String,
    val badgeTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
