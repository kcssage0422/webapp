package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import common.DBUtil;
import model_dto.StudyRecord;

public class StudyRecordDAO {
	public void insert(StudyRecord record) {
		
	}
	public Map<String, Integer> findPastWeekStudyTime(int userId) {
		Map<String, Integer> reportMap = new LinkedHashMap<>();
        String sql = "SELECT \n"
        		+ "    study_date, \n"
        		+ "    SUM(actual_minutes) AS daily_total\n"
        		+ "FROM \n"
        		+ "    study_records\n"
        		+ "WHERE \n"
        		+ "    user_id = ? \n"
        		+ "    AND study_date BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 6 DAY) AND CURRENT_DATE\n"
        		+ "GROUP BY \n"
        		+ "    study_date\n"
        		+ "ORDER BY \n"
        		+ "    study_date ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	String date = rs.getString("study_date");
                    int total = rs.getInt("daily_total");
                    
                    reportMap.put(date, total);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportMap;
	}
}
