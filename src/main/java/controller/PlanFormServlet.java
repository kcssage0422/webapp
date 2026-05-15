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

@WebServlet("/PlanFormServlet")
public class PlanFormServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id"); // 編集の時はIDが届く
        String dateStr = request.getParameter("date"); // 新規の時は日付が届く
        StudyPlan plan = new StudyPlan();
        if (dateStr == null || dateStr.trim().isEmpty() || "undefined".equals(dateStr)) {
            // 空で届いたら今日の日付にする（これで 500エラーを回避）
            dateStr = java.time.LocalDate.now().toString();
        }
        if (idStr != null) {
            // 【編集モード】
            int id = Integer.parseInt(idStr);
            plan = new StudyPlanDAO().findById(id);
            request.setAttribute("mode", "編集");
        } else {
            // 【新規モード】
            // カレンダーから届いた日付を初期値としてセット
            plan.setStartDate(Timestamp.valueOf(dateStr + " 09:00:00"));
            plan.setEndDate(Timestamp.valueOf(dateStr + " 10:00:00"));
            request.setAttribute("mode", "新規登録");
        }
        
        request.setAttribute("plan", plan);
        request.getRequestDispatcher("/WEB-INF/jsp/plan_form.jsp").forward(request, response);
    }
}