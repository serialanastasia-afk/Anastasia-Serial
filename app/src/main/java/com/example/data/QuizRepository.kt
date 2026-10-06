package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [QuestionEntity::class, ScoreRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "oyoun_masr_quiz_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class QuizRepository(private val quizDao: QuizDao) {
    val allQuestions: Flow<List<QuestionEntity>> = quizDao.getAllQuestions()
    val allScores: Flow<List<ScoreRecordEntity>> = quizDao.getAllScores()

    suspend fun ensureSeedDataLoaded() {
        val count = quizDao.getQuestionCount()
        if (count == 0) {
            quizDao.insertQuestions(InitialQuestionsData.getSeedQuestions())
            // Seed two inspiring initial school honor board records so the leaderboard is welcoming on first launch
            quizDao.insertScore(
                ScoreRecordEntity(
                    isTeamMatch = false,
                    playerOrWinnerName = "يوسف أحمد محمود",
                    gradeOrClassroom = "الصف السادس",
                    score = 180,
                    totalPossibleScore = 200,
                    correctAnswers = 9,
                    totalQuestions = 10,
                    categoryTitle = "تحدي العباقرة الشامل",
                    badgeTitle = "عبقري مدرسة عيون مصر الذهبي"
                )
            )
            quizDao.insertScore(
                ScoreRecordEntity(
                    isTeamMatch = true,
                    playerOrWinnerName = "فريق صقور عيون مصر (5/أ)",
                    gradeOrClassroom = "تحدي الفصول",
                    team1Name = "صقور عيون مصر (5/أ)",
                    team1Score = 150,
                    team2Name = "نجوم المستقبل (5/ب)",
                    team2Score = 120,
                    score = 150,
                    totalPossibleScore = 180,
                    correctAnswers = 8,
                    totalQuestions = 10,
                    categoryTitle = "العلوم والفضاء",
                    badgeTitle = "كأس عباقرة الفصول"
                )
            )
        }
    }

    suspend fun addCustomQuestion(question: QuestionEntity) {
        quizDao.insertQuestion(question)
    }

    suspend fun deleteQuestion(id: Int) {
        quizDao.deleteQuestionById(id)
    }

    suspend fun saveScoreRecord(record: ScoreRecordEntity) {
        quizDao.insertScore(record)
    }

    suspend fun clearAllScores() {
        quizDao.clearAllScores()
    }
}
