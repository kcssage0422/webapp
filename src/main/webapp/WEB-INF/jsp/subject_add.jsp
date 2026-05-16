<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>科目登録 | DailyKnowly</title>
    <style>
        body { font-family: sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 320px; text-align: center; }
        h1 { font-size: 20px; color: #2c3e50; margin-bottom: 25px; }
        .form-group { text-align: left; margin-bottom: 20px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #7f8c8d; }
        input[type="text"] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; box-sizing: border-box; }
        input[type="color"] { width: 100%; height: 40px; border: none; cursor: pointer; }
        button { width: 100%; padding: 12px; background: #3498db; color: white; border: none; border-radius: 6px; font-weight: bold; cursor: pointer; }
        .back-link { display: block; margin-top: 20px; color: #95a5a6; text-decoration: none; font-size: 14px; }
    </style>
</head>
<body>
    <div class="card">
        <h1>科目の新規登録</h1>
        <form action="SubjectAddServlet" method="post">
            <div class="form-group">
                <label>科目名</label>
                <input type="text" name="subjectName" placeholder="例：Java、世界史" required>
            </div>
            <div class="form-group">
                <label>テーマカラー</label>
                <input type="color" name="colorCode" value="#3498db">
            </div>
            <button type="submit">登録する</button>
        </form>
        <a href="CardAddServlet" class="back-link">戻る</a>
    </div>
</body>
</html>