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
import model_dto.Flashcard;
import model_dto.Subject;
import model_dto.User;

@WebServlet("/CardDownloadServlet")
public class CardDownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int currentUserId = currentUser.getId();
        String cardIdStr = request.getParameter("cardId");
        
        if (cardIdStr != null && !cardIdStr.isEmpty()) {
            int targetCardId = Integer.parseInt(cardIdStr);
            
            FlashcardDAO cardDao = new FlashcardDAO();
            Flashcard originalCard = cardDao.getCardById(targetCardId); 
            
            if (originalCard != null && originalCard.getIs_public()) {
                
                int mySubjectId = 0;
                SubjectDAO subjectDao = new SubjectDAO();
                Subject originalSubject = subjectDao.getSubjectById(originalCard.getSubjectId()); 
                
                if (originalSubject != null) {
                    String targetSubjectName = originalSubject.getName(); // 例: "test"
                    
                    // 1. 自分の科目リストから同じ名前のものを探す
                    java.util.List<Subject> mySubjects = subjectDao.findByUserId(currentUserId);
                    if (mySubjects != null) {
                        for (Subject s : mySubjects) {
                            if (s.getName() != null && s.getName().trim().equalsIgnoreCase(targetSubjectName.trim())) {
                                mySubjectId = s.getSubjectId(); // 見つかった！
                                break;
                            }
                        }
                    }
                    
                    // 💡 2. 【ここが超重要改善】もし自分の中に同じ名前の科目がなかったら、その場で自動で作る！
                    if (mySubjectId == 0 && targetSubjectName != null && !targetSubjectName.isEmpty()) {
                        Subject newSubject = new Subject();
                        newSubject.setUserId(currentUserId);
                        newSubject.setName(targetSubjectName.trim());
                        newSubject.setIs_public(false); // 自分用なのでまずは非公開
                        newSubject.setColorCode("#3498db"); // デフォルトの青色など
                        
                        // データベースに科目を新規登録
                        subjectDao.insert(newSubject); 
                        // ↑ ※お持ちの科目登録メソッド名（insert や insertSubject など）に合わせて調整してください
                        
                        // 登録直後の自動採番された最新の科目IDを、名前を元に再取得する
                        java.util.List<Subject> updatedSubjects = subjectDao.findByUserId(currentUserId);
                        if (updatedSubjects != null) {
                            for (Subject s : updatedSubjects) {
                                if (s.getName() != null && s.getName().trim().equalsIgnoreCase(targetSubjectName.trim())) {
                                    mySubjectId = s.getSubjectId();
                                    break;
                                }
                            }
                        }
                    }
                }

                // 3. 新しいカードを複製して保存
                Flashcard newCard = new Flashcard();
                newCard.setUserId(currentUserId); 
                newCard.setQuestion(originalCard.getQuestion());
                newCard.setAnswer(originalCard.getAnswer());
                
                // 確定した自分用の科目IDをセット
                if (mySubjectId != 0) {
                    newCard.setSubjectId(mySubjectId);
                } else {
                    newCard.setSubjectId(originalCard.getSubjectId()); 
                }
                
                newCard.setIs_public(false); 
                cardDao.insert(newCard); // 保存実行！
            }
        }

        // 4. 問題一覧へ戻る
        response.sendRedirect("QuestionListServlet");
    }
}