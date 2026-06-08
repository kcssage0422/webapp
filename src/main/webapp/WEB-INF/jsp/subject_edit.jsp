<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model_dto.Subject"%>
<%
// サーブレットのdoGetから渡された、編集対象の科目データを取得
Subject subject = (Subject) request.getAttribute("subject");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>科目設定 | DailyKnowly</title>
<style type="text/css">
body {
	font-family: 'Helvetica Neue', Arial, sans-serif;
	background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
	margin: 0;
	padding: 20px;
}

.card {
	background: white;
	padding: 40px;
	border-radius: 12px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
	width: 100%;
	max-width: 450px;
}

h1 {
	margin-top: 0;
	color: #2c3e50;
	font-size: 22px;
	text-align: center;
	margin-bottom: 30px;
}

.form-group {
	margin-bottom: 25px;
	text-align: left;
}

.form-group label {
	display: block;
	margin-bottom: 8px;
	font-size: 14px;
	color: #7f8c8d;
	font-weight: bold;
}

input[type="text"] {
	width: 100%;
	padding: 12px;
	border: 1px solid #ddd;
	border-radius: 8px;
	box-sizing: border-box;
	font-size: 16px;
	transition: 0.3s;
}

input[type="text"]:focus {
	outline: none;
	border-color: #3498db;
}

/* ⭕️ チェックボックス専用のレイアウトグループ（改行バグ完全防止） */
.checkbox-group {
	margin-bottom: 30px;
	text-align: left;
}

.btn-submit {
	width: 100%;
	padding: 14px;
	background-color: #2ecc71; /* 保存ボタンは信頼の緑色 */
	color: white;
	border: none;
	border-radius: 8px;
	font-size: 16px;
	font-weight: bold;
	cursor: pointer;
	transition: 0.3s;
}

.btn-submit:hover {
	background-color: #27ae60;
	transform: translateY(-2px);
}

.footer-links {
	text-align: center;
	margin-top: 25px;
	font-size: 14px;
}

.footer-links a {
	color: #7f8c8d;
	text-decoration: none;
}
.footer-links a:hover {
	color: #3498db;
}
</style>
</head>
<body>

	<div class="card">
		<h1>科目の設定変更</h1>

		<form action="SubjectUpdateServlet" method="post">
			
			<input type="hidden" name="subjectId" value="<%= subject.getSubjectId() %>">

			<div class="form-group">
				<label for="subjectName">科目名</label>
				<input type="text" name="subjectName" id="subjectName" value="<%= subject.getName() %>" required>
			</div>

			<div class="checkbox-group">
				<label style="display: inline-flex; align-items: center; gap: 8px; cursor: pointer; white-space: nowrap; color: #2c3e50;">
					<%-- SubjectクラスのgetIs_public()がnullでなく、かつtrueの時に初期チェックを入れます --%>
					<input type="checkbox" name="isPublic" value="true" 
						<%= (subject.getIs_public() != null && subject.getIs_public()) ? "checked" : "" %>>
					<span style="display: inline-block; font-weight: normal;">この科目を丸ごと公開スペースにシェアする</span>
				</label>
			</div>

			<button type="submit" class="btn-submit">設定を保存する</button>
		</form>

		<div class="footer-links">
			<a href="QuestionListServlet">キャンセルして戻る</a>
		</div>
	</div>

</body>
</html>