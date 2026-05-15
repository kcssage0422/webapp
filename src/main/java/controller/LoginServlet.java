package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import common.PasswordUtil;
import model_dao.UserDAO;
import model_dto.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
		dispatcher.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		// ユーザー名かパスワードのnullチェック
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			request.setAttribute("errorMsg", "ユーザー名かパスワードを入力してください。");
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
			return;
		}
		UserDAO dao = new UserDAO();
		// パスワードのハッシュ化
		String hashedPassword = PasswordUtil.hash(password);
		User user = dao.login(userName, hashedPassword);
		if (user != null) {
			// 成功！ セッションにユーザー情報をまるごと保存
			HttpSession session = request.getSession();
			session.setAttribute("currentUser", user);
			response.sendRedirect("HomeServlet");
		} else {
			// 失敗...
			request.setAttribute("errorMsg", "ユーザー名かパスワードが違います。");
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
		}
	}

}
