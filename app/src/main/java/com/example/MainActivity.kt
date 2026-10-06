package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.ui.HomeAndSetupScreens
import com.example.ui.LeaderboardScreen
import com.example.ui.MainTab
import com.example.ui.QuestionBankScreen
import com.example.ui.QuizViewModel
import com.example.ui.QuizViewModelFactory
import com.example.ui.TeamBattleSetupScreen
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
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()
    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val allScores by viewModel.allScores.collectAsStateWithLifecycle()

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
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "عباقرة مدرسة عيون مصر",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
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
                        label = { Text("الرئيسية") },
                        modifier = Modifier.testTag("nav_tab_home")
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainTab.TEAMS,
                        onClick = { viewModel.selectTab(MainTab.TEAMS) },
                        icon = { Icon(Icons.Default.Groups, contentDescription = "تحدي الفرق") },
                        label = { Text("تحدي الفصول") },
                        modifier = Modifier.testTag("nav_tab_teams")
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainTab.QUESTION_BANK,
                        onClick = { viewModel.selectTab(MainTab.QUESTION_BANK) },
                        icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "بنك الأسئلة") },
                        label = { Text("بنك الأسئلة") },
                        modifier = Modifier.testTag("nav_tab_bank")
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainTab.LEADERBOARD,
                        onClick = { viewModel.selectTab(MainTab.LEADERBOARD) },
                        icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "لوحة الشرف") },
                        label = { Text("لوحة الشرف") },
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
                        icon = { Icon(Icons.Default.Home, contentDescription = "الرئيسية") },
                        label = { Text("الرئيسية") },
                        modifier = Modifier.testTag("rail_tab_home")
                    )
                    NavigationRailItem(
                        selected = selectedTab == MainTab.TEAMS,
                        onClick = { viewModel.selectTab(MainTab.TEAMS) },
                        icon = { Icon(Icons.Default.Groups, contentDescription = "تحدي الفصول") },
                        label = { Text("تحدي الفصول") },
                        modifier = Modifier.testTag("rail_tab_teams")
                    )
                    NavigationRailItem(
                        selected = selectedTab == MainTab.QUESTION_BANK,
                        onClick = { viewModel.selectTab(MainTab.QUESTION_BANK) },
                        icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "بنك الأسئلة") },
                        label = { Text("بنك الأسئلة") },
                        modifier = Modifier.testTag("rail_tab_bank")
                    )
                    NavigationRailItem(
                        selected = selectedTab == MainTab.LEADERBOARD,
                        onClick = { viewModel.selectTab(MainTab.LEADERBOARD) },
                        icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "لوحة الشرف") },
                        label = { Text("لوحة الشرف") },
                        modifier = Modifier.testTag("rail_tab_leaderboard")
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (val currentSession = sessionState) {
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
                                if (currentSession.isTeamMode) {
                                    viewModel.startTeamCompetition(4)
                                } else {
                                    viewModel.startSoloQuiz(currentSession.category)
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
                                    allQuestions = allQuestions,
                                    onStudentNameChange = viewModel::updateStudentName,
                                    onGradeChange = viewModel::updateSelectedGrade,
                                    onQuestionCountChange = viewModel::updateQuestionCountPreference,
                                    onStartSoloQuiz = viewModel::startSoloQuiz,
                                    onNavigateToTeams = { viewModel.selectTab(MainTab.TEAMS) }
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
