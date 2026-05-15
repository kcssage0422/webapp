<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model_dto.Flashcard" %>
<%
    List<Flashcard> list = (List<Flashcard>)request.getAttribute("flashCardList");
    Flashcard card = list.get(0);
    int total = list.size();
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学習中 | 学習管理アプリ</title>
    <style>
        body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .study-container { width: 100%; max-width: 500px; text-align: center; padding: 20px; }
        .progress-info { color: #7f8c8d; margin-bottom: 15px; font-weight: bold; }
        .card-box { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); min-height: 300px; display: flex; flex-direction: column; justify-content: center; position: relative; }
        h2 { font-size: 14px; color: #3498db; text-transform: uppercase; letter-spacing: 2px; margin-bottom: 20px; }
        .question-text { font-size: 24px; color: #2c3e50; margin-bottom: 30px; font-weight: bold; }
        .answer-text { font-size: 24px; color: #e67e22; margin-top: 20px; padding-top: 20px; border-top: 2px dashed #f0f2f5; }
        .btn-show { background: #34495e; color: white; border: none; padding: 15px 30px; border-radius: 30px; font-size: 16px; cursor: pointer; transition: 0.3s; }
        .btn-show:hover { background: #2c3e50; }
        .controls { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-top: 30px; }
        .btn-action { border: none; padding: 15px; border-radius: 12px; font-weight: bold; cursor: pointer; color: white; transition: 0.3s; }
        .btn-correct { background-color: #2ecc71; }
        .btn-wrong { background-color: #e74c3c; }
        .btn-action:hover { opacity: 0.9; transform: translateY(-2px); }
        .hidden { display: none; }
        .back-link { display: block; margin-top: 30px; color: #95a5a6; text-decoration: none; font-size: 14px; }
    </style>
</head>
<body>
<div class="study-container">
    <div class="progress-info">残り枚数: <%= total %> 枚</div>
    <div class="card-box">
        <h2>Question</h2>
        <div class="question-text"><%= card.getQuestion() %></div>
        
        <button type="button" id="show-answer-btn" class="btn-show" onclick="showAnswer()">答えを表示</button>

        <div id="answer-area" class="hidden">
            <h2>Answer</h2>
            <div class="answer-text"><%= card.getAnswer() %></div>
            <form action="StudyServlet" method="post" class="controls">
                <input type="hidden" name="cardId" value="<%= card.getCardId() %>">
                <input type="hidden" name="studyTime" id="studyTimeInput" value="0">
                <input type="hidden" name="subjectId" value="<%= card.getSubjectId() %>">
                <button type="submit" name="result" value="wrong" class="btn-action btn-wrong">忘れた...</button>
                <button type="submit" name="result" value="correct" class="btn-action btn-correct">覚えた！</button>
            </form>
        </div>
    </div>
    <a href="HomeServlet" class="back-link">中断してホームへ戻る</a>
</div>
<script>
    let startTime = Date.now();
    function showAnswer() {
        document.getElementById("answer-area").classList.remove("hidden");
        document.getElementById("show-answer-btn").classList.add("hidden");
        let endTime = Date.now();
        let seconds = Math.floor((endTime - startTime) / 1000);
        document.getElementById("studyTimeInput").value = seconds;
    }
</script>
</body>
