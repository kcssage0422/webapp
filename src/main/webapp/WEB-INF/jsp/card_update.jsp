<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title>カード編集 | DailyKnowly</title>
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

input:focus, textarea:focus, select:focus {
	outline: none;
	border-color: #3498db;
}

textarea {
	height: 100px;
	resize: vertical;
}

.btn-submit {
	width: 100%;
	padding: 14px;
	background-color: #2ecc71;
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
	background-color: #27ae60;
	transform: translateY(-2px);
}

.add-subject {
	display: inline-block;
	margin-top: 5px;
	font-size: 13px;
	color: #3498db;
	text-decoration: none;
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

/* ⭕️ チェックボックス専用のスタイルグループを追加して、外観を安定させます */
.checkbox-group {
	margin-bottom: 20px;
	text-align: left;
}
</style>
</head>
<body>

	<div class="card">
		<h1>暗記カードを編集</h1>

		<form action="CardEditServlet" method="post">

			<input type="hidden" name="cardId" value="<%=card.getCardId()%>">

			<div class="form-group">
				<label for="subjectId">科目</label> <select name="subjectId"
					id="subjectId" required>
					<option value="">科目を選択してください</option>
					<%
					if (subjectList != null) {
						for (Subject s : subjectList) {
							String sIdStr = String.valueOf(s.getSubjectId());
							String cIdStr = String.valueOf(card.getSubjectId());

							// 💡念のため、前後の余計な空白を trim() で削って比較します
							String selectedStr = sIdStr.trim().equals(cIdStr.trim()) ? "selected" : "";
					%>
					<option value="<%=s.getSubjectId()%>" <%=selectedStr%>><%=s.getName()%></option>
					<%
					}
					}
					%>
				</select> <a href="SubjectAddServlet" class="add-subject">＋ 新しい科目を追加</a>
			</div>

			<div class="form-group">
				<label for="question">問題（表面）</label>
				<textarea name="question" id="question" required><%=card.getQuestion()%></textarea>
			</div>

			<div class="form-group">
				<label for="answer">答え（裏面）</label>
				<textarea name="answer" id="answer" required><%=card.getAnswer()%></textarea>
			</div>

			<div class="checkbox-group">
				<label
					style="display: inline-flex; align-items: center; gap: 8px; cursor: pointer; white-space: nowrap; color: #2c3e50;">
					<input type="checkbox" name="isPublic" value="true"
					<%=card.getIs_public() ? "checked" : ""%>> <span
					style="display: inline-block; font-weight: normal;">この科目を公開スペースにシェアする</span>
				</label>
			</div>

			<button type="submit" class="btn-submit">カードを更新する</button>
		</form>

		<div class="footer-links">
			<a href="QuestionListServlet">戻る</a>
		</div>
	</div>

</body>
</html>