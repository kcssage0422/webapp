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
import model_dto.Flashcard;
import model_dto.User;

/**
 * Servlet implementation class StudyServlet
 */
@WebServlet("/StudyServlet")
public class StudyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
		    User currentUser = (User) session.getAttribute("currentUser");
		    if (currentUser == null) {
	            // ログインしていない場合はログイン画面へ戻す
	            response.sendRedirect("LoginServlet"); 
	            return;
	        }
		    // 科目一覧を取得
		    FlashcardDAO sDao = new FlashcardDAO();
		    List<Flashcard> flashCardList = sDao.findCardsForReview(currentUser.getId());
		    // 学習するカードがないなら、HomeServletに返す
		    if(flashCardList.isEmpty()) {
		    	session.setAttribute("msg", "今回の学習するカードはありません");
		    	response.sendRedirect("HomeServlet");
		    	return;
		    }
		    // JSPへ渡して表示
		    request.setAttribute("flashCardList", flashCardList);
		    request.getRequestDispatcher("WEB-INF/jsp/studyCard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8"); // 文字化け防止

	    // パラメータとセッションの取得
	    String action = request.getParameter("action");
	    String studyTimeStr = request.getParameter("studyTime");
	    String subjectIdStr = request.getParameter("subjectId");
	    String cardIdStr = request.getParameter("cardId");
	    String result = request.getParameter("result");
	    
	    HttpSession session = request.getSession();
	    User loginUser = (User) session.getAttribute("currentUser");

	    System.out.println("--- StudyServlet: doPost が呼び出されました ---");
	    System.out.println("action: " + action);
	    System.out.println("studyTime: " + studyTimeStr);
	    System.out.println("subjectId: " + subjectIdStr);

	    // =========================================================
	    // 🕒 1. タイマーからの保存リクエスト（action = timer_record）
	    // =========================================================
	    if ("timer_record".equals(action)) {
	        System.out.println("-> 【検知】タイマーからの保存処理を実行します");

	        if (loginUser == null) {
	            System.out.println("【警告】未ログインのため401エラーを返します");
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;
	        }
	        if (subjectIdStr == null || studyTimeStr == null) {
	            System.out.println("【警告】パラメータ不足のため400エラーを返します");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        try {
	            int minutes = Integer.parseInt(studyTimeStr);
	            int subjectId = Integer.parseInt(subjectIdStr);

	            System.out.println("-> DAOを呼び出します: userId=" + loginUser.getId() + ", minutes=" + minutes);
	            
	            // Aivenデータベースへ保存
	            FlashcardDAO dao = new FlashcardDAO();
	            dao.insertStudyRecord(loginUser.getId(), subjectId, minutes);
	            
	            System.out.println("【成功】Aivenへの学習記録保存が完了しました！");
	            
	            // 🌟【超重要】fetch通信なのでリダイレクトは絶対せず、200 OK（成功ステータス）だけを返す
	            response.setStatus(HttpServletResponse.SC_OK);
	            return; 
	            
	        } catch (NumberFormatException e) {
	            System.out.println("【エラー】数値変換失敗: " + e.getMessage());
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }
	    }

	    // =========================================================
	    // 📇 2. 単語カードの復習結果保存リクエスト（従来の処理）
	    // =========================================================
	    if (loginUser == null) {
	        response.sendRedirect("LoginServlet"); 
	        return;
	    }

	    if (cardIdStr != null && result != null && subjectIdStr != null) {
	        try {
	            int cardId = Integer.parseInt(cardIdStr);
	            int subjectId = Integer.parseInt(subjectIdStr);
	            
	            FlashcardDAO dao = new FlashcardDAO();
	            
	            // 復習予定日の更新
	            boolean isCorrect = result.equals("correct");
	            dao.updateReviewResult(cardId, isCorrect);

	            // 学習記録の保存
	            if (studyTimeStr != null) {
	                int seconds = Integer.parseInt(studyTimeStr);
	                int minutes = (int) Math.ceil(seconds / 60.0);
	                if (minutes == 0 && seconds > 0) minutes = 1;

	                dao.insertStudyRecord(loginUser.getId(), subjectId, minutes);
	                System.out.println("カード学習記録成功: " + minutes + "分");
	            }
	            
	        } catch (NumberFormatException e) {
	            e.printStackTrace();
	        }
	        
	        // カード機能は画面移動を伴うので、通常のリダイレクトを行う
	        response.sendRedirect("StudyServlet");
	        return;
	    }
	    
	    // 3. どちらの条件にも当てはまらなかった場合
	    System.out.println("【警告】条件不一致のためHomeへ戻します。");
	    response.sendRedirect("HomeServlet");
	}
}
