<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true"%>
<%@ page import="model_dto.StudyPlan"%>
<% StudyPlan plan = (StudyPlan) request.getAttribute("plan"); %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>削除確認</title>
    <style>
        body { font-family: sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .confirm-card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 350px; text-align: center; }
        h2 { color: #e74c3c; margin-bottom: 15px; }
        .plan-info { background: #fff5f5; padding: 15px; border-radius: 8px; border-left: 5px solid <%=plan.getColor()%>; text-align: left; margin-bottom: 25px; }
        .btn-delete { width: 100%; padding: 12px; background: #e74c3c; color: white; border: none; border-radius: 6px; font-weight: bold; cursor: pointer; margin-bottom: 10px; }
        .btn-cancel { display: block; color: #95a5a6; text-decoration: none; font-size: 14px; }
    </style>
</head>
<body>
    <div class="confirm-card">
        <h2>予定を削除しますか？</h2>
        <p style="color: #7f8c8d; font-size: 14px; margin-bottom: 20px;">この操作は取り消せません。</p>
        <div class="plan-info">
            <strong><%=plan.getTitle()%></strong><br>
            <small><%=plan.getStartDate()%> ～</small>
        </div>
        <form action="PlanActionServlet" method="post">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="<%=plan.getId()%>">
            <button type="submit" class="btn-delete">削除を実行する</button>
            <a href="CalendarServlet" class="btn-cancel">キャンセル</a>
        </form>
    </div>
</body>
</html>