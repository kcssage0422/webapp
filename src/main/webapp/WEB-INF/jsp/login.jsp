<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン | DailyKnowly</title>
<style>
body {
	font-family: 'Helvetica Neue', Arial, sans-serif;
	background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
	margin: 0;
	color: #333;
}

.login-wrapper {
	width: 100%;
	max-width: 400px;
	text-align: center;
}

/* タイトルエリア（枠線なし） */
.title-area {
	position: relative;
	display: inline-block;
	margin-bottom: 40px;
}

.main-title {
	margin: 0;
	font-size: 4rem;
	color: #2c3e50;
	letter-spacing: 1px;
}

.version-badge {
	position: absolute;
	right: -35px; /* タイトルの右側に配置 */
	bottom: 5px;
	font-size: 0.8rem;
	color: #95a5a6;
	font-weight: bold;
}

/* ログインカード */
.login-card {
	background: white;
	padding: 40px;
	border-radius: 12px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

h2 {
	margin-bottom: 20px;
	color: #7f8c8d;
	font-size: 18px;
}

/* 以下、入力項目などは共通 */
.error-msg {
	background-color: #fee;
	color: #e74c3c;
	padding: 10px;
	border-radius: 6px;
	font-size: 14px;
	margin-bottom: 20px;
	border: 1px solid #fab1a0;
}

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

input[type="text"], input[type="password"] {
	width: 100%;
	padding: 12px;
	border: 1px solid #ddd;
	border-radius: 8px;
	box-sizing: border-box;
}

button {
	width: 100%;
	padding: 14px;
	background-color: #3498db;
	color: white;
	border: none;
	border-radius: 8px;
	font-weight: bold;
	cursor: pointer;
}

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
</style>
</head>
<body>

	<div class="login-wrapper">
		<div class="title-area">
			<h1 class="main-title">DailyKnowly</h1><br>
			<span class="version-badge">v1.2.1</span>
		</div>

		<div class="login-card">
			<h2>LOGIN</h2>
			<%
			if (request.getAttribute("errorMsg") != null) {
			%>
			<div class="error-msg">${errorMsg}</div>
			<%
			}
			%>

			<form action="${pageContext.request.contextPath}/LoginServlet"
				method="post">
				<div class="input-group">
					<label for="userName">ユーザー名</label> <input type="text"
						id="userName" name="userName" required autofocus>
				</div>
				<div class="input-group">
					<label for="password">パスワード</label> <input type="password"
						id="password" name="password" required>
				</div>
				<button type="submit">ログイン</button>
			</form>

			<div class="footer-links">
				アカウントをお持ちでないですか？<br> <a href="RegisterServlet">新規アカウント作成</a>
			</div>
		</div>
	</div>

</body>
</html>