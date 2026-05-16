<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model_dto.User, java.util.List, model_dto.StudyPlan"%>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<StudyPlan> todayPlans = (List<StudyPlan>) request.getAttribute("todayPlans");
    List<String> labels = (List<String>) request.getAttribute("chartLabels");
    List<Integer> values = (List<Integer>) request.getAttribute("chartValues");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ホーム | DailyKnowly</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            background: white;
            padding: 15px 25px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .welcome-msg { font-size: 18px; font-weight: bold; color: #2c3e50; }
        .logout-btn { color: #e74c3c; text-decoration: none; font-size: 14px; border: 1px solid #e74c3c; padding: 5px 12px; border-radius: 20px; }
        
        .grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }
        @media (max-width: 768px) { .grid { grid-template-columns: 1fr; } }

        .card {
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
        }
        .card h3 { margin-top: 0; border-bottom: 2px solid #f0f2f5; padding-bottom: 10px; font-size: 16px; color: #7f8c8d; }
        
        /* 予定リスト */
        .plan-list { list-style: none; padding: 0; }
        .plan-item { padding: 10px; border-bottom: 1px solid #f9f9f9; display: flex; align-items: center; }
        .plan-item::before { content: "•"; color: #3498db; font-weight: bold; margin-right: 10px; }

        /* ナビゲーションボタン */
        .nav-menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 15px;
            margin-top: 20px;
        }
        .nav-item {
            background: #3498db;
            color: white;
            text-align: center;
            padding: 15px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            transition: 0.3s;
        }
        .nav-item:hover { background: #2980b9; transform: translateY(-2px); }
        .nav-item.timer { background: #e67e22; }

        /* 通知メッセージ */
        .alert {
            background-color: #d1edff;
            color: #0c5460;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 5px solid #3498db;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <div class="welcome-msg">👋 ようこそ、${currentUser.userName}さん</div>
        <a href="LogoutServlet" class="logout-btn">ログアウト</a>
    </div>

    <% if (session.getAttribute("msg") != null) { %>
        <div class="alert"><%= session.getAttribute("msg") %></div>
        <% session.removeAttribute("msg"); %>
    <% } %>

    <div class="grid">
        <div class="card">
            <h3>📈 学習時間の推移</h3>
            <canvas id="myChart"></canvas>
        </div>

        <div class="card">
            <h3>📅 今日の予定</h3>
            <% if (todayPlans == null || todayPlans.isEmpty()) { %>
                <p style="color: #95a5a6; text-align: center; margin-top: 20px;">今日の予定はありません。</p>
            <% } else { %>
                <ul class="plan-list">
                    <% for(StudyPlan p : todayPlans) { %>
                        <li class="plan-item"><%= p.getTitle() %></li>
                    <% } %>
                </ul>
            <% } %>
        </div>
    </div>

    <div class="nav-menu">
        <a href="StudyServlet" class="nav-item">学習開始</a>
        <a href="CardAddServlet" class="nav-item">カード作成</a>
        <a href="CalendarServlet" class="nav-item">カレンダー</a>
        <a href="StudyTimerServlet" class="nav-item timer">集中タイマー</a>
    </div>
</div>

<script>
    const labels = [<% for(int i=0; i<labels.size(); i++) { %>"<%= labels.get(i) %>"<%= (i < labels.size()-1) ? "," : "" %><% } %>];
    const dataValues = [<% for(int i=0; i<values.size(); i++) { %><%= values.get(i) %><%= (i < values.size()-1) ? "," : "" %><% } %>];

    const ctx = document.getElementById('myChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar', // 棒グラフの方が学習量の比較がしやすいので変更
        data: {
            labels: labels,
            datasets: [{
                label: '学習時間 (分)',
                data: dataValues,
                backgroundColor: 'rgba(52, 152, 219, 0.6)',
                borderColor: 'rgba(52, 152, 219, 1)',
                borderWidth: 1,
                borderRadius: 5
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } },
            plugins: { legend: { display: false } } // ラベルが重複するので凡例は非表示
        }
    });
</script>

</body>
</html>