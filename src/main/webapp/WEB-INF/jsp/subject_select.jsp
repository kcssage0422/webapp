<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model_dto.Subject, java.util.List" %>
<%
List<Subject> subjectList = (List<Subject>) request.getAttribute("subjectList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>科目選択 | DailyKnowly</title>
<style>
    body { font-family: sans-serif; background: #f4f7f6; display: flex; justify-content: center; padding-top: 50px; }
    .container { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); width: 350px; }
    h2 { color: #2c3e50; text-align: center; }
    
    select { width: 100%; padding: 12px; margin: 20px 0; border: 2px solid #ddd; border-radius: 6px; font-size: 16px; box-sizing: border-box; }
    
    .btn-group { display: flex; flex-direction: column; gap: 12px; }
    
    /* 共通ボタン設定：文字色を確実に白に固定 */
    .btn { 
        display: block; 
        padding: 14px; 
        border: none; 
        border-radius: 6px; 
        color: #ffffff !important; /* 文字を白に強制 */
        font-weight: bold; 
        text-align: center; 
        text-decoration: none; /* 下線を消す */
        transition: 0.3s; 
        box-sizing: border-box;
    }

    .btn-review { background-color: #3498db; } /* 青 */
    .btn-review:hover { background-color: #2980b9; }

    .btn-mock { background-color: #e67e22; } /* オレンジ */
    .btn-mock:hover { background-color: #d35400; }

    .btn-home { background-color: #7f8c8d; margin-top: 10px; } /* グレー */
    .btn-home:hover { background-color: #636e72; }
</style>
</head>
<body>
<div class="container">
    <h2>学習モード選択</h2>
    
    <select id="subjectSelect">
        <option value="">科目を選択してください...</option>
        <% if (subjectList != null) {
            for (Subject s : subjectList) { %>
                <option value="<%= s.getSubjectId() %>"><%= s.getName() %></option>
        <% } } %>
    </select>

    <div class="btn-group">
        <a href="#" id="reviewLink" class="btn btn-review">復習モードを開始</a>
        <a href="#" id="mockLink" class="btn btn-mock">模擬試験モードを開始</a>
        
        <a href="HomeServlet" class="btn btn-home">ホームに戻る</a>
    </div>
</div>

<script>
    const select = document.getElementById('subjectSelect');
    const reviewLink = document.getElementById('reviewLink');
    const mockLink = document.getElementById('mockLink');

    select.addEventListener('change', function() {
        const id = this.value;
        if (id) {
            reviewLink.href = "StudyServlet?subjectId=" + id;
            mockLink.href = "MockExamServlet?subjectId=" + id;
        } else {
            reviewLink.href = "#";
            mockLink.href = "#";
        }
    });
</script>
</body>
</html>