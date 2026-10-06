package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.InitialQuestionsData
import com.example.data.QuestionEntity
import com.example.data.QuizCategory
import com.example.data.QuizRepository
import com.example.data.ScoreRecordEntity
import com.example.data.StudentRegistrationEntity
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    REGISTRATION,
    STAGES_AND_GUIDE,
    TEAMS,
    QUESTION_BANK,
    LEADERBOARD,
    WEB_PORTAL
}

enum class GuideSubSection {
    HOW_TO_PLAY,
    TOURNAMENT_STAGES,
    ABOUT_COMPETITION
}

sealed class ActiveSessionState {
    data object Idle : ActiveSessionState()

    // Official 50-Question Online Qualifier (30 Minutes, free navigation between questions)
    data class OnlineQualifierExam(
        val studentName: String,
        val gradeLevel: String,
        val classroom: String,
        val participationCode: String,
        val questions: List<QuestionEntity>,
        val currentIndex: Int = 0,
        val selectedAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> optionIndex (0..3)
        val flaggedQuestions: Set<Int> = emptySet(),
        val remainingSeconds: Int = 30 * 60, // 30 minutes = 1800 seconds
        val totalSeconds: Int = 30 * 60
    ) : ActiveSessionState() {
        val currentQuestion: QuestionEntity
            get() = questions[currentIndex]

        val answeredCount: Int
            get() = selectedAnswers.size
    }

    // Existing Interactive Arena (Solo Practice & Stage 2 School Team Battles)
    data class Playing(
        val isTeamMode: Boolean,
        val studentName: String,
        val selectedGrade: String,
        val team1Name: String,
        val team2Name: String,
        val team1Score: Int,
        val team2Score: Int,
        val currentTeamTurn: Int, // 1 or 2
        val category: QuizCategory,
        val questions: List<QuestionEntity>,
        val currentIndex: Int,
        val score: Int,
        val correctCount: Int,
        val streak: Int,
        val timeLeftSeconds: Int,
        val totalTimeSeconds: Int = 25,
        val selectedOptionIndex: Int? = null,
        val isAnswerRevealed: Boolean = false,
        val eliminatedOptionIndices: Set<Int> = emptySet(),
        val usedFiftyFifty: Boolean = false,
        val usedExtraTime: Boolean = false,
        val usedSkipQuestion: Boolean = false,
        val pointsEarnedThisTurn: Int = 0
    ) : ActiveSessionState() {
        val currentQuestion: QuestionEntity
            get() = questions[currentIndex]
    }

