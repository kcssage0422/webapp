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

@WebServlet("/MockExamServlet")
public class MockExamServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    
	    // 1. パラメータから取得を試みる
	    String subjectIdStr = request.getParameter("subjectId");
	    int subjectId;

	    if (subjectIdStr != null) {
	        // パラメータがあれば数値変換してセッションに保存
	        subjectId = Integer.parseInt(subjectIdStr);
	        session.setAttribute("currentSubjectId", subjectId);
	    } else {
	        // パラメータがなければセッションから復元
	        Integer savedId = (Integer) session.getAttribute("currentSubjectId");
	        if (savedId == null) {
	            // どこにもIDがないならホームへ
	            response.sendRedirect("HomeServlet");
	            return;
	        }
	        subjectId = savedId;
	    }

	    // 初回アクセス（または再挑戦）の場合のみリストを再取得
	    // ※「正解数」などがリセットされるよう、初回判定の条件を調整します
	    if (request.getParameter("subjectId") != null) {
	        int userId = ((User)session.getAttribute("currentUser")).getId();
	        FlashcardDAO dao = new FlashcardDAO();
	        List<Flashcard> mockList = dao.getAllQuestionsBySubject(userId, subjectId);
	        
	        session.setAttribute("mockList", mockList);
	        session.setAttribute("currentIndex", 0);
	        session.setAttribute("score", 0);
	    }
	    
	    // 以下、現在の問題を表示する処理...
	    List<Flashcard> mockList = (List<Flashcard>) session.getAttribute("mockList");
	    int index = (int) session.getAttribute("currentIndex");
	    
	    if (mockList != null && index < mockList.size()) {
	        request.setAttribute("card", mockList.get(index));
	        request.getRequestDispatcher("/WEB-INF/jsp/mockExam.jsp").forward(request, response);
	    } else {
	        // リストがない、または全問終了なら結果画面へ
	        request.getRequestDispatcher("/WEB-INF/jsp/mockResult.jsp").forward(request, response);
	    }
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int score = (int) session.getAttribute("score");
        String result = request.getParameter("result"); // "correct" か "incorrect" が来る
        
        // 正解ならスコア加算
        if ("correct".equals(result)) {
            session.setAttribute("score", score + 1);
        }
        
        // 次の問題へインデックスを進める
        int index = (int) session.getAttribute("currentIndex");
        session.setAttribute("currentIndex", index + 1);
        
        response.sendRedirect("MockExamServlet");
    }
}