<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true"%>
<%@ page import="java.util.List, model_dto.StudyPlan"%>
<% List<StudyPlan> planList = (List<StudyPlan>) request.getAttribute("planList"); %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学習カレンダー | DailyKnowly</title>
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
    <style>
        body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #f0f2f5; margin: 0; padding: 20px; color: #333; }
        .container { max-width: 1100px; margin: 0 auto; display: flex; gap: 20px; }
        @media (max-width: 768px) { .container { flex-direction: column; } }

        /* カレンダー部分 */
        #calendar-container { flex: 3; background: white; padding: 20px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        
        /* サイドバー */
        #sidebar { flex: 1; background: white; padding: 20px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); align-self: flex-start; }
        
        .back-link { display: inline-block; margin-bottom: 20px; color: #7f8c8d; text-decoration: none; font-size: 14px; transition: 0.3s; }
        .back-link:hover { color: #3498db; }
        
        h3 { margin-top: 0; font-size: 18px; color: #2c3e50; border-bottom: 2px solid #f0f2f5; padding-bottom: 10px; }
        
        .btn-new { display: block; text-align: center; padding: 10px; background: #3498db; color: white; text-decoration: none; border-radius: 8px; font-weight: bold; transition: 0.3s; margin-bottom: 15px; }
        .btn-new:hover { background: #2980b9; transform: translateY(-2px); }

        .plan-item { background: #f8f9fa; border-left: 4px solid #3498db; padding: 10px; border-radius: 4px; margin-bottom: 10px; font-size: 14px; }
        .plan-actions { margin-top: 5px; font-size: 12px; }
        .plan-actions a { text-decoration: none; color: #3498db; margin-right: 10px; }
        .plan-actions a.delete { color: #e74c3c; }

        /* FullCalendar カスタム */
        .fc-day-sat { background-color: #eaf4ff !important; }
        .fc-day-sun { background-color: #fff0f0 !important; }
        .fc-toolbar-title { font-size: 1.2em !important; color: #2c3e50; }
        .fc-button-primary { background-color: #3498db !important; border: none !important; }
    </style>
</head>
<body>

<div class="container">
    <div id="calendar-container">
        <div id="calendar"></div>
    </div>

    <div id="sidebar">
        <a href="HomeServlet" class="back-link">← ホームへ戻る</a>
        <h3 id="view-date">日付を選択</h3>
        <div id="day-plans">
            <p style="color: #95a5a6; font-size: 14px;">カレンダーの日付をクリックすると予定が表示されます。</p>
        </div>
    </div>
</div>

<script>
    const allPlans = [
        <% if (planList != null) { for (StudyPlan p : planList) { %>
        {
            id: <%= p.getId() %>,
            title: '<%= p.getTitle() %>',
            start: '<%= p.getStartDate() %>',
            end: '<%= p.getEndDate() %>',
            color: '<%= p.getColor() %>',
            dateOnly: '<%= p.getStartDate().toString().substring(0, 10) %>'
        },
        <% } } %>
    ];

    document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ja',
            height: 'auto',
            events: allPlans,
            dateClick: function(info) {
                const clickedDate = info.dateStr;
                document.getElementById('view-date').innerText = clickedDate;
                const listEl = document.getElementById('day-plans');
                
                let html = '<a href="PlanFormServlet?date=' + clickedDate + '" class="btn-new">＋ 新規作成</a>';
                html += '<h4>予定一覧</h4>';

                const dayPlans = allPlans.filter(p => p.dateOnly === clickedDate);
                
                if (dayPlans.length === 0) {
                    html += '<p style="color: #95a5a6; font-size: 14px;">予定はありません</p>';
                } else {
                    dayPlans.forEach(p => {
                        html += '<div class="plan-item" style="border-left-color:' + p.color + '">';
                        html += '<strong>' + p.title + '</strong>';
                        html += '<div class="plan-actions">';
                        html += '<a href="PlanFormServlet?id=' + p.id + '">編集</a>';
                        html += '<a href="PlanDeleteServlet?id=' + p.id + '" class="delete">削除</a>';
                        html += '</div></div>';
                    });
                }
                listEl.innerHTML = html;
            }
        });
        calendar.render();
    });
</script>
</body>
</html>