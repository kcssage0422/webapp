<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model_dto.StudyPlan" %>
<%
    StudyPlan plan = (StudyPlan)request.getAttribute("plan");
    String mode = (String)request.getAttribute("mode");
    String start = plan.getStartDate().toString().substring(0, 16).replace(" ", "T");
    String end = plan.getEndDate().toString().substring(0, 16).replace(" ", "T");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>計画の<%= mode %> | DailyKnowly</title>
    <style>
        body { font-family: sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; padding: 40px 20px; }
        .form-card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); width: 100%; max-width: 500px; }
        h2 { color: #2c3e50; margin-bottom: 25px; text-align: center; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #7f8c8d; font-size: 14px; }
        input, textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; font-size: 16px; }
        .btn-submit { width: 100%; padding: 14px; background: #3498db; color: white; border: none; border-radius: 8px; font-weight: bold; cursor: pointer; margin-top: 10px; }
        .back-btn { display: block; text-align: center; margin-top: 20px; color: #95a5a6; text-decoration: none; }
    </style>
</head>
<body>
<div class="form-card">
    <h2>計画の<%= mode %></h2>
    <form action="PlanActionServlet" method="post">
        <input type="hidden" name="id" value="<%= plan.getId() %>">
        <div class="form-group">
            <label>タイトル</label>
            <input type="text" name="title" value="<%= plan.getTitle() == null ? "" : plan.getTitle() %>" required>
        </div>
        <div class="form-group">
            <label>内容（詳細）</label>
            <textarea name="description" rows="3"><%= plan.getDescription() == null ? "" : plan.getDescription() %></textarea>
        </div>
        <div class="form-group">
            <label>開始日時</label>
            <input type="datetime-local" name="startDate" value="<%= start %>" required>
        </div>
        <div class="form-group">
            <label>終了日時</label>
            <input type="datetime-local" name="endDate" value="<%= end %>" required>
        </div>
        <div class="form-group">
            <label>テーマカラー</label>
            <input type="color" name="color" value="<%= plan.getColor() %>" style="height: 50px; padding: 5px;">
        </div>
        <button type="submit" class="btn-submit">保存する</button>
    </form>
    <a href="CalendarServlet" class="back-btn">キャンセル</a>
</div>
</body>
</html>