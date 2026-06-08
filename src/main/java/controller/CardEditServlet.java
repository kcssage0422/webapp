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
import model_dto.Subject;
import model_dto.User;

@WebServlet("/CardEditServlet")
public class CardEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 💡 1. JSPのフォーム（POST）からリクエストをここで受け取ります
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // POSTで届いたデータをそのまま下のdoGet処理へ丸投げします
        doGet(request, response);
    }

    // 💡 2. 実際の画面表示（GET/内部処理）はここで行います
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = currentUser.getId();
        
        // フォームから送られてきたカードIDを取得
        String cardIdStr = request.getParameter("cardId");
        if (cardIdStr == null || cardIdStr.isEmpty()) {
            response.sendRedirect("QuestionListServlet");
            return;
        }
        
        int cardId = Integer.parseInt(cardIdStr);

        FlashcardDAO cardDao = new FlashcardDAO();
        Flashcard card = cardDao.getCardByIdAndUserId(cardId, userId);

        // 🔒 最重要セキュリティチェック（他人のカードを勝手に編集できないようにガード）
        if (card != null && card.getUserId() == userId) {
            
            // データをリクエストにセット
            request.setAttribute("card", card);

            SubjectDAO subjectDao = new SubjectDAO(); 
            List<Subject> subjectList = subjectDao.findByUserId(userId); 
            request.setAttribute("subjectList", subjectList);
            
            // ========================================================
            // ★ ここが一番の修正ポイント！
            // sendRedirect ではなく、必ず「forward」でJSPを呼び出します！
            // ========================================================
            request.getRequestDispatcher("/WEB-INF/jsp/card_update.jsp").forward(request, response);
            
        } else {
            // 他人のカードIDが指定された場合は、何もせずに一覧へ戻す
            response.sendRedirect("QuestionListServlet");
        }
    }
}