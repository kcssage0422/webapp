<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model_dto.Flashcard" %>
<%
    // 上のほうで一度だけ計算する
    int score = (int) session.getAttribute("score");
    List<Flashcard> mockList = (List<Flashcard>) session.getAttribute("mockList");
    int total = (mockList != null) ? mockList.size() : 0;
    
    // 正解率の計算（ここで一度だけ定義）
    int percentage = (total > 0) ? (int)((double)score / total * 100) : 0;
    
    // メッセージの出し分け
    String message = "";
    if (percentage == 100) message = "完璧です！素晴らしい！";
    else if (percentage >= 80) message = "あと少しで満点です！";
    else if (percentage >= 50) message = "よく頑張りました！";
    else message = "また復習して挑戦しましょう！";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>試験結果</title>
<style>
    body { font-family: 'Helvetica Neue', Arial, sans-serif; background: #f4f7f6; display: flex; justify-content: center; padding-top: 60px; }
    .result-card { background: white; padding: 40px; border-radius: 15px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 350px; text-align: center; }
    h1 { color: #2c3e50; }
    .score-big { font-size: 48px; font-weight: bold; color: #3498db; margin: 20px 0; }
    .message { font-size: 18px; color: #555; margin-bottom: 30px; }
    .btn-home { background-color: #2c3e50; color: white; padding: 15px 30px; border-radius: 8px; text-decoration: none; display: inline-block; transition: 0.3s; }
    .btn-home:hover { background-color: #34495e; }
    .btn-retry { 
        background-color: #e67e22; 
        color: white; 
        padding: 15px 30px; 
        border-radius: 8px; 
        text-decoration: none; 
        transition: 0.3s; 
    }
    .btn-retry:hover { 
        background-color: #d35400; 
    }
</style>
</head>
<body>

<div class="result-card">
    <h1>試験結果</h1>
    <div class="score-big"><%= score %> / <%= total %></div>
    
    <div class="score-big"><%= percentage %>点</div>
    
    <p class="message"><%= message %></p>
    
    <div style="display: flex; flex-direction: column; gap: 15px;">
        <a href="MockExamServlet?subjectId=<%= session.getAttribute("currentSubjectId") %>" class="btn-retry">もう一度挑戦する</a>
        <a href="HomeServlet" class="btn-home">ホームに戻る</a>
    </div>
</div>

</body>
</html>