    data class Completed(
        val isTeamMode: Boolean,
        val isOfficialQualifier: Boolean = false,
        val winnerOrPlayerName: String,
        val gradeOrClassroom: String,
        val participationCode: String = "",
        val team1Name: String,
        val team1Score: Int,
        val team2Name: String,
        val team2Score: Int,
        val finalScore: Int,
        val totalPossibleScore: Int,
        val correctCount: Int,
        val totalQuestions: Int,
        val timeSpentSeconds: Int = 0,
        val category: QuizCategory,
        val badgeTitle: String,
        val domainBreakdown: Map<QuizCategory, Pair<Int, Int>> = emptyMap() // category -> (correct, total)
    ) : ActiveSessionState()
}

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    val allQuestions: StateFlow<List<QuestionEntity>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allScores: StateFlow<List<ScoreRecordEntity>> = repository.allScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRegistrations: StateFlow<List<StudentRegistrationEntity>> = repository.allRegistrations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTab = MutableStateFlow(MainTab.HOME)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _guideSubSection = MutableStateFlow(GuideSubSection.TOURNAMENT_STAGES)
    val guideSubSection: StateFlow<GuideSubSection> = _guideSubSection.asStateFlow()

    private val _showWelcomeScreen = MutableStateFlow(true)
    val showWelcomeScreen: StateFlow<Boolean> = _showWelcomeScreen.asStateFlow()

    // Current active registered student (if registered or logged in with participation code)
    private val _activeRegistration = MutableStateFlow<StudentRegistrationEntity?>(null)
    val activeRegistration: StateFlow<StudentRegistrationEntity?> = _activeRegistration.asStateFlow()

    // Setup configuration states for Solo Mode
    private val _studentName = MutableStateFlow("بطل عيون مصر")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _selectedGrade = MutableStateFlow("الصف الخامس الابتدائي")
    val selectedGrade: StateFlow<String> = _selectedGrade.asStateFlow()

    private val _questionCountPreference = MutableStateFlow(8)
    val questionCountPreference: StateFlow<Int> = _questionCountPreference.asStateFlow()

    // Setup configuration states for Team Mode (Stage 2 School Finals)
    private val _team1Name = MutableStateFlow("نسور عيون مصر (٥/أ)")
    val team1Name: StateFlow<String> = _team1Name.asStateFlow()

    private val _team2Name = MutableStateFlow("رواد المستقبل (٦/أ)")
    val team2Name: StateFlow<String> = _team2Name.asStateFlow()

    private val _teamCategory = MutableStateFlow(QuizCategory.MIXED)
    val teamCategory: StateFlow<QuizCategory> = _teamCategory.asStateFlow()

    // Question Bank filter state
    private val _bankCategoryFilter = MutableStateFlow<QuizCategory?>(null)
    val bankCategoryFilter: StateFlow<QuizCategory?> = _bankCategoryFilter.asStateFlow()

    private val _bankGradeFilter = MutableStateFlow<String?>(null)
    val bankGradeFilter: StateFlow<String?> = _bankGradeFilter.asStateFlow()

    private val _sessionState = MutableStateFlow<ActiveSessionState>(ActiveSessionState.Idle)
    val sessionState: StateFlow<ActiveSessionState> = _sessionState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.ensureSeedDataLoaded()
        }
    }

    fun selectTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun openGuideSection(section: GuideSubSection) {
        _guideSubSection.value = section
        _selectedTab.value = MainTab.STAGES_AND_GUIDE
        _showWelcomeScreen.value = false
    }

    fun dismissWelcomeScreen(targetTab: MainTab = MainTab.HOME) {
        _selectedTab.value = targetTab
        _showWelcomeScreen.value = false
    }

    fun returnToWelcomeScreen() {
        timerJob?.cancel()
        _sessionState.value = ActiveSessionState.Idle
        _showWelcomeScreen.value = true
    }

    fun updateStudentName(name: String) {
        _studentName.value = name
    }

    fun updateSelectedGrade(grade: String) {
        _selectedGrade.value = grade
    }

    fun updateQuestionCountPreference(count: Int) {
        _questionCountPreference.value = count
    }

    fun updateTeam1Name(name: String) {
        _team1Name.value = name
    }

    fun updateTeam2Name(name: String) {
        _team2Name.value = name
    }

    fun updateTeamCategory(category: QuizCategory) {
        _teamCategory.value = category
    }

    fun setBankCategoryFilter(category: QuizCategory?) {
        _bankCategoryFilter.value = category
    }

    fun setBankGradeFilter(grade: String?) {
        _bankGradeFilter.value = grade
    }

    // ==================== 3. Student Registration & Participation Code ====================
    fun registerNewStudent(
        fullName: String,
        gradeLevel: String,
        classroom: String,
        studentNumberOrCode: String,
        teamName: String,
        parentPhone: String,
        onGenerated: (StudentRegistrationEntity) -> Unit
    ) {
        val gradeDigit = when {
            gradeLevel.contains("الرابع") -> "4"
            gradeLevel.contains("الخامس") -> "5"
            gradeLevel.contains("السادس") -> "6"
            else -> "5"
        }
        val randomDigits = Random.nextInt(1000, 9999)
        val generatedCode = "OM-$gradeDigit-$randomDigits"

        val entity = StudentRegistrationEntity(
            studentName = fullName.trim(),
            gradeLevel = gradeLevel,
            classroom = classroom.trim().ifEmpty { "أ" },
            studentNumberOrId = studentNumberOrCode.trim().ifEmpty { "$randomDigits" },
            teamName = teamName.trim(),
            parentContact = parentPhone.trim(),
            participationCode = generatedCode
        )

        viewModelScope.launch {
            repository.registerStudent(entity)
            _activeRegistration.value = entity
            _studentName.value = entity.studentName
            _selectedGrade.value = entity.gradeLevel
            onGenerated(entity)
        }
    }

    fun activateRegistration(registration: StudentRegistrationEntity) {
        _activeRegistration.value = registration
        _studentName.value = registration.studentName
        _selectedGrade.value = registration.gradeLevel
    }

    fun deleteStudentRegistration(id: Int) {
        viewModelScope.launch {
            repository.deleteRegistration(id)
            if (_activeRegistration.value?.id == id) {
                _activeRegistration.value = null
            }
        }
    }

    fun lookupStudentByParticipationCode(
        code: String,
        onResult: (StudentRegistrationEntity?) -> Unit
    ) {
        viewModelScope.launch {
            val found = repository.findStudentByCode(code)
            if (found != null) {
                activateRegistration(found)
            }
            onResult(found)
        }
    }

    // ==================== 5 & 6. Official 50-Question Online Qualifier (30 Minutes) ====================
    fun startOfficialOnlineQualifierExam() {
        val currentList = allQuestions.value.ifEmpty {
            InitialQuestionsData.getSeedQuestions()
        }

        // Assemble exact quota from the 8 official domains:
        // Science: 10, Math: 8, Arabic: 6, Egypt & World: 6, Logic: 8, Observation: 6, General: 4, Tech: 2 = 50 total
        val assembledQuestions = mutableListOf<QuestionEntity>()
        QuizCategory.officialQualifierDomains.forEach { domain ->
            val domainQuestions = currentList.filter { it.categoryId == domain.id }.shuffled()
            val picked = domainQuestions.take(domain.qualifierQuestionQuota)
            assembledQuestions.addAll(picked)
        }

        // If for any reason custom deletions reduced a category below quota, top up to 50 from remaining
        if (assembledQuestions.size < 50) {
            val usedIds = assembledQuestions.map { it.id }.toSet()
            val remaining = currentList.filter { it.id !in usedIds }.shuffled()
            assembledQuestions.addAll(remaining.take(50 - assembledQuestions.size))
        }

        val activeReg = _activeRegistration.value
        val name = activeReg?.studentName ?: _studentName.value.trim().ifEmpty { "بطل عيون مصر" }
        val grade = activeReg?.gradeLevel ?: _selectedGrade.value
        val classroom = activeReg?.classroom ?: "عام"
        val code = activeReg?.participationCode ?: "OM-GUEST"

        _showWelcomeScreen.value = false
        _sessionState.value = ActiveSessionState.OnlineQualifierExam(
            studentName = name,
            gradeLevel = grade,
            classroom = classroom,
            participationCode = code,
            questions = assembledQuestions,
            currentIndex = 0,
            selectedAnswers = emptyMap(),
            flaggedQuestions = emptySet(),
            remainingSeconds = 30 * 60,
            totalSeconds = 30 * 60
        )
        startQualifierExamCountdown()
    }

    private fun startQualifierExamCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _sessionState.value
                if (current is ActiveSessionState.OnlineQualifierExam) {
                    if (current.remainingSeconds > 1) {
                        _sessionState.update { state ->
                            if (state is ActiveSessionState.OnlineQualifierExam) {
                                state.copy(remainingSeconds = state.remainingSeconds - 1)
                            } else state
                        }
                    } else {
                        finishOnlineQualifierExam()
                        break
                    }
                } else {
                    break
                }
            }
        }
    }

    fun selectQualifierAnswer(optionIndex: Int) {
        _sessionState.update { state ->
            if (state is ActiveSessionState.OnlineQualifierExam) {
                val updatedMap = state.selectedAnswers.toMutableMap()
                updatedMap[state.currentIndex] = optionIndex
                state.copy(selectedAnswers = updatedMap)
            } else state
        }
    }

    fun toggleQualifierQuestionFlag() {
        _sessionState.update { state ->
            if (state is ActiveSessionState.OnlineQualifierExam) {
                val updatedFlags = state.flaggedQuestions.toMutableSet()
                if (updatedFlags.contains(state.currentIndex)) {
                    updatedFlags.remove(state.currentIndex)
                } else {
                    updatedFlags.add(state.currentIndex)
                }
                state.copy(flaggedQuestions = updatedFlags)
            } else state
        }
    }

    fun jumpToQualifierQuestion(index: Int) {
        _sessionState.update { state ->
            if (state is ActiveSessionState.OnlineQualifierExam && index in state.questions.indices) {
                state.copy(currentIndex = index)
            } else state
        }
    }

    fun finishOnlineQualifierExam() {
        val current = _sessionState.value as? ActiveSessionState.OnlineQualifierExam ?: return
        timerJob?.cancel()

        var totalCorrect = 0
        val breakdown = mutableMapOf<QuizCategory, Pair<Int, Int>>()

        current.questions.forEachIndexed { index, question ->
            val chosen = current.selectedAnswers[index]
            val isCorrect = chosen != null && chosen == question.correctOptionIndex
            if (isCorrect) totalCorrect++

            val cat = question.category
            val prev = breakdown[cat] ?: (0 to 0)
            breakdown[cat] = (prev.first + if (isCorrect) 1 else 0) to (prev.second + 1)
        }

        val finalScoreOutOf100 = totalCorrect * 2
        val timeSpent = (current.totalSeconds - current.remainingSeconds).coerceAtLeast(1)
        val accuracyRatio = if (current.questions.isNotEmpty()) {
            totalCorrect.toFloat() / current.questions.size.toFloat()
        } else 0f

        val badge = when {
            accuracyRatio >= 0.85f -> "متأهل للبطولة النهائية • وسام عبقري عيون مصر الذهبي 🏆"
            accuracyRatio >= 0.70f -> "متأهل للبطولة النهائية • وسام التفوق الفضي 🥈"
            accuracyRatio >= 0.50f -> "مرشح للتأهل • وسام التميز البرونزي 🥉"
            else -> "وسام المحاولة والشجاعة • تدرب أكثر للتأهل!"
        }

        val completed = ActiveSessionState.Completed(
            isTeamMode = false,
            isOfficialQualifier = true,
            winnerOrPlayerName = current.studentName,
            gradeOrClassroom = "${current.gradeLevel} • فصل ${current.classroom}",
            participationCode = current.participationCode,
            team1Name = "",
            team1Score = 0,
            team2Name = "",
            team2Score = 0,
            finalScore = finalScoreOutOf100,
            totalPossibleScore = current.questions.size * 2,
            correctCount = totalCorrect,
            totalQuestions = current.questions.size,
            timeSpentSeconds = timeSpent,
            category = QuizCategory.MIXED,
            badgeTitle = badge,
            domainBreakdown = breakdown
        )
        _sessionState.value = completed

        viewModelScope.launch {
            repository.saveScoreRecord(
                ScoreRecordEntity(
                    isTeamMatch = false,
                    playerOrWinnerName = current.studentName,
                    gradeOrClassroom = "${current.gradeLevel} (${current.classroom})",
                    participationCode = current.participationCode,
                    score = finalScoreOutOf100,
                    totalPossibleScore = current.questions.size * 2,
                    correctAnswers = totalCorrect,
                    totalQuestions = current.questions.size,
                    timeSpentSeconds = timeSpent,
                    categoryTitle = "التصفيات الأونلاين (٥٠ سؤالاً)",
                    badgeTitle = badge
                )
            )
        }
    }

    // ==================== Existing Solo & Team Arena Modes ====================
    fun startSoloQuiz(category: QuizCategory) {
        val pool = filterQuestions(category, _selectedGrade.value)
        if (pool.isEmpty()) return
        val selectedQuestions = pool.shuffled().take(_questionCountPreference.value.coerceAtMost(pool.size))

        val cleanName = _activeRegistration.value?.studentName
            ?: _studentName.value.trim().ifEmpty { "بطل عيون مصر" }
        _showWelcomeScreen.value = false
        _sessionState.value = ActiveSessionState.Playing(
            isTeamMode = false,
            studentName = cleanName,
            selectedGrade = _selectedGrade.value,
            team1Name = "",
            team2Name = "",
            team1Score = 0,
            team2Score = 0,
            currentTeamTurn = 1,
            category = category,
            questions = selectedQuestions,
            currentIndex = 0,
            score = 0,
            correctCount = 0,
            streak = 0,
            timeLeftSeconds = 25,
            totalTimeSeconds = 25
        )
        startQuestionTimer()
    }

    fun startTeamCompetition(roundsPerTeam: Int = 4) {
        val totalNeeded = (roundsPerTeam * 2).coerceAtLeast(4)
        val pool = filterQuestions(_teamCategory.value, "كل الصفوف (٤ - ٦ ابتدائي)")
        if (pool.isEmpty()) return
        val selectedQuestions = pool.shuffled().take(totalNeeded.coerceAtMost(pool.size))

        val t1 = _team1Name.value.trim().ifEmpty { "الفريق الأول" }
        val t2 = _team2Name.value.trim().ifEmpty { "الفريق الثاني" }

        _showWelcomeScreen.value = false
        _sessionState.value = ActiveSessionState.Playing(
            isTeamMode = true,
            studentName = "",
            selectedGrade = "المرحلة الثانية: البطولة النهائية بالمدرسة",
            team1Name = t1,
            team2Name = t2,
            team1Score = 0,
            team2Score = 0,
            currentTeamTurn = 1,
            category = _teamCategory.value,
            questions = selectedQuestions,
            currentIndex = 0,
            score = 0,
            correctCount = 0,
            streak = 0,
            timeLeftSeconds = 30,
            totalTimeSeconds = 30
        )
        startQuestionTimer()
    }

    private fun filterQuestions(category: QuizCategory, gradeFilter: String): List<QuestionEntity> {
        val currentList = allQuestions.value.ifEmpty {
            InitialQuestionsData.getSeedQuestions()
        }
        val byCategory = if (category == QuizCategory.MIXED) {
            currentList
        } else {
            currentList.filter { it.categoryId == category.id }
        }

        val specificGrade = when {
            gradeFilter.contains("الرابع") -> "الصف الرابع"
            gradeFilter.contains("الخامس") -> "الصف الخامس"
            gradeFilter.contains("السادس") -> "الصف السادس"
            else -> null
        }

        if (specificGrade == null) return byCategory
        val filteredByGrade = byCategory.filter {
            it.gradeLevel.contains(specificGrade) || it.gradeLevel == "عام"
        }
        return filteredByGrade.ifEmpty { byCategory }
    }

    private fun startQuestionTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _sessionState.value
                if (current is ActiveSessionState.Playing && !current.isAnswerRevealed) {
                    if (current.timeLeftSeconds > 1) {
                        _sessionState.update { state ->
                            if (state is ActiveSessionState.Playing) {
                                state.copy(timeLeftSeconds = state.timeLeftSeconds - 1)
                            } else state
                        }
                    } else {
                        _sessionState.update { state ->
                            if (state is ActiveSessionState.Playing) {
                                state.copy(
                                    timeLeftSeconds = 0,
                                    selectedOptionIndex = -1,
                                    isAnswerRevealed = true,
                                    streak = 0,
                                    pointsEarnedThisTurn = 0
                                )
                            } else state
                        }
                        break
                    }
                } else {
                    break
                }
            }
        }
    }

    fun submitAnswer(optionIndex: Int) {
        val current = _sessionState.value as? ActiveSessionState.Playing ?: return
        if (current.isAnswerRevealed) return

        timerJob?.cancel()
        val question = current.currentQuestion
        val isCorrect = optionIndex == question.correctOptionIndex

        val speedBonus = if (isCorrect && current.timeLeftSeconds >= 15) 5 else 0
        val streakBonus = if (isCorrect && current.streak >= 2) 5 else 0
        val pointsEarned = if (isCorrect) question.points + speedBonus + streakBonus else 0

        val newTeam1Score = if (current.isTeamMode && current.currentTeamTurn == 1) {
            current.team1Score + pointsEarned
        } else {
            current.team1Score
        }

        val newTeam2Score = if (current.isTeamMode && current.currentTeamTurn == 2) {
            current.team2Score + pointsEarned
        } else {
            current.team2Score
        }

        _sessionState.value = current.copy(
            selectedOptionIndex = optionIndex,
            isAnswerRevealed = true,
            score = current.score + pointsEarned,
            team1Score = newTeam1Score,
            team2Score = newTeam2Score,
            correctCount = if (isCorrect) current.correctCount + 1 else current.correctCount,
            streak = if (isCorrect) current.streak + 1 else 0,
            pointsEarnedThisTurn = pointsEarned
        )
    }

    fun useFiftyFiftyLifeline() {
        val current = _sessionState.value as? ActiveSessionState.Playing ?: return
        if (current.isAnswerRevealed || current.usedFiftyFifty) return

        val correctIdx = current.currentQuestion.correctOptionIndex
        val wrongIndices = (0..3).filter { it != correctIdx }.shuffled().take(2).toSet()

        _sessionState.value = current.copy(
            eliminatedOptionIndices = wrongIndices,
            usedFiftyFifty = true
        )
    }

    fun useExtraTimeLifeline() {
        val current = _sessionState.value as? ActiveSessionState.Playing ?: return
        if (current.isAnswerRevealed || current.usedExtraTime) return

        val newTime = (current.timeLeftSeconds + 15).coerceAtMost(45)
        _sessionState.value = current.copy(
            timeLeftSeconds = newTime,
            totalTimeSeconds = maxOf(current.totalTimeSeconds, newTime),
            usedExtraTime = true
        )
    }

    fun useSkipQuestionLifeline() {
        val current = _sessionState.value as? ActiveSessionState.Playing ?: return
        if (current.isAnswerRevealed || current.usedSkipQuestion) return

        timerJob?.cancel()
        val question = current.currentQuestion
        val basePoints = question.points
        val newTeam1Score = if (current.isTeamMode && current.currentTeamTurn == 1) {
            current.team1Score + basePoints
        } else current.team1Score
        val newTeam2Score = if (current.isTeamMode && current.currentTeamTurn == 2) {
            current.team2Score + basePoints
        } else current.team2Score

        _sessionState.value = current.copy(
            selectedOptionIndex = question.correctOptionIndex,
            isAnswerRevealed = true,
            usedSkipQuestion = true,
            score = current.score + basePoints,
            team1Score = newTeam1Score,
            team2Score = newTeam2Score,
            correctCount = current.correctCount + 1,
            pointsEarnedThisTurn = basePoints
        )
    }

    fun proceedToNextQuestion() {
        val current = _sessionState.value as? ActiveSessionState.Playing ?: return
        if (current.currentIndex + 1 < current.questions.size) {
            val nextTurn = if (current.isTeamMode) {
                if (current.currentTeamTurn == 1) 2 else 1
            } else 1
            val defaultSeconds = if (current.isTeamMode) 30 else 25

            _sessionState.value = current.copy(
                currentIndex = current.currentIndex + 1,
                currentTeamTurn = nextTurn,
                timeLeftSeconds = defaultSeconds,
                totalTimeSeconds = defaultSeconds,
                selectedOptionIndex = null,
                isAnswerRevealed = false,
                eliminatedOptionIndices = emptySet(),
                pointsEarnedThisTurn = 0
            )
            startQuestionTimer()
        } else {
            finishQuizSession(current)
        }
    }

    private fun finishQuizSession(current: ActiveSessionState.Playing) {
        timerJob?.cancel()
        val totalPossible = current.questions.sumOf { it.points + 10 }
        val accuracyRatio = if (current.questions.isNotEmpty()) {
            current.correctCount.toFloat() / current.questions.size.toFloat()
        } else 0f

        val badge = when {
            accuracyRatio >= 0.9f -> "وسام عبقري مدرسة عيون مصر الذهبي"
            accuracyRatio >= 0.75f -> "وسام التفوق العلمي الفضي"
            accuracyRatio >= 0.5f -> "وسام الباحث المجتهد البرونزي"
            else -> "وسام المشاركة والشجاعة"
        }

        val winnerName = if (current.isTeamMode) {
            when {
                current.team1Score > current.team2Score -> current.team1Name
                current.team2Score > current.team1Score -> current.team2Name
                else -> "تعادل الأبطال (${current.team1Name} و ${current.team2Name})"
            }
        } else {
            current.studentName
        }

        val winningScore = if (current.isTeamMode) {
            maxOf(current.team1Score, current.team2Score)
        } else {
            current.score
        }

        val completedState = ActiveSessionState.Completed(
            isTeamMode = current.isTeamMode,
            isOfficialQualifier = false,
            winnerOrPlayerName = winnerName,
            gradeOrClassroom = current.selectedGrade,
            participationCode = _activeRegistration.value?.participationCode ?: "",
            team1Name = current.team1Name,
            team1Score = current.team1Score,
            team2Name = current.team2Name,
            team2Score = current.team2Score,
            finalScore = winningScore,
            totalPossibleScore = totalPossible,
            correctCount = current.correctCount,
            totalQuestions = current.questions.size,
            category = current.category,
            badgeTitle = badge
        )
        _sessionState.value = completedState

        viewModelScope.launch {
            repository.saveScoreRecord(
                ScoreRecordEntity(
                    isTeamMatch = current.isTeamMode,
                    playerOrWinnerName = winnerName,
                    gradeOrClassroom = current.selectedGrade,
                    participationCode = _activeRegistration.value?.participationCode ?: "",
                    team1Name = current.team1Name,
                    team1Score = current.team1Score,
                    team2Name = current.team2Name,
                    team2Score = current.team2Score,
                    score = winningScore,
                    totalPossibleScore = totalPossible,
                    correctAnswers = current.correctCount,
                    totalQuestions = current.questions.size,
                    categoryTitle = current.category.titleAr,
                    badgeTitle = badge
                )
            )
        }
    }

    fun exitSessionToHome() {
        timerJob?.cancel()
        _sessionState.value = ActiveSessionState.Idle
    }

    fun addCustomQuestion(
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
    ) {
        viewModelScope.launch {
            repository.addCustomQuestion(
                QuestionEntity(
                    categoryId = category.id,
                    gradeLevel = gradeLevel,
                    difficulty = difficulty,
                    questionText = questionText.trim(),
                    optionA = optionA.trim(),
                    optionB = optionB.trim(),
                    optionC = optionC.trim(),
                    optionD = optionD.trim(),
                    correctOptionIndex = correctOptionIndex,
                    explanation = explanation.trim().ifEmpty { "إجابة صحيحة معتمدة من بنك أسئلة مدرسة عيون مصر." },
                    isCustom = true
                )
            )
        }
    }

    fun deleteQuestion(id: Int) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
        }
    }

    fun clearAllScores() {
        viewModelScope.launch {
            repository.clearAllScores()
        }
    }
}

class QuizViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
