<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新規ユーザー登録 | DailyKnowly</title>
    <style>
        /* 全体のリセットと背景 */
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

        /* ラッパー（タイトルとカードを縦に並べるための箱） */
        .register-wrapper {
            width: 100%;
            max-width: 380px;
            text-align: center;
        }

        /* カードの上のアプリタイトル（login.jspと共通） */
        .app-title {
            font-size: 28px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 100px;
            letter-spacing: 2px;
            text-shadow: 1px 1px 0px rgba(255, 255, 255, 0.5);
        }

        /* 新規登録カード */
        .register-card {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h2 {
            margin-bottom: 30px;
            color: #7f8c8d;
            font-size: 18px;
            letter-spacing: 1px;
        }

        /* エラーメッセージ */
        .error-msg {
            background-color: #fee;
            color: #e74c3c;
            padding: 10px;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 20px;
            border: 1px solid #fab1a0;
        }

        /* 入力エリア */
        .input-group {
            text-align: left;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            color: #7f8c8d;
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s;
        }

        input:focus {
            outline: none;
            border-color: #2ecc71; /* 新規登録はフレッシュな緑をアクセントに */
        }

        /* ボタン */
        button {
            width: 100%;
            padding: 14px;
            background-color: #2ecc71; /* ボタンも登録完了をイメージする緑に */
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.1s;
            margin-top: 10px;
        }

        button:hover {
            background-color: #27ae60;
        }

        button:active {
            transform: scale(0.98);
        }

        /* 下部リンク */
        .footer-links {
            margin-top: 25px;
            font-size: 14px;
            color: #7f8c8d;
        }

        .footer-links a {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
        }

        .footer-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="register-wrapper">
        
        <div class="app-title"><h1>DailyKnowly</h1></div>

        <div class="register-card">
            <h2>SIGN UP</h2>

            <% if (request.getAttribute("errorMsg") != null) { %>
                <div class="error-msg">${errorMsg}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
                <div class="input-group">
                    <label for="userName">ユーザー名</label>
                    <input type="text" id="userName" name="userName" required autofocus>
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
                すでにアカウントをお持ちですか？ <br>
                <a href="LoginServlet">ログイン画面に戻る</a>
            </div>
        </div>
        
    </div>

</body>
</html>