package controller;

import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model_dao.StudyPlanDAO;
import model_dto.StudyPlan;
import model_dto.User;

/**
 * Servlet implementation class PlanActionServlet
 */
@WebServlet("/PlanActionServlet")
public class PlanActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
	    
	    // まず「何の操作か」を判定する
	    String action = request.getParameter("action");
	    String idStr = request.getParameter("id");
	    int id = (idStr == null || idStr.isEmpty()) ? 0 : Integer.parseInt(idStr);
	    
	    StudyPlanDAO dao = new StudyPlanDAO();

	    // 削除の場合は、日時変換をせずにすぐ実行して終わる
	    if ("delete".equals(action)) {
	        dao.delete(id);
	        response.sendRedirect("CalendarServlet");
	        return; // ここで処理を終了させる
	    }

	    // ここから下は「登録・編集」の時だけ実行される
	    
	    // 削除の時はここを通らないので、getParameter("startDate") が null でもエラーにならない
	    String startRaw = request.getParameter("startDate");
	    String endRaw = request.getParameter("endDate");
	    
	    // 安全にデータを取得して変換
	    String startDate = startRaw.replace("T", " ") + ":00";
	    String endDate = endRaw.replace("T", " ") + ":00";
	    
	    StudyPlan plan = new StudyPlan();
	    plan.setId(id);
	    plan.setTitle(request.getParameter("title"));
	    plan.setDescription(request.getParameter("description"));
	    plan.setStartDate(Timestamp.valueOf(startDate));
	    plan.setEndDate(Timestamp.valueOf(endDate));
	    plan.setColor(request.getParameter("color"));
	    
	    User user = (User) request.getSession().getAttribute("currentUser");
	    plan.setUserId(user.getId());

	    if (id == 0) {
	        dao.insert(plan);
	    } else {
	        dao.update(plan);
	    }

	    response.sendRedirect("CalendarServlet");
	}

}
