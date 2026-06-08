package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model_dao.FlashcardDAO;
import model_dao.SubjectDAO;
import model_dto.Subject;
import model_dto.User;

/**
 * 名前やURLマッピングは一切変えず、この1つで科目の表示（所有者チェック）と
 * 編集処理（カード連動アップデート）を安全に行うサーブレット
 */
@WebServlet("/SubjectUpdateServlet") // 💡 URLは元の「SubjectUpdateServlet」のままです
public class SubjectUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * ⭕️ 1. 科目編集画面の「表示」処理（内部処理用）
	 * 直接GETアクセスされた場合、またはdoPostから画面表示を任されたときに動きます。
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession();
	    User currentUser = (User) session.getAttribute("currentUser");
	    
	    if (currentUser == null) {
	        response.sendRedirect("login.jsp");
	        return;
	    }

	    // doPostのボタン経由、またはリクエストからIDを取り出す
	    String subjectIdStr = (String) request.getAttribute("subjectId");
	    if (subjectIdStr == null) {
	        subjectIdStr = request.getParameter("subjectId");
	    }

	    // 万が一IDが完全に空っぽ（不正なURL直打ちなど）の場合は一覧に戻す
	    if (subjectIdStr == null || subjectIdStr.isEmpty()) {
	        response.sendRedirect("QuestionListServlet");
	        return;
	    }

	    int subjectId = Integer.parseInt(subjectIdStr);
	    SubjectDAO subjectDao = new SubjectDAO();
	    Subject subject = subjectDao.getSubjectById(subjectId);

	    // 🔒 最重要セキュリティチェック（ログインユーザーと科目の所有者が一致するか）
	    if (subject != null && subject.getUserId() == currentUser.getId()) {
	        request.setAttribute("subject", subject);
	        // 📁 GET通信の枠組みの中で、安全に編集画面を表示（フォワード）する！（URL欄から変数消滅）
	        request.getRequestDispatcher("/WEB-INF/jsp/subject_edit.jsp").forward(request, response);
	    } else {
	        // 他人の科目IDを狙った不正アクセスの場合は一覧へ弾く
	        response.sendRedirect("QuestionListServlet");
	    }
	}

	/**
	 * ⭕️ 2. リクエストの「仕分け」および「編集内容の保存」処理
	 * 一覧画面のボタン（POST）が押されたとき、および編集画面の保存ボタンが押されたときの両方を受け持ちます。
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// セッションからログインユーザー情報を取得
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");

		if (currentUser == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		String subjectName = request.getParameter("subjectName");

		if (subjectName == null) {
			String subjectIdStr = request.getParameter("subjectId");
			request.setAttribute("subjectId", subjectIdStr);
			
			// 実際の画面表示は上の「doGet」にバトンタッチしてJSPを開く
			doGet(request, response);
			
		} else {
			//データベースの更新処理
			int subjectId = Integer.parseInt(request.getParameter("subjectId"));
			boolean isPublic = "true".equals(request.getParameter("isPublic"));

			SubjectDAO flashdao = new SubjectDAO();
			FlashcardDAO carddao = new FlashcardDAO();
			
			//  更新を実行
			flashdao.updateSubject(subjectId, subjectName, isPublic);
			
			// 科目の公開状態に合わせて、中身の問題もすべて一括更新！
			carddao.updateCardsPublicStatusBySubject(subjectId, isPublic);

			// 処理が終わったら、リリダイレクトで問題一覧画面へ戻る
			response.sendRedirect("QuestionListServlet");
		}
	}
}