package com.example.data

object WebAppExporter {

    fun generateStandaloneHtml(
        questions: List<QuestionEntity>,
        scores: List<ScoreRecordEntity>,
        registrations: List<StudentRegistrationEntity>
    ): String {
        val questionsJson = buildString {
            append("[\n")
            questions.forEachIndexed { idx, q ->
                val cat = QuizCategory.fromId(q.categoryId)
                append("  {")
                append("\"id\":${q.id},")
                append("\"categoryId\":\"${escapeJs(q.categoryId)}\",")
                append("\"categoryTitle\":\"${escapeJs(cat.titleAr)}\",")
                append("\"emoji\":\"${escapeJs(cat.emoji)}\",")
                append("\"grade\":\"${escapeJs(q.gradeLevel)}\",")
                append("\"difficulty\":${q.difficulty},")
                append("\"q\":\"${escapeJs(q.questionText)}\",")
                append("\"clue\":\"${escapeJs(q.visualClueText)}\",")
                append("\"opts\":[\"${escapeJs(q.optionA)}\",\"${escapeJs(q.optionB)}\",\"${escapeJs(q.optionC)}\",\"${escapeJs(q.optionD)}\"],")
                append("\"ans\":${q.correctOptionIndex},")
                append("\"exp\":\"${escapeJs(q.explanation)}\"")
                append("}")
                if (idx < questions.lastIndex) append(",")
                append("\n")
            }
            append("]")
        }

        val initialScoresJson = buildString {
            append("[\n")
            scores.take(15).forEachIndexed { idx, s ->
                append("  {")
                append("\"name\":\"${escapeJs(s.playerOrWinnerName)}\",")
                append("\"grade\":\"${escapeJs(s.gradeOrClassroom)}\",")
                append("\"code\":\"${escapeJs(s.participationCode)}\",")
                append("\"score\":${s.score},")
                append("\"total\":${s.totalPossibleScore},")
                append("\"badge\":\"${escapeJs(s.badgeTitle)}\",")
                append("\"category\":\"${escapeJs(s.categoryTitle)}\"")
                append("}")
                if (idx < minOf(scores.lastIndex, 14)) append(",")
                append("\n")
            }
            append("]")
        }

        val initialRegsJson = buildString {
            append("[\n")
            registrations.take(20).forEachIndexed { idx, r ->
                append("  {")
                append("\"name\":\"${escapeJs(r.studentName)}\",")
                append("\"grade\":\"${escapeJs(r.gradeLevel)}\",")
                append("\"classroom\":\"${escapeJs(r.classroom)}\",")
                append("\"studentId\":\"${escapeJs(r.studentNumberOrId)}\",")
                append("\"team\":\"${escapeJs(r.teamName)}\",")
                append("\"code\":\"${escapeJs(r.participationCode)}\"")
                append("}")
                if (idx < minOf(registrations.lastIndex, 19)) append(",")
                append("\n")
            }
            append("]")
        }

        return """
<!DOCTYPE html>
<html lang="ar" dir="rtl">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>🏆 عباقرة عيون مصر | المنصة الرسمية للمسابقة</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@500;700;800&family=Tajawal:wght@400;500;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --navy: #0B132B;
      --navy-card: #1C2541;
      --blue: #1D4ED8;
      --sky: #0284C7;
      --gold: #FFB703;
      --gold-light: #FEF3C7;
      --emerald: #10B981;
      --coral: #EF4444;
      --bg: #F1F5F9;
      --surface: #FFFFFF;
      --text: #0F172A;
      --subtext: #475569;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      font-family: 'Tajawal', 'Cairo', sans-serif;
      background: var(--bg);
      color: var(--text);
      line-height: 1.6;
      min-height: 100vh;
    }
    h1, h2, h3, h4, .btn, .badge, .nav-tab {
      font-family: 'Cairo', sans-serif;
    }
    header.site-header {
      background: linear-gradient(135deg, var(--navy) 0%, var(--navy-card) 60%, var(--blue) 100%);
      color: #fff;
      padding: 16px 24px;
      position: sticky;
      top: 0;
      z-index: 100;
      box-shadow: 0 4px 20px rgba(11,19,43,0.22);
    }
    .header-inner {
      max-width: 1120px;
      margin: 0 auto;
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
    }
    .brand {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 1.35rem;
      font-weight: 800;
      color: var(--gold);
      cursor: pointer;
    }
    .nav-links {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }
    .nav-tab {
      background: rgba(255,255,255,0.12);
      color: #fff;
      border: 1px solid rgba(255,255,255,0.2);
      padding: 8px 15px;
      border-radius: 999px;
      font-size: 0.92rem;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.2s;
    }
    .nav-tab:hover, .nav-tab.active {
      background: var(--gold);
      color: var(--navy);
      border-color: var(--gold);
    }
    .container {
      max-width: 1080px;
      margin: 24px auto;
      padding: 0 16px 64px;
    }
    .hero-card {
      background: linear-gradient(135deg, #0284C7 0%, #1D4ED8 55%, #312E81 100%);
      color: #fff;
      border-radius: 28px;
      padding: 32px 26px;
      text-align: center;
      box-shadow: 0 12px 32px rgba(29,78,216,0.25);
      margin-bottom: 24px;
      position: relative;
      overflow: hidden;
    }
    .hero-slogan {
      display: inline-block;
      background: var(--gold-light);
      color: #92400E;
      font-weight: 800;
      padding: 8px 20px;
      border-radius: 999px;
      margin: 12px 0;
      font-size: 1.05rem;
    }
    .icons-row {
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      gap: 10px;
      margin: 16px 0;
    }
    .icon-pill {
      background: rgba(255,255,255,0.18);
      border: 1px solid rgba(255,255,255,0.35);
      padding: 6px 14px;
      border-radius: 999px;
      font-size: 0.9rem;
      font-weight: 700;
    }
    .btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      border: none;
      border-radius: 16px;
      padding: 13px 24px;
      font-size: 1rem;
      font-weight: 800;
      cursor: pointer;
      transition: transform 0.15s, box-shadow 0.15s;
    }
    .btn:active { transform: scale(0.98); }
    .btn-gold {
      background: var(--gold);
      color: var(--navy);
      box-shadow: 0 6px 18px rgba(255,183,3,0.35);
    }
    .btn-emerald {
      background: var(--emerald);
      color: #fff;
    }
    .btn-primary {
      background: var(--blue);
      color: #fff;
    }
    .btn-outline {
      background: rgba(255,255,255,0.15);
      color: #fff;
      border: 1.5px solid rgba(255,255,255,0.45);
    }
    .grid-2 {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 16px;
    }
    .grid-3 {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 14px;
    }
    .card {
      background: var(--surface);
      border-radius: 22px;
      padding: 22px;
      box-shadow: 0 4px 16px rgba(15,23,42,0.06);
      border: 1px solid #E2E8F0;
      margin-bottom: 16px;
    }
    .card-dark {
      background: var(--navy-card);
      color: #fff;
      border: none;
    }
    .badge {
      display: inline-block;
      padding: 4px 12px;
      border-radius: 999px;
      font-size: 0.82rem;
      font-weight: 700;
    }
    .form-group {
      margin-bottom: 14px;
      text-align: right;
    }
    .form-group label {
      display: block;
      font-weight: 700;
      margin-bottom: 6px;
      font-size: 0.95rem;
    }
    .form-control {
      width: 100%;
      padding: 12px 14px;
      border-radius: 12px;
      border: 1.5px solid #CBD5E1;
      font-family: inherit;
      font-size: 1rem;
    }
    .ticket-box {
      background: linear-gradient(135deg, var(--navy) 0%, #1E3A8A 100%);
      color: #fff;
      padding: 22px;
      border-radius: 22px;
      margin-bottom: 20px;
      border: 2px solid var(--gold);
    }
    .q-nav-strip {
      display: flex;
      gap: 6px;
      overflow-x: auto;
      padding: 8px 4px;
      margin-bottom: 14px;
    }
    .q-dot {
      min-width: 38px;
      height: 38px;
      border-radius: 50%;
      border: none;
      font-weight: 800;
      cursor: pointer;
      background: #E2E8F0;
      color: var(--text);
    }
    .q-dot.current { background: var(--blue); color: #fff; }
    .q-dot.answered { background: var(--emerald); color: #fff; }
    .option-btn {
      width: 100%;
      text-align: right;
      padding: 15px 18px;
      border-radius: 16px;
      border: 2px solid #E2E8F0;
      background: #fff;
      font-size: 1.05rem;
      font-weight: 700;
      cursor: pointer;
      margin-bottom: 10px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      transition: all 0.15s;
    }
    .option-btn:hover { border-color: var(--blue); background: #F8FAFC; }
    .option-btn.selected { border-color: var(--blue); background: #DBEAFE; color: #1E3A8A; }
    .option-btn.correct { border-color: var(--emerald); background: #D1FAE5; color: #065F46; }
    .option-btn.wrong { border-color: var(--coral); background: #FEE2E2; color: #991B1B; }
    .visual-clue {
      background: #FFFBEB;
      border: 2px dashed var(--gold);
      color: var(--navy);
      padding: 14px;
      border-radius: 14px;
      font-weight: 800;
      text-align: center;
      margin: 12px 0;
      font-size: 1.15rem;
    }
    .timer-pill {
      background: var(--gold);
      color: var(--navy);
      padding: 6px 16px;
      border-radius: 999px;
      font-weight: 800;
      font-size: 1.1rem;
    }
    .hidden { display: none !important; }
  </style>
</head>
<body>

<header class="site-header">
  <div class="header-inner">
    <div class="brand" onclick="showView('home')">
      <span>🏆 عباقرة عيون مصر</span>
    </div>
    <nav class="nav-links">
      <button class="nav-tab active" id="tab-home" onclick="showView('home')">🚀 الرئيسية</button>
      <button class="nav-tab" id="tab-register" onclick="showView('register')">🎟️ التسجيل والكود</button>
      <button class="nav-tab" id="tab-qualifier" onclick="startQualifierExam()">🌐 التصفيات (50 سؤالاً)</button>
      <button class="nav-tab" id="tab-teams" onclick="showView('teams')">🏫 نهائيات الفرق</button>
      <button class="nav-tab" id="tab-guide" onclick="showView('guide')">📖 مراحل البطولة</button>
      <button class="nav-tab" id="tab-results" onclick="showView('results')">📊 النتائج</button>
    </nav>
  </div>
</header>

<main class="container">
  <!-- 1. HOME VIEW -->
  <section id="view-home">
    <div class="hero-card">
      <span class="badge" style="background:var(--gold);color:var(--navy);">مدرسة عيون مصر • الصفوف ٤ و ٥ و ٦ الابتدائي</span>
      <h1 style="font-size:2.4rem;margin-top:10px;">🏆 عباقرة عيون مصر</h1>
      <div class="hero-slogan">«فكّر… أسرع. اعرف… أكثر. العب… كفريق!»</div>
      <p style="max-width:700px;margin:8px auto 16px;color:#E2E8F0;">
        مسابقة الذكاء والمعرفة وسرعة البديهة والتفكير المنطقي لطلاب المرحلة الابتدائية العليا بمدرسة عيون مصر.
      </p>
      <div class="icons-row">
        <span class="icon-pill">💡 مصباح فكرة</span>
        <span class="icon-pill">❓ علامة استفهام</span>
        <span class="icon-pill">🔢 أرقام</span>
        <span class="icon-pill">📚 كتب</span>
        <span class="icon-pill">🪐 كواكب</span>
        <span class="icon-pill">🧩 قطع ألغاز</span>
        <span class="icon-pill">⭐ نجمة البطولة</span>
      </div>
      <div style="display:flex;flex-wrap:wrap;justify-content:center;gap:10px;margin-top:20px;">
        <button class="btn btn-gold" onclick="startQualifierExam()">🚀 ابدأ المسابقة (التصفيات 50 سؤالاً)</button>
        <button class="btn btn-outline" onclick="showView('register')">🎟️ تسجيل طالب وإصدار كود</button>
        <button class="btn btn-outline" onclick="showView('guide')">📖 كيف تلعب؟ ومراحل البطولة</button>
        <button class="btn btn-outline" onclick="showView('results')">📊 النتائج ولوحة الشرف</button>
      </div>
    </div>

    <div id="active-student-banner"></div>

    <h2 style="margin-bottom:14px;">المجالات الثمانية للمسابقة (اختر أي مجال للتدريب الفردي السريع)</h2>
    <div class="grid-3" id="categories-grid"></div>
  </section>

  <!-- 2. REGISTRATION VIEW -->
  <section id="view-register" class="hidden">
    <div id="ticket-container"></div>
    <div class="card">
      <h2>🎟️ تسجيل بيانات الطالب في المسابقة</h2>
      <p style="color:var(--subtext);margin-bottom:16px;">سجّل بياناتك للحصول على كود مشاركة فريد للدخول إلى التصفيات الأونلاين.</p>
      <div class="grid-2">
        <div class="form-group">
          <label>اسم الطالب ثلاثياً *</label>
          <input type="text" id="reg-name" class="form-control" placeholder="مثال: عمر أحمد محمود" />
        </div>
        <div class="form-group">
          <label>الصف الدراسي *</label>
          <select id="reg-grade" class="form-control">
            <option value="الصف الرابع الابتدائي">الصف الرابع الابتدائي</option>
            <option value="الصف الخامس الابتدائي" selected>الصف الخامس الابتدائي</option>
            <option value="الصف السادس الابتدائي">الصف السادس الابتدائي</option>
          </select>
        </div>
        <div class="form-group">
          <label>الفصل *</label>
          <input type="text" id="reg-class" class="form-control" value="٥/أ" />
        </div>
        <div class="form-group">
          <label>رقم الطالب أو الكود المدرسي</label>
          <input type="text" id="reg-num" class="form-control" placeholder="مثال: 1045" />
        </div>
        <div class="form-group">
          <label>اسم الفريق (اختياري)</label>
          <input type="text" id="reg-team" class="form-control" placeholder="مثال: صقور عيون مصر" />
        </div>
        <div class="form-group">
          <label>رقم تواصل ولي الأمر (اختياري)</label>
          <input type="text" id="reg-phone" class="form-control" placeholder="اختياري حسب نظام المدرسة" />
        </div>
      </div>
      <button class="btn btn-primary" style="width:100%;margin-top:8px;" onclick="registerStudentWeb()">حفظ البيانات وإصدار 🎟️ كود المشاركة</button>
    </div>

    <div class="card">
      <h3>لديك كود مشاركة محفوظ؟ أدخله هنا:</h3>
      <div style="display:flex;gap:10px;margin-top:10px;">
        <input type="text" id="lookup-code" class="form-control" placeholder="مثال: OM-5-4821" />
        <button class="btn btn-gold" onclick="lookupByCodeWeb()">تفعيل الكود</button>
      </div>
      <p id="lookup-msg" style="margin-top:8px;font-weight:700;color:var(--blue);"></p>
    </div>
  </section>

  <!-- 3. ONLINE QUALIFIER EXAM (50 QUESTIONS / 30 MIN) -->
  <section id="view-qualifier" class="hidden">
    <div class="card card-dark" style="display:flex;flex-wrap:wrap;justify-content:space-between;align-items:center;gap:12px;">
      <div>
        <h3 style="color:var(--gold);">🌐 اختبار التصفيات الأونلاين (50 سؤالاً)</h3>
        <div id="qualifier-student-label" style="font-size:0.9rem;color:#CBD5E1;"></div>
      </div>
      <div style="display:flex;align-items:center;gap:12px;">
        <span id="qualifier-progress-label" style="font-weight:700;"></span>
        <span class="timer-pill" id="qualifier-timer">30:00</span>
      </div>
    </div>

    <div class="q-nav-strip" id="qualifier-nav-strip"></div>

    <div class="card" id="qualifier-question-card"></div>
  </section>

  <!-- 4. STAGE 2 TEAM BATTLE VIEW -->
  <section id="view-teams" class="hidden">
    <div class="card card-dark">
      <h2 style="color:var(--gold);">🏫 المرحلة الثانية: البطولة النهائية للفرق داخل المدرسة</h2>
      <p style="color:#CBD5E1;">منافسة مباشرة بالتناوب بين فريقين أو فصلين دراسيين.</p>
    </div>
    <div class="card" id="team-setup-box">
      <div class="grid-2">
        <div class="form-group">
          <label>اسم الفريق الأول</label>
          <input type="text" id="t1-name" class="form-control" value="نسور عيون مصر (٥/أ)" />
        </div>
        <div class="form-group">
          <label>اسم الفريق الثاني</label>
          <input type="text" id="t2-name" class="form-control" value="رواد المستقبل (٦/أ)" />
        </div>
      </div>
      <button class="btn btn-gold" style="width:100%;" onclick="startTeamMatchWeb()">انطلاق مواجهة الفريقين (8 أسئلة بالتناوب)</button>
    </div>
    <div id="team-arena-box" class="hidden"></div>
  </section>

  <!-- 5. GUIDE & STAGES VIEW -->
  <section id="view-guide" class="hidden">
    <div class="grid-2">
      <div class="card">
        <span class="badge" style="background:#E0F2FE;color:#0284C7;">المرحلة الأولى 🌐</span>
        <h3 style="margin:8px 0;">التصفيات الأونلاين</h3>
        <p>يدخل كل طالب من الهاتف أو الكمبيوتر بكود المشاركة لخوض اختبار فردي من 50 سؤالاً خلال 30 دقيقة، ويتم اختيار أفضل الطلاب للتأهل للنهائيات.</p>
      </div>
      <div class="card">
        <span class="badge" style="background:var(--gold-light);color:#92400E;">المرحلة الثانية 🏫</span>
        <h3 style="margin:8px 0;">البطولة النهائية داخل المدرسة</h3>
        <p>يتم تكوين فرق من الطلاب المتأهلين لخوض مواجهات مباشرة داخل مدرسة عيون مصر للفوز بكأس البطولة.</p>
      </div>
    </div>
    <div class="card">
      <h3>توزيع أسئلة اختبار التصفيات الأونلاين (50 سؤالاً)</h3>
      <div class="grid-3" style="margin-top:12px;">
        <div>🔬 <b>العلوم:</b> 10 أسئلة</div>
        <div>➗ <b>الرياضيات والذكاء الحسابي:</b> 8 أسئلة</div>
        <div>📚 <b>اللغة العربية:</b> 6 أسئلة</div>
        <div>🇪🇬 <b>مصر والعالم:</b> 6 أسئلة</div>
        <div>🧩 <b>المنطق والاستنتاج:</b> 8 أسئلة</div>
        <div>👀 <b>الملاحظة والتركيز:</b> 6 أسئلة</div>
        <div>🌍 <b>الثقافة العامة:</b> 4 أسئلة</div>
        <div>💻 <b>التكنولوجيا والابتكار:</b> 2 سؤال</div>
      </div>
    </div>
  </section>

  <!-- 6. RESULTS & LEADERBOARD VIEW -->
  <section id="view-results" class="hidden">
    <div class="card card-dark">
      <h2 style="color:var(--gold);">📊 لوحة شرف ونتائج عباقرة عيون مصر</h2>
      <p style="color:#CBD5E1;">ترتيب الأبطال والفرق حسب الدرجات والسرعة</p>
    </div>
    <div id="leaderboard-list"></div>
  </section>
</main>

<script>
  const QUESTIONS = $questionsJson;
  const INITIAL_SCORES = $initialScoresJson;
  const INITIAL_REGS = $initialRegsJson;

  const QUOTAS = {
    "SCIENCE": 10,
    "MATH_LOGIC": 8,
    "ARABIC": 6,
    "EGYPT_HISTORY": 6,
    "LOGIC_DEDUCTION": 8,
    "OBSERVATION_FOCUS": 6,
    "GENERAL": 4,
    "TECH_INNOVATION": 2
  };

  let registrations = JSON.parse(localStorage.getItem('om_regs') || 'null') || INITIAL_REGS;
  let scores = JSON.parse(localStorage.getItem('om_scores') || 'null') || INITIAL_SCORES;
  let activeStudent = JSON.parse(localStorage.getItem('om_active_student') || 'null') || (registrations[0] || null);

  let examQuestions = [];
  let examIndex = 0;
  let examAnswers = {};
  let examRemainingSec = 1800;
  let examTimerId = null;

  function showView(viewId) {
    ['home','register','qualifier','teams','guide','results'].forEach(v => {
      document.getElementById('view-' + v).classList.add('hidden');
      const tab = document.getElementById('tab-' + v);
      if (tab) tab.classList.remove('active');
    });
    document.getElementById('view-' + viewId).classList.remove('hidden');
    const activeTab = document.getElementById('tab-' + viewId);
    if (activeTab) activeTab.classList.add('active');
    renderActiveStudentTicket();
    if (viewId === 'results') renderLeaderboard();
  }

  function renderActiveStudentTicket() {
    const html = activeStudent ? `
      <div class="ticket-box">
        <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:8px;">
          <div>
            <span style="color:var(--gold);font-weight:800;">بطاقة المتسابق المسجل:</span>
            <h3 style="margin:4px 0;">${'$'}{activeStudent.name} (${'$'}{activeStudent.grade} - فصل ${'$'}{activeStudent.classroom})</h3>
          </div>
          <span class="timer-pill">🎟️ كود المشاركة: ${'$'}{activeStudent.code}</span>
        </div>
      </div>
    ` : '';
    document.getElementById('active-student-banner').innerHTML = html;
    document.getElementById('ticket-container').innerHTML = html;
  }

  function registerStudentWeb() {
    const name = document.getElementById('reg-name').value.trim();
    const grade = document.getElementById('reg-grade').value;
    const classroom = document.getElementById('reg-class').value.trim() || 'أ';
    const num = document.getElementById('reg-num').value.trim() || '100';
    const team = document.getElementById('reg-team').value.trim();
    if (name.length < 2) { alert('يرجى إدخال اسم الطالب ثلاثياً'); return; }
    const gDigit = grade.includes('الرابع') ? '4' : (grade.includes('الخامس') ? '5' : '6');
    const code = 'OM-' + gDigit + '-' + Math.floor(1000 + Math.random() * 9000);
    const st = { name, grade, classroom, studentId: num, team, code };
    registrations.unshift(st);
    activeStudent = st;
    localStorage.setItem('om_regs', JSON.stringify(registrations));
    localStorage.setItem('om_active_student', JSON.stringify(activeStudent));
    renderActiveStudentTicket();
    alert('تم تسجيل الطالب بنجاح! كود المشاركة الخاص بك هو: ' + code);
  }

  function lookupByCodeWeb() {
    const code = document.getElementById('lookup-code').value.trim().toUpperCase();
    const found = registrations.find(r => r.code.toUpperCase() === code);
    const msg = document.getElementById('lookup-msg');
    if (found) {
      activeStudent = found;
      localStorage.setItem('om_active_student', JSON.stringify(activeStudent));
      renderActiveStudentTicket();
      msg.textContent = 'مرحباً بعودتك يا ' + found.name + '!';
    } else {
      msg.textContent = 'لم يتم العثور على هذا الكود، تأكد من صحته أو سجّل كطالب جديد.';
    }
  }

  function shuffle(arr) {
    return [...arr].sort(() => Math.random() - 0.5);
  }

  function startQualifierExam(singleCategoryId) {
    clearInterval(examTimerId);
    examQuestions = [];
    if (singleCategoryId) {
      examQuestions = shuffle(QUESTIONS.filter(q => q.categoryId === singleCategoryId)).slice(0, 10);
      examRemainingSec = 600;
    } else {
      Object.keys(QUOTAS).forEach(catId => {
        const pool = shuffle(QUESTIONS.filter(q => q.categoryId === catId));
        examQuestions.push(...pool.slice(0, QUOTAS[catId]));
      });
      examRemainingSec = 1800;
    }
    examIndex = 0;
    examAnswers = {};
    showView('qualifier');
    renderQualifierState();
    examTimerId = setInterval(() => {
      examRemainingSec--;
      if (examRemainingSec <= 0) {
        clearInterval(examTimerId);
        submitQualifierWeb();
      } else {
        const m = String(Math.floor(examRemainingSec / 60)).padStart(2, '0');
        const s = String(examRemainingSec % 60).padStart(2, '0');
        document.getElementById('qualifier-timer').textContent = m + ':' + s;
      }
    }, 1000);
  }

  function renderQualifierState() {
    const stName = activeStudent ? (activeStudent.name + ' • 🎟️ ' + activeStudent.code) : 'متسابق عيون مصر';
    document.getElementById('qualifier-student-label').textContent = stName;
    const answeredCount = Object.keys(examAnswers).length;
    document.getElementById('qualifier-progress-label').textContent =
      'تمت الإجابة: ' + answeredCount + ' / ' + examQuestions.length;

    const strip = document.getElementById('qualifier-nav-strip');
    strip.innerHTML = examQuestions.map((_, i) => {
      const cls = i === examIndex ? 'q-dot current' : (examAnswers[i] !== undefined ? 'q-dot answered' : 'q-dot');
      return `<button class="${'$'}{cls}" onclick="jumpQuestion(${'$'}{i})">${'$'}{i + 1}</button>`;
    }).join('');

    const q = examQuestions[examIndex];
    const letters = ['أ', 'ب', 'ج', 'د'];
    const clueHtml = q.clue ? `<div class="visual-clue">👀 لوحة التركيز والملاحظة: ${'$'}{q.clue}</div>` : '';
    const optsHtml = q.opts.map((opt, idx) => {
      const sel = examAnswers[examIndex] === idx ? 'selected' : '';
      return `<button class="option-btn ${'$'}{sel}" onclick="pickQualifierAns(${'$'}{idx})">
        <span><b>(${'$'}{letters[idx]})</b> ${'$'}{opt}</span>
        <span>${'$'}{sel ? '✅' : ''}</span>
      </button>`;
    }).join('');

    document.getElementById('qualifier-question-card').innerHTML = `
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:10px;">
        <span class="badge" style="background:#DBEAFE;color:#1E3A8A;">${'$'}{q.emoji} ${'$'}{q.categoryTitle}</span>
        <span class="badge" style="background:#F1F5F9;color:#475569;">سؤال ${'$'}{examIndex + 1} من ${'$'}{examQuestions.length}</span>
      </div>
      <h2 style="margin-bottom:14px;">${'$'}{q.q}</h2>
      ${'$'}{clueHtml}
      <div style="margin-top:16px;">${'$'}{optsHtml}</div>
      <div style="display:flex;justify-content:space-between;gap:10px;margin-top:18px;">
        <button class="btn" style="background:#E2E8F0;" onclick="jumpQuestion(${'$'}{Math.max(0, examIndex - 1)})">السؤال السابق</button>
        ${'$'}{examIndex + 1 < examQuestions.length
          ? `<button class="btn btn-primary" onclick="jumpQuestion(${'$'}{examIndex + 1})">السؤال التالي</button>`
          : `<button class="btn btn-emerald" onclick="submitQualifierWeb()">إنهاء وتسليم الاختبار</button>`
        }
      </div>
      <button class="btn btn-gold" style="width:100%;margin-top:12px;" onclick="submitQualifierWeb()">تسليم الاختبار وعرض النتيجة النهائية (${'$'}{answeredCount}/${'$'}{examQuestions.length})</button>
    `;
  }

  function jumpQuestion(i) {
    examIndex = i;
    renderQualifierState();
  }

  function pickQualifierAns(optIdx) {
    examAnswers[examIndex] = optIdx;
    renderQualifierState();
  }

  function submitQualifierWeb() {
    clearInterval(examTimerId);
    let correct = 0;
    examQuestions.forEach((q, i) => {
      if (examAnswers[i] === q.ans) correct++;
    });
    const score = correct * 2;
    const total = examQuestions.length * 2;
    const ratio = correct / examQuestions.length;
    const badge = ratio >= 0.85 ? 'متأهل للنهائيات • وسام عبقري عيون مصر الذهبي 🏆' :
                  (ratio >= 0.7 ? 'متأهل للنهائيات • وسام التفوق الفضي 🥈' : 'وسام التميز والمشاركة 🥉');

    const record = {
      name: activeStudent ? activeStudent.name : 'بطل عيون مصر',
      grade: activeStudent ? (activeStudent.grade + ' (' + activeStudent.classroom + ')') : 'المرحلة الابتدائية العليا',
      code: activeStudent ? activeStudent.code : 'OM-WEB',
      score,
      total,
      badge,
      category: examQuestions.length === 50 ? 'التصفيات الأونلاين (50 سؤالاً)' : examQuestions[0].categoryTitle
    };
    scores.unshift(record);
    scores.sort((a, b) => b.score - a.score);
    localStorage.setItem('om_scores', JSON.stringify(scores));

    document.getElementById('qualifier-question-card').innerHTML = `
      <div style="text-align:center;padding:24px;">
        <div style="font-size:3.5rem;">🏆</div>
        <span class="badge" style="background:var(--gold);color:var(--navy);font-size:1rem;">${'$'}{badge}</span>
        <h2 style="margin:12px 0;">أحسنت يا ${'$'}{record.name}!</h2>
        <h1 style="color:var(--blue);font-size:2.6rem;">${'$'}{score} / ${'$'}{total} نقطة</h1>
        <p style="margin:8px 0 20px;">الإجابات الصحيحة: ${'$'}{correct} من ${'$'}{examQuestions.length} سؤالاً</p>
        <button class="btn btn-primary" onclick="showView('results')">عرض لوحة الشرف والنتائج</button>
      </div>
    `;
  }

  function startTeamMatchWeb() {
    const t1 = document.getElementById('t1-name').value.trim() || 'الفريق الأول';
    const t2 = document.getElementById('t2-name').value.trim() || 'الفريق الثاني';
    const matchQs = shuffle(QUESTIONS).slice(0, 8);
    let idx = 0, s1 = 0, s2 = 0;
    const box = document.getElementById('team-arena-box');
    box.classList.remove('hidden');

    function renderTurn() {
      if (idx >= matchQs.length) {
        const winner = s1 > s2 ? t1 : (s2 > s1 ? t2 : 'تعادل الأبطال');
        box.innerHTML = `<div class="card" style="text-align:center;">
          <h2>🏆 انتهت مواجهة الفرق! الفائز: ${'$'}{winner}</h2>
          <p style="font-size:1.2rem;margin:10px 0;">${'$'}{t1}: <b>${'$'}{s1}</b> نقطة | ${'$'}{t2}: <b>${'$'}{s2}</b> نقطة</p>
        </div>`;
        return;
      }
      const turnTeam = (idx % 2 === 0) ? t1 : t2;
      const q = matchQs[idx];
      box.innerHTML = `
        <div class="card">
          <div style="display:flex;justify-content:space-between;margin-bottom:12px;">
            <span class="badge" style="background:var(--gold);color:var(--navy);">دور الإجابة: ${'$'}{turnTeam}</span>
            <span><b>${'$'}{t1}:</b> ${'$'}{s1} | <b>${'$'}{t2}:</b> ${'$'}{s2}</span>
          </div>
          <h3>س${'$'}{idx + 1}: ${'$'}{q.q}</h3>
          <div style="margin-top:12px;">
            ${'$'}{q.opts.map((o, oi) => `<button class="option-btn" onclick="window._answerTeam(${'$'}{oi})">${'$'}{o}</button>`).join('')}
          </div>
        </div>
      `;
      window._answerTeam = (chosen) => {
        if (chosen === q.ans) {
          if (idx % 2 === 0) s1 += 20; else s2 += 20;
          alert('إجابة صحيحة! +20 نقطة. ' + q.exp);
        } else {
          alert('إجابة غير صحيحة! الإجابة الصحيحة هي: ' + q.opts[q.ans]);
        }
        idx++;
        renderTurn();
      };
    }
    renderTurn();
  }

  function renderCategoriesGrid() {
    const cats = [
      { id: 'SCIENCE', title: 'العلوم', emoji: '🔬', quota: 10 },
      { id: 'MATH_LOGIC', title: 'الرياضيات والذكاء الحسابي', emoji: '➗', quota: 8 },
      { id: 'ARABIC', title: 'اللغة العربية', emoji: '📚', quota: 6 },
      { id: 'EGYPT_HISTORY', title: 'مصر والعالم', emoji: '🇪🇬', quota: 6 },
      { id: 'LOGIC_DEDUCTION', title: 'المنطق والاستنتاج', emoji: '🧩', quota: 8 },
      { id: 'OBSERVATION_FOCUS', title: 'الملاحظة والتركيز', emoji: '👀', quota: 6 },
      { id: 'GENERAL', title: 'الثقافة العامة', emoji: '🌍', quota: 4 },
      { id: 'TECH_INNOVATION', title: 'التكنولوجيا والابتكار', emoji: '💻', quota: 2 }
    ];
    document.getElementById('categories-grid').innerHTML = cats.map(c => `
      <div class="card" style="cursor:pointer;" onclick="startQualifierExam('${'$'}{c.id}')">
        <div style="font-size:2rem;">${'$'}{c.emoji}</div>
        <h3 style="margin:6px 0;">${'$'}{c.title}</h3>
        <p style="color:var(--subtext);font-size:0.9rem;">حصة التصفيات الرسمية: ${'$'}{c.quota} أسئلة</p>
        <button class="btn btn-primary" style="margin-top:10px;width:100%;padding:8px;">تدريب في هذا المجال</button>
      </div>
    `).join('');
  }

  function renderLeaderboard() {
    document.getElementById('leaderboard-list').innerHTML = scores.map((s, i) => `
      <div class="card" style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:10px;">
        <div>
          <span class="badge" style="background:var(--gold);color:var(--navy);">#${'$'}{i + 1}</span>
          <strong style="font-size:1.15rem;margin-right:8px;">${'$'}{s.name}</strong>
          <div style="color:var(--subtext);font-size:0.9rem;">${'$'}{s.grade} • ${'$'}{s.category} • 🎟️ ${'$'}{s.code || ''}</div>
          <div style="color:var(--emerald);font-weight:700;font-size:0.88rem;">${'$'}{s.badge}</div>
        </div>
        <div class="timer-pill">${'$'}{s.score} نقطة</div>
      </div>
    `).join('');
  }

  renderCategoriesGrid();
  renderActiveStudentTicket();
  renderLeaderboard();
</script>
</body>
</html>
""".trimIndent()
    }

    private fun escapeJs(input: String): String {
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", " ")
            .replace("\r", "")
    }
}
