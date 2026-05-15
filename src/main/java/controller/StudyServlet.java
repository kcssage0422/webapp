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

	    // 1. 共通で使用するパラメータやセッションを取得
	    String action = request.getParameter("action");
	    String studyTimeStr = request.getParameter("studyTime");
	    String subjectIdStr = request.getParameter("subjectId");
	    
	    HttpSession session = request.getSession();
	    // ここでUserの型名は、プロジェクトの実際のクラス名（User または User_dto）に合わせてください
	    User loginUser = (User) session.getAttribute("currentUser");


	    // ---------------------------------------------------------
	    // パターンA：タイマーからの記録送信の場合
	    // ---------------------------------------------------------
	    if ("timer_record".equals(action)) {
	        if (loginUser == null) System.out.println("エラー: loginUserがnullです（ログインしていません）");
	        if (studyTimeStr == null) System.out.println("エラー: studyTimeStrがnullです");
	        if (subjectIdStr == null) System.out.println("エラー: subjectIdStrがnullです");

	        if (loginUser != null && studyTimeStr != null && subjectIdStr != null) {
	            try {
	                int seconds = Integer.parseInt(studyTimeStr);
	                int subjectId = Integer.parseInt(subjectIdStr);
	                int minutes = Math.round((float)seconds / 60);
	                if (minutes == 0 && seconds > 0) minutes = 1;

	                FlashcardDAO dao = new FlashcardDAO();
	                dao.insertStudyRecord(loginUser.getId(), subjectId, minutes);
	                response.setStatus(HttpServletResponse.SC_OK);
	                return;
	                
	            } catch (NumberFormatException e) {
	                System.out.println("エラー: 数値変換に失敗しました " + e.getMessage());
	            }
	        } else {
	            System.out.println("条件を満たさなかったため、DB登録をスキップしました。");
	        }
	        
	        response.sendRedirect("studyTimer.jsp"); 
	        return; // ここで処理終了
	    }


	    
	    // ログインしていない場合は、ログイン画面へリダイレクト
	    if (loginUser == null) {
	        response.sendRedirect("LoginServlet"); 
	        return;
	    }

	    String cardIdStr = request.getParameter("cardId");
	    String result = request.getParameter("result");

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

	                // セッションから取得したユーザーIDを使用して保存
	                dao.insertStudyRecord(loginUser.getId(), subjectId, minutes);
	                System.out.println("カード学習記録成功: " + minutes + "分");
	            }
	            
	        } catch (NumberFormatException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // 次の問題へ（StudyServletのdoGetへ）
	    response.sendRedirect("StudyServlet");
	}
	
	
}
