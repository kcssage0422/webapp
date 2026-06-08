package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model_dao.FlashcardDAO;
import model_dao.SubjectDAO;
import model_dto.Flashcard;
import model_dto.User;

/**
 * Servlet implementation class CardAddServlet
 */
@WebServlet("/CardAddServlet")
public class CardAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // 1. セッションからユーザーID取得（既存の仕組みを利用）
	    HttpSession session = request.getSession();
	    User currentUser = (User) session.getAttribute("currentUser");
	    
	    // 2. 科目一覧を取得
	    SubjectDAO sDao = new SubjectDAO();
	    List<model_dto.Subject> subjectList = sDao.findByUserId(currentUser.getId());
	    
	    // 3. JSPへ渡して表示
	    request.setAttribute("subjectList", subjectList);
	    request.getRequestDispatcher("WEB-INF/jsp/card_add.jsp").forward(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // 文字化け対策（日本語を扱うため）
	    request.setCharacterEncoding("UTF-8");

	    // セッションからログイン中のユーザー情報を取得
	    HttpSession session = request.getSession();
	    User currentUser = (User) session.getAttribute("currentUser");
	    
	    // 念のためログインチェック
	    if (currentUser == null) {
	        response.sendRedirect("LoginServlet");
	        return;
	    }

	    // フォームから送信された値を取得
	    try {
	        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
	        String question = request.getParameter("question");
	        String answer = request.getParameter("answer");
	        boolean is_public = request.getParameter("isPublic") != null;

	        // DTO（JavaBeans）にデータをセット
	        Flashcard card = new Flashcard();
	        card.setUserId(currentUser.getId());
	        card.setSubjectId(subjectId);
	        card.setQuestion(question);
	        card.setAnswer(answer);
	        card.setIs_public(is_public);
	        
	        // DAOを使ってDBに保存
	        FlashcardDAO dao = new FlashcardDAO();
	        boolean isSuccess = dao.insert(card);

	        if (isSuccess) {
	            // 保存成功：ひとまずカード作成画面にメッセージ付きで戻すか、一覧画面へ
	            request.setAttribute("message", "カードを登録しました！");
	            // 科目一覧を再取得して、再び登録画面を表示（doGetを呼び出すイメージ）
	            doGet(request, response);
	        } else {
	            // 保存失敗
	            request.setAttribute("error", "登録に失敗しました。");
	            doGet(request, response);
	        }

	    } catch (NumberFormatException e) {
	        request.setAttribute("error", "科目が正しく選択されていません。");
	        doGet(request, response);
	    }
	}
}
