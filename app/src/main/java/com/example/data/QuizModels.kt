package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuizCategory(
    val id: String,
    val titleAr: String,
    val subtitleAr: String,
    val colorHex: Long
) {
    SCIENCE("SCIENCE", "العلوم والفضاء", "جسم الإنسان، الطاقة، الكواكب والطبيعة", 0xFF0284C7),
    EGYPT_HISTORY("EGYPT_HISTORY", "تاريخ وجغرافيا مصر", "الحضارة المصرية، المحافظات، ونهر النيل", 0xFFD97706),
    ARABIC("ARABIC", "لغتنا الجميلة", "النحو، المفردات، والشعر العربي", 0xFF0D9488),
    MATH_LOGIC("MATH_LOGIC", "الرياضيات والذكاء", "الحساب الذهني، الهندسة، والألغاز", 0xFF7C3AED),
    GENERAL("GENERAL", "المعلومات العامة", "عواصم العالم، اختراعات، وثقافة", 0xFFE11D48),
    MIXED("MIXED", "تحدي العباقرة الشامل", "أسئلة متنوعة من كافة المجالات", 0xFF1D4ED8);

    companion object {
        fun fromId(id: String): QuizCategory = entries.find { it.id == id } ?: GENERAL
    }
}

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val gradeLevel: String, // "الصف الرابع", "الصف الخامس", "الصف السادس", "عام"
    val difficulty: Int, // 1 = سهل (10 نقاط), 2 = متوسط (20 نقطة), 3 = عبقري (30 نقطة)
    val questionText: String,
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
}

@Entity(tableName = "score_records")
data class ScoreRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isTeamMatch: Boolean,
    val playerOrWinnerName: String,
    val gradeOrClassroom: String,
    val team1Name: String = "",
    val team1Score: Int = 0,
    val team2Name: String = "",
    val team2Score: Int = 0,
    val score: Int,
    val totalPossibleScore: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val categoryTitle: String,
    val badgeTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
