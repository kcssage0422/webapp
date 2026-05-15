<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新規登録 | 学習管理アプリ</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: #333;
        }
        .register-card {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        h2 { margin-bottom: 30px; color: #2c3e50; font-size: 22px; }
        .error-msg {
            background-color: #fee;
            color: #e74c3c;
            padding: 10px;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 20px;
            border: 1px solid #fab1a0;
        }
        .input-group { text-align: left; margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-size: 14px; color: #7f8c8d; }
        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 16px;
        }
        button {
            width: 100%;
            padding: 14px;
            background-color: #2ecc71; /* 登録は緑色でポジティブに */
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 15px;
        }
        button:hover { background-color: #27ae60; }
        .footer-links { margin-top: 20px; font-size: 14px; }
        .footer-links a { color: #3498db; text-decoration: none; }
    </style>
</head>
<body>
    <div class="register-card">
        <h2>新規ユーザー登録</h2>
        <% if (request.getAttribute("errorMsg") != null) { %>
            <div class="error-msg">${errorMsg}</div>
        <% } %>
        <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
            <div class="input-group">
                <label for="userName">ユーザー名</label>
                <input type="text" id="userName" name="userName" required>
            </div>
            <div class="input-group">
                <label for="password">パスワード</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="input-group">
                <label for="checkPassword">確認用パスワード</label>
                <input type="password" id="checkPassword" name="checkPassword" required>
            </div>
            <button type="submit">登録を完了する</button>
        </form>
        <div class="footer-links">
            <a href="LoginServlet">ログイン画面に戻る</a>
        </div>
    </div>
</body>
</html>