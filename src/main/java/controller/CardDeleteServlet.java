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
 * Servlet implementation class CardEditServlet
 */
@WebServlet("/CardDeleteServlet")
public class CardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 画面から送られてきたカードIDを取得
		HttpSession session = request.getSession();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if (currentUser == null) {
			response.sendRedirect("login.jsp");
			return;
		}
String subjectIdStr = request.getParameter("subjectId");
        
        // 念のためのバリデーション（IDが空っぽの場合は一覧に戻すなど）
        if (subjectIdStr == null || subjectIdStr.isEmpty()) {
            response.sendRedirect("QuestionListServlet");
            return;
        }
        
        try {
            int subjectId = Integer.parseInt(subjectIdStr);
            
            // 2. DAOを使って、データベースから該当する科目のデータを1件取得
            SubjectDAO subjectDao = new SubjectDAO();
            Subject subject = subjectDao.getSubjectById(subjectId); // 💡1件取得するメソッド
            
            if (subject != null) {
                // 3. 取得した科目データをリクエストスコープにセット
                request.setAttribute("subject", subject);
                
                // 4. 科目編集画面（前に作った subject_edit.jsp）へ画面を切り替える
                request.getRequestDispatcher("/WEB-INF/jsp/subject_edit.jsp").forward(request, response);
            } else {
                // 科目が見つからない場合は一覧へ戻す
                response.sendRedirect("QuestionListServlet");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("QuestionListServlet");
        }
    }

    
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		// 1. セッションからログインユーザー情報を取得
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");

		if (currentUser == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		int userId = currentUser.getId();

		// 2. 画面（またはURLリクエスト）から削除したいcardIdを取得
		int cardId = Integer.parseInt(request.getParameter("cardId"));

		// 3. DAOを呼び出して削除処理を実行
		FlashcardDAO cardDao = new FlashcardDAO();
		boolean isDeleted = cardDao.deleteCard(cardId, userId); // 💡ここで本人のIDも一緒に渡す！

		if (isDeleted) {
			// 削除が成功したら、二重送信を防ぐために「一覧画面」へリダイレクト
			response.sendRedirect("QuestionListServlet");
		} else {
			// 失敗、または他人のIDで不正操作された場合は、エラーにせず一覧へ戻す（またはエラー画面へ）
			System.out.println("警告：存在しないカード、または他人のカードの削除リクエストがブロックされました。");
			response.sendRedirect("QuestionListServlet");
		}
	}
}
