<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model_dto.Flashcard, java.util.List" %>
<%
    // セッションから現在データとインデックスを取得
    List<Flashcard> mockList = (List<Flashcard>) session.getAttribute("mockList");
    int index = (int) session.getAttribute("currentIndex");
    Flashcard card = mockList.get(index);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>模擬試験モード</title>
<style>
    body { font-family: 'Helvetica Neue', Arial, sans-serif; background: #f9f9f9; display: flex; justify-content: center; padding-top: 50px; }
    .card-container { background: white; padding: 40px; border-radius: 15px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 400px; text-align: center; }
    h2 { color: #7f8c8d; font-size: 14px; margin-bottom: 20px; }
    .question { font-size: 22px; font-weight: bold; color: #2c3e50; margin-bottom: 30px; }
    .answer-box { display: none; margin: 20px 0; padding: 20px; background: #f0f4f8; border-radius: 10px; }
    .answer-text { font-size: 18px; color: #34495e; margin-bottom: 20px; }
    
    button { padding: 12px 25px; border: none; border-radius: 8px; cursor: pointer; font-weight: bold; transition: 0.3s; }
    #showBtn { background-color: #34495e; color: white; width: 100%; }
    .btn-yes { background-color: #27ae60; color: white; margin-right: 10px; }
    .btn-no { background-color: #e74c3c; color: white; }
    button:hover { opacity: 0.9; transform: translateY(-2px); }
</style>
</head>
<body>

<div class="card-container">
    <h2>第 <%= index + 1 %> 問 / 全 <%= mockList.size() %> 問</h2>
    <div class="question">${card.question}</div>

    <button id="showBtn" onclick="showAnswer()">答えを見る</button>

    <div id="answerArea" class="answer-box">
        <div class="answer-text">${card.answer}</div>
        <p>この答えであっていましたか？</p>
        
        <form action="MockExamServlet" method="post" style="display:inline;">
            <input type="hidden" name="result" value="correct">
            <button type="submit" class="btn-yes">あっていた</button>
        </form>
        
        <form action="MockExamServlet" method="post" style="display:inline;">
            <input type="hidden" name="result" value="incorrect">
            <button type="submit" class="btn-no">間違っていた</button>
        </form>
    </div>
</div>

<script>
    function showAnswer() {
        document.getElementById('answerArea').style.display = 'block';
        document.getElementById('showBtn').style.display = 'none';
    }
</script>

</body>
</html>