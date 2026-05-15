package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model_dao.StudyPlanDAO;
import model_dao.StudyRecordDAO;
import model_dto.StudyPlan;
import model_dto.User;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
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
        
        int userId = currentUser.getId();
        StudyPlanDAO SPdao = new StudyPlanDAO();
        StudyRecordDAO SRdao = new StudyRecordDAO();
        
        // DAOからデータを取得
        Map<String, Integer> rawData = SRdao.findPastWeekStudyTime(userId);
        		
        // グラフ用の「確定7日間リスト」を作る
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        
        for (int i = 6; i >= 0; i--) {
            // 今日から i 日前の日付文字列を作る (例: "2026-04-15")
            String dateKey = LocalDate.now().minusDays(i).toString();
            
            labels.add(dateKey); // 横軸に追加
            
            // Mapにデータがあればその値を、なければ 0 を入れる
            values.add(rawData.getOrDefault(dateKey, 0));
        }

        // JSPへ渡す
        request.setAttribute("chartLabels", labels);
        request.setAttribute("chartValues", values);
        
        
        List<StudyPlan> list = SPdao.findByDate(userId);
        request.setAttribute("todayPlans", list);
        request.getRequestDispatcher("home.jsp").forward(request, response);
	}

}
