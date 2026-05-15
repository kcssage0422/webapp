package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model_dao.StudyPlanDAO;
import model_dto.StudyPlan;

@WebServlet("/PlanDeleteServlet")
public class PlanDeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // サイドバーのリンクから届いたIDを取得
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            
            // DAOを使って削除対象のデータを1件取得
            StudyPlanDAO dao = new StudyPlanDAO();
            StudyPlan plan = dao.findById(id);
            
            if (plan != null) {
                // データをセットして確認画面へ
                request.setAttribute("plan", plan);
                request.getRequestDispatcher("/WEB-INF/jsp/plan_confirm_delete.jsp").forward(request, response);
                return;
            }
        }
        
        // IDがない、またはデータが見つからない場合はカレンダーに戻す
        response.sendRedirect("CalendarServlet");
    }
}