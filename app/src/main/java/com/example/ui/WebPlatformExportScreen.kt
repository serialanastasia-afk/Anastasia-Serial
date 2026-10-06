package com.example.ui

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.InitialQuestionsData
import com.example.data.QuestionEntity
import com.example.data.ScoreRecordEntity
import com.example.data.StudentRegistrationEntity
import com.example.data.WebAppExporter
import com.example.ui.theme.ChampionshipGold
import com.example.ui.theme.CorrectEmerald
import com.example.ui.theme.RoyalNavyCard
import com.example.ui.theme.RoyalNavyDark

@Composable
fun WebPlatformExportScreen(
    allQuestions: List<QuestionEntity>,
    allScores: List<ScoreRecordEntity>,
    allRegistrations: List<StudentRegistrationEntity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val effectiveQuestions = remember(allQuestions) {
        allQuestions.ifEmpty { InitialQuestionsData.getSeedQuestions() }
    }

    val standaloneHtml = remember(effectiveQuestions, allScores, allRegistrations) {
        WebAppExporter.generateStandaloneHtml(
            questions = effectiveQuestions,
            scores = allScores,
            registrations = allRegistrations
        )
    }

    var viewMode by remember { mutableStateOf("PREVIEW") } // "PREVIEW" or "CODE"
    var statusBanner by remember { mutableStateOf<String?>(null) }

    val saveHtmlLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/html")
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    out.write(standaloneHtml.toByteArray(Charsets.UTF_8))
                }
                statusBanner = "تم حفظ ملف الموقع (index.html) بنجاح! يمكنك رفعه مباشرة على أي استضافة ويب."
            }.onFailure {
                statusBanner = "تعذر حفظ الملف، يمكنك استخدام زر نسخ الكود أو مشاركته."
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 820.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalNavyCard)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = ChampionshipGold,
                                modifier = Modifier.size(30.dp)
                            )
                            Column {
                                Text(
                                    text = "🌐 نسخة موقع الويب الجاهزة للنشر (Web App)",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "ملف HTML5 تفاعلي متكامل يضم الـ ${effectiveQuestions.size} سؤالاً، نظام التسجيل وإصدار الأكواد، واختبار الـ ٥٠ سؤالاً",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { saveHtmlLauncher.launch("oyoun-masr-quiz-index.html") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ChampionshipGold,
                                    contentColor = RoyalNavyDark
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("download_html_file_button")
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تنزيل index.html", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(standaloneHtml))
                                    statusBanner = "تم نسخ كود موقع الويب الكامل (HTML/CSS/JS) إلى الحافظة! الصقه في ملف index.html وانشره فوراً."
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CorrectEmerald,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("copy_html_code_button")
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("نسخ كود الموقع", fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "موقع مسابقة عباقرة عيون مصر (index.html)")
                                    putExtra(Intent.EXTRA_TEXT, standaloneHtml)
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "مشاركة كود موقع الويب"))
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إرسال ومشاركة ملف الموقع للكمبيوتر", color = Color.White)
                        }

                        if (statusBanner != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = CorrectEmerald.copy(alpha = 0.22f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = statusBanner!!,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ChampionshipGold,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Mode Switcher: Interactive Web Preview vs Source Code + Hosting Guide
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = viewMode == "PREVIEW",
                        onClick = { viewMode = "PREVIEW" },
                        label = { Text("معاينة موقع الويب التفاعلي مباشرة") },
                        leadingIcon = {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = viewMode == "CODE",
                        onClick = { viewMode = "CODE" },
                        label = { Text("خطوات النشر والكود المصدري") },
                        leadingIcon = {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (viewMode == "PREVIEW") {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(640.dp)
                            .testTag("live_web_preview_card"),
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    webViewClient = WebViewClient()
                                    loadDataWithBaseURL(
                                        "https://oyounmasr.edu.eg/",
                                        standaloneHtml,
                                        "text/html",
                                        "UTF-8",
                                        null
                                    )
                                }
                            },
                            update = { webView ->
                                // Keep loaded unless needed
                            }
                        )
                    }
                }
            } else {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "كيف تنشر موقع «عباقرة عيون مصر» على الإنترنت مجاناً في دقيقة واحدة؟",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Text(
                                text = "١. اضغط على زر «تنزيل index.html» أعلاه (أو انسخ الكود واحفظه في ملف باسم index.html).\n" +
                                    "٢. افتح موقع Netlify Drop (app.netlify.com/drop) أو GitHub Pages أو Vercel أو سيرفر المدرسة.\n" +
                                    "٣. اسحب ملف index.html وأفلته ليصبح الموقع متاحاً برابط مباشر لجميع طلاب الصفوف الرابع والخامس والسادس من الهاتف أو الكمبيوتر!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "معاينة الكود المصدري للموقع (index.html - ${standaloneHtml.length} حرف):",
                                style = MaterialTheme.typography.labelLarge,
                                color = ChampionshipGold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = standaloneHtml.take(2400) + "\n\n... [اضغط على «نسخ كود الموقع» أو «تنزيل index.html» للحصول على الملف الكامل]",
                                fontFamily = FontFamily.Monospace,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }
        }
    }
}
