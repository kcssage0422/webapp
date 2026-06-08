<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model_dto.Subject, model_dto.Flashcard"%>
<%
Flashcard card = (Flashcard) request.getAttribute("card");
List<Subject> subjectList = (List<Subject>) request.getAttribute("subjectList");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>カード削除確認 | DailyKnowly</title>
<style>
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
	max-width: 500px;
}

h1 {
	margin-top: 0;
	color: #2c3e50;
	font-size: 22px;
	text-align: center;
	margin-bottom: 30px;
}

.form-group {
	margin-bottom: 20px;
	text-align: left;
}

.form-group label {
	display: block;
	margin-bottom: 8px;
	font-size: 14px;
	color: #7f8c8d;
	font-weight: bold;
}

input, textarea, select {
	width: 100%;
	padding: 12px;
	border: 1px solid #ddd;
	border-radius: 8px;
	box-sizing: border-box;
	font-size: 16px;
	transition: 0.3s;
}

/* 💡 読み取り専用（入力不可）のときの背景色を少しグレーにして、触れないことを分かりやすくします */
input:disabled, textarea:disabled, select:disabled {
	background-color: #f5f5f5;
	color: #7f8c8d;
	cursor: not-allowed;
}

input:focus, textarea:focus, select:focus {
	outline: none;
	border-color: #3498db;
}

textarea {
	height: 100px;
	resize: vertical;
}

/* 💡 削除ボタンなので、緑（#2ecc71）から警戒色の赤・オレンジ系に変更するとユーザーに親切です */
.btn-submit {
	width: 100%;
	padding: 14px;
	background-color: #e74c3c;
	color: white;
	border: none;
	border-radius: 8px;
	font-size: 16px;
	font-weight: bold;
	cursor: pointer;
	transition: 0.3s;
	margin-top: 10px;
}

.btn-submit:hover {
	background-color: #c0392b;
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

.checkbox-group {
	margin-bottom: 20px;
	text-align: left;
}
</style>
</head>
<body>

	<div class="card">
		<h1>暗記カードを削除しますか？</h1>

		<form action="CardDeleteServlet" method="post">

			<%-- サーブレット側で削除を実行するために必要な隠しパラメータ --%>
			<input type="hidden" name="cardId" value="<%=card.getCardId()%>">

			<%-- 💡 選択不可にするため select に disabled を追加 --%>
			<div class="form-group">
				<label for="subjectId">科目</label> 
				<select name="subjectId" id="subjectId" disabled>
					<option value="">科目を選択してください</option>
					<%
					if (subjectList != null) {
						for (Subject s : subjectList) {
							String sIdStr = String.valueOf(s.getSubjectId());
							String cIdStr = String.valueOf(card.getSubjectId());
							String selectedStr = sIdStr.trim().equals(cIdStr.trim()) ? "selected" : "";
					%>
					<option value="<%=s.getSubjectId()%>" <%=selectedStr%>><%=s.getName()%></option>
					<%
						}
					}
					%>
				</select>
				<%-- 💡 読み込み専用なので「新しい科目を追加」のリンクは削除または非表示にします --%>
			</div>

			<%-- 💡 入力不可にするため textarea に disabled を追加 --%>
			<div class="form-group">
				<label for="question">問題（表面）</label>
				<textarea name="question" id="question" disabled><%=card.getQuestion()%></textarea>
			</div>

			<%-- 💡 入力不可にするため textarea に disabled を追加 --%>
			<div class="form-group">
				<label for="answer">答え（裏面）</label>
				<textarea name="answer" id="answer" disabled><%=card.getAnswer()%></textarea>
			</div>

			<%-- 💡 変更不可にするため input(checkbox) に disabled を追加 --%>
			<div class="checkbox-group">
				<label style="display: inline-flex; align-items: center; gap: 8px; cursor: not-allowed; white-space: nowrap; color: #7f8c8d;">
					<input type="checkbox" name="isPublic" value="true" <%=card.getIs_public() ? "checked" : ""%> disabled> 
					<span style="display: inline-block; font-weight: normal;">この科目を公開スペースにシェアする</span>
				</label>
			</div>

			<button type="submit" class="btn-submit">このカードを削除する</button>
		</form>

		<div class="footer-links">
			<a href="QuestionListServlet">戻る</a>
		</div>
	</div>

</body>
</html>