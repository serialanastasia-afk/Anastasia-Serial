package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM questions ORDER BY isCustom DESC, id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Int)

    @Query("SELECT * FROM score_records ORDER BY score DESC, timeSpentSeconds ASC, timestamp DESC")
    fun getAllScores(): Flow<List<ScoreRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreRecord: ScoreRecordEntity)

    @Query("DELETE FROM score_records")
    suspend fun clearAllScores()

    @Query("SELECT * FROM student_registrations ORDER BY registeredAt DESC")
    fun getAllRegistrations(): Flow<List<StudentRegistrationEntity>>

    @Query("SELECT * FROM student_registrations WHERE participationCode = :code LIMIT 1")
    suspend fun findRegistrationByCode(code: String): StudentRegistrationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: StudentRegistrationEntity): Long
}
