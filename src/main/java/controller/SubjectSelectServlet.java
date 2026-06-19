package controller;

import java.io.IOException;
import java.util.List;

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
 * Servlet implementation class SubjectSelectServlet
 */
@WebServlet("/SubjectSelectServlet")
public class SubjectSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // 自分の科目リストを取得
        SubjectDAO sdao = new SubjectDAO();
        List<Subject> subjectList = sdao.findByUserId(currentUser.getId());
        
        // JSPへ渡す
        request.setAttribute("subjectList", subjectList);
        request.getRequestDispatcher("/WEB-INF/jsp/subject_select.jsp").forward(request, response);
    }
}
