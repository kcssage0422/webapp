package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
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

/**
 * Servlet implementation class QuestionListServlet
 */
@WebServlet("/QuestionListServlet")
public class QuestionListServlet extends HttpServlet {
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
        FlashcardDAO fcdao = new FlashcardDAO();
        SubjectDAO sdao = new SubjectDAO();
        List<Flashcard> myCardList = fcdao.findMyCards(currentUser.getId());
        List<Map<String, Object>> publicCardList = fcdao.getPublicCardsWithSubjectName();
        List<Subject> subjectList = sdao.findByUserId(currentUser.getId());
        List<Subject> publicSubjectList = sdao.getPublicSubjects();
        request.setAttribute("myCardList", myCardList);
        request.setAttribute("publicCardList", publicCardList);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("subjectList", subjectList);
        request.setAttribute("publicSubjectList", publicSubjectList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/question_list.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
