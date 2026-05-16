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

	    // パラメータの取得
	    String studyTimeStr = request.getParameter("studyTime");
	    String subjectIdStr = request.getParameter("subjectId");
	    String cardIdStr = request.getParameter("cardId");
	    String result = request.getParameter("result");
	    
	    HttpSession session = request.getSession();
	    User loginUser = (User) session.getAttribute("currentUser");

	    System.out.println("--- StudyServlet: doPost が呼び出されました ---");
	    System.out.println("studyTime: " + studyTimeStr);
	    System.out.println("subjectId: " + subjectIdStr);
	    System.out.println("cardId: " + cardIdStr);

	    // ---------------------------------------------------------
	    // 【判定】カードの復習機能か、タイマー機能か
	    // ---------------------------------------------------------
	    if (cardIdStr == null && studyTimeStr != null) {
	        // 🕒 タイマーからの学習記録保存処理
	        System.out.println("-> タイマーからのデータ送信を検知しました。");

	        if (loginUser == null) {
	            System.out.println("【警告】ログインユーザーがnullのため、保存をスキップしました。");
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;
	        }
	        if (subjectIdStr == null) {
	            System.out.println("【警告】subjectIdがnullのため、保存をスキップしました。");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        try {
	            int minutes = Integer.parseInt(studyTimeStr);
	            int subjectId = Integer.parseInt(subjectIdStr);

	            System.out.println("-> DAOの呼び出し直前: userId=" + loginUser.getId() + ", subjectId=" + subjectId + ", minutes=" + minutes);
	            
	            FlashcardDAO dao = new FlashcardDAO();
	            dao.insertStudyRecord(loginUser.getId(), subjectId, minutes);
	            
	            System.out.println("【成功】Aivenデータベースへの保存命令が完了しました！");
	            response.setStatus(HttpServletResponse.SC_OK);
	            return;
	            
	        } catch (NumberFormatException e) {
	            System.out.println("【エラー】数値の変換に失敗しました: " + e.getMessage());
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	    } else if (cardIdStr != null && result != null && subjectIdStr != null) {
	        // 📇 単語カードの復習結果保存処理
	        System.out.println("-> 単語カードからのデータ送信を検知しました。");
	        
	        if (loginUser == null) {
	            response.sendRedirect("LoginServlet"); 
	            return;
	        }

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
	        
	        // 次の問題へ（StudyServletのdoGetへ）
	        response.sendRedirect("StudyServlet");
	        return;
	    }
	    
	    // どちらの条件にも当てはまらなかった場合
	    System.out.println("【警告】タイマー、カードどちらの条件にも一致しませんでした。");
	    response.sendRedirect("HomeServlet");
	}
	
	
}
