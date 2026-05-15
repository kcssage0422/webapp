package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model_dao.SubjectDAO;
import model_dto.Subject;
import model_dto.User;

/**
 * Servlet implementation class SubjectAddServle
 */
@WebServlet("/SubjectAddServlet")
public class SubjectAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // 1. セッションからユーザー情報を取得
	    HttpSession session = request.getSession();
	    User currentUser = (User) session.getAttribute("currentUser");
	    
	    // 2. ログインしていない場合はログイン画面へリダイレクト
	    if (currentUser == null) {
	        response.sendRedirect("LoginServlet");
	        return;
	    }

	    // 3. 科目登録画面（JSP）へフォワード
	    request.getRequestDispatcher("WEB-INF/jsp/subject_add.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession();
	    User currentUser = (User) session.getAttribute("currentUser");
	    
	    if (currentUser == null) {
	        response.sendRedirect("LoginServlet");
	        return;
	    }

	    // フォーム値を取得
	    String name = request.getParameter("subjectName");
	    String color = request.getParameter("colorCode");

	    // DTOにセット
	    Subject subject = new Subject();
	    subject.setUserId(currentUser.getId());
	    subject.setName(name);
	    subject.setColorCode(color);

	    // DAOで保存
	    SubjectDAO dao = new SubjectDAO();
	    boolean success = dao.insert(subject);

	    if (success) {
	        // 登録できたら、暗記カード作成画面に戻してあげるのが親切です
	        response.sendRedirect("CardAddServlet");
	    } else {
	        request.setAttribute("error", "科目の登録に失敗しました。");
	        request.getRequestDispatcher("WEB-INF/jsp/subject_add.jsp").forward(request, response);
	    }
	}

}
