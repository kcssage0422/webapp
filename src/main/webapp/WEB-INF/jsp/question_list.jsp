<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, model_dto.Flashcard, model_dto.Subject, model_dto.User"%>
<%
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

/* 🏠 ホーム戻るボタン用のエリアとスタイル設定 */
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
/* ボタンの共通スタイル */
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

/* 🔒 リンクからボタン仕様に変更するためのスタイル調整 */
.btn-subject {
	background-color: #2ecc71;
	font-size: 12px;
	padding: 4px 8px;
	border: none;
	font-family: inherit;
}

.btn-subject:hover {
	background-color: #27ae60;
}

.status-badge {
	font-size: 11px;
	padding: 2px 6px;
	border-radius: 4px;
	margin-left: 5px;
	font-weight: normal;
}

.status-public {
	background-color: #e8f8f5;
	color: #2ecc71;
}

.status-private {
	background-color: #fbeee6;
	color: #e67e22;
}
</style>
</head>
<body>
	<div class="top-bar">
		<%-- 
		  ※ もしホーム画面を表示する際にサーブレット（例: HomeServlet）を経由させている場合は、
		  href="HomeServlet" に書き換えてください。単にJSPを開くだけなら "home.jsp" でOKです。
		--%>
		<a href="HomeServlet" class="btn btn-home">←ホームへ戻る</a>
	</div>
	<div class="section-container">
		<h1>マイ問題</h1>

		<%
		// 1. 自分の科目リストが存在するかチェック
		if (subjectList != null) {
			for (Subject s : subjectList) {
				// マイ問題側は、非公開の科目であっても自分自身のものなので全て表示します
				boolean hasMyCards = false;
		%>
		<div class="subject-group">
			<%-- 科目名ヘッダー --%>
			<div class="subject-header">
				<span>📁 <%= s.getName() %></span>
				<% if (s.getIs_public() != null && s.getIs_public()) { %>
					<span class="badge public-badge">公開中</span>
				<% } else { %>
					<span class="badge private-badge">非公開</span>
				<% } %>
			</div>

			<%-- その科目に属する自分の問題リスト --%>
			<div class="card-list">
				<%
				if (myFlashcard != null) {
					for (Flashcard myQ : myFlashcard) {
						// 💡 【超重要】問題の科目IDと、現在の科目のIDが一致しているか判定
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
					
					<%-- 編集・削除ボタンのエリア（マイ問題のみ表示） --%>
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
				
				// もしこの科目に自分の問題が1つも登録されていない場合
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
		<p style="text-align: center; color: #7f8c8d;">作成済みの科目がありません。まずは科目を作成してください。</p>
		<%
		}
		%>
	</div>


	<div class="section-container" style="margin-top: 40px;">
		<h1>公開問題</h1>

		<%
		// 1. 公開されている科目リストと公開問題リストが存在するかチェック
		if (publicSubjectList != null && publicFlashcard != null) {
			for (Subject s : publicSubjectList) {
				
				// 💡 事前に「この公開科目に紐づく公開問題」が本当に存在するかカウントする
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

				// 💡 公開問題が1件もない科目の場合は、枠（フォルダ）ごと画面に表示しない！
				if (visibleCardCount == 0) {
					continue;
				}
		%>
		<div class="subject-group">
			<div class="subject-header" style="background: #f4f6f7;">
				<span> 📁 <%= s.getName() %></span>
			</div>

			<div class="card-list">
				<%
				// 2. 実際に1件以上あることが確定しているので、中身をループして出力
				for (java.util.Map<String, Object> map : publicFlashcard) {
					Flashcard pq = (Flashcard) map.get("cardData");
					
					if (pq != null) {
						String pqSubId = String.valueOf(pq.getSubjectId()).trim();
						String sSubId = String.valueOf(s.getSubjectId()).trim();

						if (pqSubId.equals(sSubId)) {
							
							// 💡 すでに同じ問題を自分が持っているかチェック
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
							<%-- 取得済みの場合はグレーの無効化ボタン --%>
							<button class="btn" style="background-color: #bdc3c7; cursor: not-allowed;" disabled>✓ 取得済み</button>
						<% } else { %>
							<%-- 未取得の場合は紫色の取得ボタン --%>
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
			} // 外側の公開科目ループの終わり
		} else {
		%>
		<p style="text-align: center; color: #7f8c8d;">公開されている問題はありません。</p>
		<%
		}
		%>
	</div>

	

</body>
</html>