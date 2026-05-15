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
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
		dispatcher.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		// ユーザー名とパスワードの取得
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String checkPassword = request.getParameter("checkPassword");
		// ユーザー名かパスワードのnullチェック
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			request.setAttribute("errorMsg", "ユーザー名とパスワードを入力してください。");
			request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
			return;
		}
		UserDAO dao = new UserDAO();
		if (dao.isUsernameExists(userName)) {
			// エラーがある場合はリクエストスコープにメッセージを入れて元の画面へ
			request.setAttribute("errorMsg", "そのユーザー名は既に使用されています。");
			request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
			return;
		}
		if (!password.equals(checkPassword)) {
			// エラーがある場合はリクエストスコープにメッセージを入れて元の画面へ
			request.setAttribute("errorMsg", "パスワードが違います。");
			request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
			return;
		}
		// パスワードのハッシュ化
		String hashedPassword = PasswordUtil.hash(password);
		// DTOにセットして登録実行
		User newUser = new User();
		newUser.setUserName(userName);
		newUser.setPassword(hashedPassword);

		boolean isSuccess = dao.registerUser(newUser);
		if (isSuccess) {
			// 1. セッションを開始
		    HttpSession session = request.getSession();
		    
		    // 2. 登録したユーザー情報を「ログイン済み」としてセット
		    // (newUserは登録に使ったDTOオブジェクト)
		    session.setAttribute("currentUser", newUser);
			// 成功：完了画面へリダイレクト（二重送信防止）
			response.sendRedirect(request.getContextPath() + "/HomeServlet");
		} else {
			// 失敗：エラーメッセージを出して入力画面へ戻る
			request.setAttribute("errorMsg", "登録に失敗しました。システム管理者へ連絡してください。");
			request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
		}
	}

}
