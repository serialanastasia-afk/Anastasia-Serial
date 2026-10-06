package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.QuizRepository
import com.example.ui.ActiveQuizArenaScreen
import com.example.ui.ActiveSessionState
import com.example.ui.CelebrationResultScreen
import com.example.ui.GuideSubSection
import com.example.ui.HomeAndSetupScreens
import com.example.ui.LeaderboardScreen
import com.example.ui.MainTab
import com.example.ui.OnlineQualifierExamScreen
import com.example.ui.QuestionBankScreen
import com.example.ui.QuizViewModel
import com.example.ui.QuizViewModelFactory
import com.example.ui.StudentRegistrationScreen
import com.example.ui.TeamBattleSetupScreen
import com.example.ui.TournamentGuideScreen
import com.example.ui.WelcomeSplashScreen
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoyalNavyDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = QuizRepository(database.quizDao())
        val factory = QuizViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val quizViewModel: QuizViewModel = viewModel(factory = factory)
                OyounMasrQuizApp(viewModel = quizViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OyounMasrQuizApp(viewModel: QuizViewModel) {
    val showWelcomeScreen by viewModel.showWelcomeScreen.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val guideSubSection by viewModel.guideSubSection.collectAsStateWithLifecycle()
    val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()
    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val allScores by viewModel.allScores.collectAsStateWithLifecycle()
    val allRegistrations by viewModel.allRegistrations.collectAsStateWithLifecycle()
    val activeRegistration by viewModel.activeRegistration.collectAsStateWithLifecycle()

    val studentName by viewModel.studentName.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val questionCount by viewModel.questionCountPreference.collectAsStateWithLifecycle()

    val team1Name by viewModel.team1Name.collectAsStateWithLifecycle()
    val team2Name by viewModel.team2Name.collectAsStateWithLifecycle()
    val teamCategory by viewModel.teamCategory.collectAsStateWithLifecycle()

    val bankCategoryFilter by viewModel.bankCategoryFilter.collectAsStateWithLifecycle()
    val bankGradeFilter by viewModel.bankGradeFilter.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val isExpandedScreen = configuration.screenWidthDp >= 600

    AnimatedContent(
        targetState = showWelcomeScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "welcome_to_main_transition"
    ) { isWelcomeVisible ->
        if (isWelcomeVisible) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.safeDrawing
            ) { welcomePadding ->
                WelcomeSplashScreen(
                    totalQuestionsCount = allQuestions.size.coerceAtLeast(70),
                    onStartClicked = { viewModel.dismissWelcomeScreen(MainTab.HOME) },
                    onHowToPlayClicked = { viewModel.openGuideSection(GuideSubSection.HOW_TO_PLAY) },
                    onStagesClicked = { viewModel.openGuideSection(GuideSubSection.TOURNAMENT_STAGES) },
                    onResultsClicked = { viewModel.dismissWelcomeScreen(MainTab.LEADERBOARD) },
                    onAboutClicked = { viewModel.openGuideSection(GuideSubSection.ABOUT_COMPETITION) },
                    onQuickTeamMatchClicked = { viewModel.dismissWelcomeScreen(MainTab.TEAMS) },
                    modifier = Modifier.padding(welcomePadding)
                )
            }
        } else {
            if (sessionState is ActiveSessionState.Idle) {
                BackHandler {
                    viewModel.returnToWelcomeScreen()
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    if (sessionState is ActiveSessionState.Idle) {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "شعار المدرسة",
                                        tint = ChampionshipGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🏆 عباقرة عيون مصر",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { viewModel.selectTab(MainTab.STAGES_AND_GUIDE) },
                                    modifier = Modifier.testTag("top_guide_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "مراحل البطولة وكيف تلعب",
                                        tint = Color.White
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { viewModel.returnToWelcomeScreen() },
                                    modifier = Modifier.testTag("return_to_welcome_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "شاشة الترحيب",
                                        tint = ChampionshipGold
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = RoyalNavyDark,
                                titleContentColor = Color.White
                            )
                        )
                    }
                },
                bottomBar = {
                    if (sessionState is ActiveSessionState.Idle && !isExpandedScreen) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavigationBarItem(
                                selected = selectedTab == MainTab.HOME,
                                onClick = { viewModel.selectTab(MainTab.HOME) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "الرئيسية") },
                                label = { Text("المسابقة") },
                                modifier = Modifier.testTag("nav_tab_home")
                            )
                            NavigationBarItem(
                                selected = selectedTab == MainTab.REGISTRATION,
                                onClick = { viewModel.selectTab(MainTab.REGISTRATION) },
                                icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "التسجيل") },
                                label = { Text("التسجيل") },
                                modifier = Modifier.testTag("nav_tab_registration")
                            )
                            NavigationBarItem(
                                selected = selectedTab == MainTab.TEAMS,
                                onClick = { viewModel.selectTab(MainTab.TEAMS) },
                                icon = { Icon(Icons.Default.Groups, contentDescription = "نهائيات الفرق") },
                                label = { Text("الفرق") },
                                modifier = Modifier.testTag("nav_tab_teams")
                            )
                            NavigationBarItem(
                                selected = selectedTab == MainTab.QUESTION_BANK,
                                onClick = { viewModel.selectTab(MainTab.QUESTION_BANK) },
                                icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "بنك الأسئلة") },
                                label = { Text("الأسئلة") },
                                modifier = Modifier.testTag("nav_tab_bank")
                            )
                            NavigationBarItem(
                                selected = selectedTab == MainTab.LEADERBOARD,
                                onClick = { viewModel.selectTab(MainTab.LEADERBOARD) },
                                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "النتائج") },
                                label = { Text("النتائج") },
                                modifier = Modifier.testTag("nav_tab_leaderboard")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (sessionState is ActiveSessionState.Idle && isExpandedScreen) {
                        NavigationRail(
                            modifier = Modifier.fillMaxHeight(),
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            NavigationRailItem(
                                selected = selectedTab == MainTab.HOME,
                                onClick = { viewModel.selectTab(MainTab.HOME) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "المسابقة") },
                                label = { Text("المسابقة") },
                                modifier = Modifier.testTag("rail_tab_home")
                            )
                            NavigationRailItem(
                                selected = selectedTab == MainTab.REGISTRATION,
                                onClick = { viewModel.selectTab(MainTab.REGISTRATION) },
                                icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "التسجيل") },
                                label = { Text("التسجيل") },
                                modifier = Modifier.testTag("rail_tab_registration")
                            )
                            NavigationRailItem(
                                selected = selectedTab == MainTab.STAGES_AND_GUIDE,
                                onClick = { viewModel.selectTab(MainTab.STAGES_AND_GUIDE) },
                                icon = { Icon(Icons.Default.Info, contentDescription = "المراحل") },
                                label = { Text("المراحل") },
                                modifier = Modifier.testTag("rail_tab_guide")
                            )
                            NavigationRailItem(
                                selected = selectedTab == MainTab.TEAMS,
                                onClick = { viewModel.selectTab(MainTab.TEAMS) },
                                icon = { Icon(Icons.Default.Groups, contentDescription = "الفرق") },
                                label = { Text("الفرق") },
                                modifier = Modifier.testTag("rail_tab_teams")
                            )
                            NavigationRailItem(
                                selected = selectedTab == MainTab.QUESTION_BANK,
                                onClick = { viewModel.selectTab(MainTab.QUESTION_BANK) },
                                icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "بنك الأسئلة") },
                                label = { Text("الأسئلة") },
                                modifier = Modifier.testTag("rail_tab_bank")
                            )
                            NavigationRailItem(
                                selected = selectedTab == MainTab.LEADERBOARD,
                                onClick = { viewModel.selectTab(MainTab.LEADERBOARD) },
                                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "النتائج") },
                                label = { Text("النتائج") },
                                modifier = Modifier.testTag("rail_tab_leaderboard")
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        when (val currentSession = sessionState) {
                            is ActiveSessionState.OnlineQualifierExam -> {
                                OnlineQualifierExamScreen(
                                    state = currentSession,
                                    onSelectAnswer = viewModel::selectQualifierAnswer,
                                    onToggleFlag = viewModel::toggleQualifierQuestionFlag,
                                    onJumpToQuestion = viewModel::jumpToQualifierQuestion,
                                    onFinishExam = viewModel::finishOnlineQualifierExam,
                                    onExitExam = viewModel::exitSessionToHome
                                )
                            }

                            is ActiveSessionState.Playing -> {
                                ActiveQuizArenaScreen(
                                    state = currentSession,
                                    onSelectOption = viewModel::submitAnswer,
                                    onUseFiftyFifty = viewModel::useFiftyFiftyLifeline,
                                    onUseExtraTime = viewModel::useExtraTimeLifeline,
                                    onUseSkipQuestion = viewModel::useSkipQuestionLifeline,
                                    onNextQuestion = viewModel::proceedToNextQuestion,
                                    onExitQuiz = viewModel::exitSessionToHome
                                )
                            }

                            is ActiveSessionState.Completed -> {
                                CelebrationResultScreen(
                                    state = currentSession,
                                    onPlayAgainSameCategory = {
                                        when {
                                            currentSession.isOfficialQualifier -> viewModel.startOfficialOnlineQualifierExam()
                                            currentSession.isTeamMode -> viewModel.startTeamCompetition(4)
                                            else -> viewModel.startSoloQuiz(currentSession.category)
                                        }
                                    },
                                    onBackToHome = viewModel::exitSessionToHome
                                )
                            }

                            ActiveSessionState.Idle -> {
                                when (selectedTab) {
                                    MainTab.HOME -> {
                                        HomeAndSetupScreens(
                                            studentName = studentName,
                                            selectedGrade = selectedGrade,
                                            questionCount = questionCount,
                                            activeRegistration = activeRegistration,
                                            allQuestions = allQuestions,
                                            onStudentNameChange = viewModel::updateStudentName,
                                            onGradeChange = viewModel::updateSelectedGrade,
                                            onQuestionCountChange = viewModel::updateQuestionCountPreference,
                                            onStartOfficialQualifierExam = viewModel::startOfficialOnlineQualifierExam,
                                            onOpenRegistration = { viewModel.selectTab(MainTab.REGISTRATION) },
                                            onStartSoloQuiz = viewModel::startSoloQuiz,
                                            onNavigateToTeams = { viewModel.selectTab(MainTab.TEAMS) }
                                        )
                                    }

                                    MainTab.REGISTRATION -> {
                                        StudentRegistrationScreen(
                                            activeRegistration = activeRegistration,
                                            savedRegistrations = allRegistrations,
                                            onRegisterStudent = viewModel::registerNewStudent,
                                            onActivateExistingRegistration = viewModel::activateRegistration,
                                            onLookupCode = viewModel::lookupStudentByParticipationCode,
                                            onStartOfficialQualifierExam = viewModel::startOfficialOnlineQualifierExam
                                        )
                                    }

                                    MainTab.STAGES_AND_GUIDE -> {
                                        TournamentGuideScreen(
                                            selectedSection = guideSubSection,
                                            onSelectSection = viewModel::openGuideSection,
                                            onStartQualifierExam = viewModel::startOfficialOnlineQualifierExam,
                                            onGoToRegistration = { viewModel.selectTab(MainTab.REGISTRATION) },
                                            onGoToTeamBattle = { viewModel.selectTab(MainTab.TEAMS) }
                                        )
                                    }

                                    MainTab.TEAMS -> {
                                        TeamBattleSetupScreen(
                                            team1Name = team1Name,
                                            team2Name = team2Name,
                                            selectedCategory = teamCategory,
                                            onTeam1NameChange = viewModel::updateTeam1Name,
                                            onTeam2NameChange = viewModel::updateTeam2Name,
                                            onCategorySelect = viewModel::updateTeamCategory,
                                            onStartTeamBattle = viewModel::startTeamCompetition
                                        )
                                    }

                                    MainTab.QUESTION_BANK -> {
                                        QuestionBankScreen(
                                            allQuestions = allQuestions,
                                            selectedCategoryFilter = bankCategoryFilter,
                                            selectedGradeFilter = bankGradeFilter,
                                            onCategoryFilterChange = viewModel::setBankCategoryFilter,
                                            onGradeFilterChange = viewModel::setBankGradeFilter,
                                            onAddQuestion = viewModel::addCustomQuestion,
                                            onDeleteQuestion = viewModel::deleteQuestion
                                        )
                                    }

                                    MainTab.LEADERBOARD -> {
                                        LeaderboardScreen(
                                            scores = allScores,
                                            onClearScores = viewModel::clearAllScores
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
