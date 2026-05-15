package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class StudyTImerServlet
 */
@WebServlet("/StudyTimerServlet")
public class StudyTimerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    

	/**
	 * @throws IOException 
	 * @throws ServletException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    HttpSession session = request.getSession();
	    if (session.getAttribute("currentUser") == null) {
	        // ログインしていないならログイン画面へ飛ばす
	        response.sendRedirect("LoginServlet");
	        return;
	    }
	    // ログインしていればタイマー画面を表示
	    request.getRequestDispatcher("WEB-INF/jsp/studyTimer.jsp").forward(request, response);
	}

	

}
