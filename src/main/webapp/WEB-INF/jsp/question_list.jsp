<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, model_dto.Flashcard, model_dto.Subject, model_dto.User"%>
<%
// 💡 サーブレットから渡されたデータを正しく受け取る
List<Flashcard> myFlashcard = (List<Flashcard>) request.getAttribute("myCardList");
List<java.util.Map<String, Object>> publicFlashcard = (List<java.util.Map<String, Object>>) request.getAttribute("publicCardList");
List<Subject> subjectList = (List<Subject>) request.getAttribute("subjectList");
User currentUser = (User) request.getAttribute("currentUser");
List<Subject> publicSubjectList = (List<Subject>) request.getAttribute("publicSubjectList");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>問題一覧 | DailyKnowly</title>
<style type="text/css">
body {
	font-family: 'Helvetica Neue', Arial, sans-serif;
	background: #f5f7fa;
	color: #2c3e50;
	padding: 30px;
	margin: 0;
}

.top-bar {
	max-width: 700px;
	margin: 0 auto 15px auto;
	display: flex;
	justify-content: flex-start;
}

.btn-home {
	background-color: #7f8c8d;
	display: inline-flex;
	align-items: center;
	gap: 5px;
}

.btn-home:hover {
	background-color: #95a5a6;
}

.section-container {
	max-width: 700px;
	margin: 0 auto 40px auto;
	background: white;
	padding: 30px;
	border-radius: 12px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

h1 {
	font-size: 24px;
	border-bottom: 2px solid #2c3e50;
	padding-bottom: 10px;
	margin-top: 0;
	margin-bottom: 20px;
}

.subject-group {
	background: #fdfefe;
	border: 1px solid #e5e8e8;
	border-radius: 8px;
	padding: 15px;
	margin-bottom: 20px;
}

.subject-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	background: #ebf5fb;
	padding: 10px 15px;
	border-radius: 6px;
	font-weight: bold;
	margin-bottom: 10px;
}

.card-item {
	padding: 10px 15px;
	border-bottom: 1px dashed #ddd;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.card-item:last-child {
	border-bottom: none;
}

.card-info {
	font-size: 15px;
}

.actions {
	display: flex;
	gap: 5px;
}

.btn {
	display: inline-block;
	padding: 6px 12px;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-size: 13px;
	font-weight: bold;
	border: none;
	cursor: pointer;
	transition: background-color 0.2s;
}

.btn-edit {
	background-color: #3498db;
}

.btn-edit:hover {
	background-color: #2980b9;
}

.btn-delete {
	background-color: #e74c3c;
}

.btn-delete:hover {
	background-color: #c0392b;
}

.badge {
	font-size: 11px;
	padding: 2px 6px;
	border-radius: 4px;
	margin-left: 5px;
	font-weight: normal;
}

.public-badge {
	background-color: #e8f8f5;
	color: #2ecc71;
}

.private-badge {
	background-color: #fbeee6;
	color: #e67e22;
}
</style>
</head>
<body>

	<div class="top-bar">
		<a href="HomeServlet" class="btn btn-home">←ホームへ戻る</a>
	</div>

	<div class="section-container">
		<h1>マイ問題</h1>

		<%
		// 自分の作成した科目が1つ以上ある場合のみループ
		if (subjectList != null && !subjectList.isEmpty()) {
			for (Subject s : subjectList) {
				boolean hasMyCards = false;
		%>
		<div class="subject-group">
			<div class="subject-header">
				<div>
					<span>📁 <%= s.getName() %></span>
					<% if (s.getIs_public() != null && s.getIs_public()) { %>
						<span class="badge public-badge">公開中</span>
					<% } else { %>
						<span class="badge private-badge">非公開</span>
					<% } %>
				</div>
				
				<div>
					<form action="SubjectUpdateServlet" method="get" style="display: inline;">
						<input type="hidden" name="subjectId" value="<%= s.getSubjectId() %>">
						<button type="submit" class="btn" style="background-color: #2ecc71; color: white; padding: 4px 10px; font-size: 12px; border-radius: 4px;">
							⚙️ 科目を編集
						</button>
					</form>
				</div>
			</div>

			<div class="card-list">
				<%
				if (myFlashcard != null) {
					for (Flashcard myQ : myFlashcard) {
						String cardSubId = String.valueOf(myQ.getSubjectId()).trim();
						String sSubId = String.valueOf(s.getSubjectId()).trim();

						if (cardSubId.equals(sSubId)) {
							hasMyCards = true;
				%>
				<div class="card-item">
					<div class="card-info">
						<strong>Q.</strong> <%= myQ.getQuestion() %><br>
						<small style="color: #7f8c8d;">A. <%= myQ.getAnswer() %></small>
					</div>
					
					<div class="actions">
						<form action="CardEditServlet" method="get" style="display: inline;">
							<input type="hidden" name="cardId" value="<%= myQ.getCardId() %>">
							<button type="submit" class="btn btn-edit">編集</button>
						</form>
						<form action="CardDeleteServlet" method="post" style="display: inline;" 
							  onsubmit="return confirm('本当に削除しますか？');">
							<input type="hidden" name="cardId" value="<%= myQ.getCardId() %>">
							<button type="submit" class="btn btn-delete">削除</button>
						</form>
					</div>
				</div>
				<%
						}
					}
				}
				
				if (!hasMyCards) {
				%>
				<p style="color: #95a5a6; font-size: 13px; text-align: center; margin: 10px 0 0 0;">
					この科目に登録されている問題はまだありません。
				</p>
				<%
				}
				%>
			</div>
		</div>
		<%
			}
		} else {
		%>
		<p style="text-align: center; color: #7f8c8d; padding: 20px 0;">作成済みの科目がありません。まずはホームから科目を作成してください。</p>
		<%
		}
		%>
	</div>


	<div class="section-container" style="margin-top: 40px;">
		<h1>公開問題</h1>

		<%
		if (publicSubjectList != null && publicFlashcard != null && !publicSubjectList.isEmpty()) {
			for (Subject s : publicSubjectList) {
				
				// 事前にこの公開科目に属するカードがあるか数える
				int visibleCardCount = 0;
				for (java.util.Map<String, Object> map : publicFlashcard) {
					Flashcard pq = (Flashcard) map.get("cardData");
					if (pq != null) {
						String pqSubId = String.valueOf(pq.getSubjectId()).trim();
						String sSubId = String.valueOf(s.getSubjectId()).trim();
						if (pqSubId.equals(sSubId)) {
							visibleCardCount++;
						}
					}
				}

				// 公開問題がない科目の枠は表示しない
				if (visibleCardCount == 0) {
					continue;
				}
		%>
		<div class="subject-group">
			<div class="subject-header" style="background: #f4f6f7;">
				<span>📁 <%= s.getName() %></span>
			</div>

			<div class="card-list">
				<%
				for (java.util.Map<String, Object> map : publicFlashcard) {
					Flashcard pq = (Flashcard) map.get("cardData");
					
					if (pq != null) {
						String pqSubId = String.valueOf(pq.getSubjectId()).trim();
						String sSubId = String.valueOf(s.getSubjectId()).trim();

						if (pqSubId.equals(sSubId)) {
							
							// すでに同じ問題を自分が持っているかチェック
							boolean isDownloaded = false;
							if (myFlashcard != null) {
								for (Flashcard myQ : myFlashcard) {
									if (myQ.getQuestion() != null && pq.getQuestion() != null &&
										myQ.getQuestion().trim().equals(pq.getQuestion().trim())) {
										isDownloaded = true;
										break;
									}
								}
							}
				%>
				<div class="card-item">
					<div class="card-info">
						<strong>Q.</strong> <%= pq.getQuestion() %><br> 
						<small style="color: #7f8c8d;">A. <%= pq.getAnswer() %></small>
					</div>
					
					<div class="actions">
						<% if (isDownloaded) { %>
							<button class="btn" style="background-color: #bdc3c7; cursor: not-allowed;" disabled>✓ 取得済み</button>
						<% } else { %>
							<form action="CardDownloadServlet" method="post" style="display: inline;">
								<input type="hidden" name="cardId" value="<%= pq.getCardId() %>">
								<button type="submit" class="btn" style="background-color: #9b59b6;">📥 取得</button>
							</form>
						<% } %>
					</div>
				</div>
				<%
						}
					}
				}
				%>
			</div>
		</div>
		<%
			}
		} else {
		%>
		<p style="text-align: center; color: #7f8c8d; padding: 20px 0;">公開されている問題はまだありません。</p>
		<%
		}
		%>
	</div>

</body>
</html>