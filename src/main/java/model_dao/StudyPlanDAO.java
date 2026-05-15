package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DBUtil;
import model_dto.StudyPlan;

public class StudyPlanDAO {
	public boolean insert(StudyPlan plan) {
		String sql = "INSERT INTO study_plans (user_id, title, description, start_date, end_date, color) VALUES (?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, plan.getUserId());
            pstmt.setString(2, plan.getTitle());
            pstmt.setString(3, plan.getDescription());
            pstmt.setTimestamp(4, plan.getStartDate());
            pstmt.setTimestamp(5, plan.getEndDate());
            pstmt.setString(6, plan.getColor());
            
            return pstmt.executeUpdate() > 0;
		}catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	public boolean delete(int id) {
	    String sql = "DELETE FROM study_plans WHERE id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean update(StudyPlan plan) {
	    String sql = "UPDATE study_plans SET title = ?, description = ?, start_date = ?, end_date = ?, color = ? WHERE id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, plan.getTitle());
	        pstmt.setString(2, plan.getDescription());
	        pstmt.setTimestamp(3, plan.getStartDate());
	        pstmt.setTimestamp(4, plan.getEndDate());
	        pstmt.setString(5, plan.getColor());
	        pstmt.setInt(6, plan.getId());

	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public StudyPlan findById(int id) {
	    String sql = "SELECT * FROM study_plans WHERE id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                StudyPlan p = new StudyPlan();
	                p.setId(rs.getInt("id"));
	                p.setTitle(rs.getString("title"));
	                p.setDescription(rs.getString("description"));
	                p.setStartDate(rs.getTimestamp("start_date"));
	                p.setEndDate(rs.getTimestamp("end_date"));
	                p.setColor(rs.getString("color"));
	                return p;
	            }
	        }
	    } catch (SQLException e) { e.printStackTrace(); }
	    return null;
	}
	public List<StudyPlan> findAllByUserId(int userId) {
        List<StudyPlan> list = new ArrayList<>();
        String sql = "SELECT * FROM study_plans WHERE user_id = ? ORDER BY start_date ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudyPlan p = new StudyPlan();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setTitle(rs.getString("title"));
                    p.setDescription(rs.getString("description"));
                    p.setStartDate(rs.getTimestamp("start_date"));
                    p.setEndDate(rs.getTimestamp("end_date"));
                    p.setColor(rs.getString("color"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
	public List<StudyPlan> findByDate(int userId) {
		List<StudyPlan> list = new ArrayList<>();
	    String sql = "SELECT * FROM study_plans WHERE user_id = ? AND DATE(start_date) = CURRENT_DATE";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, userId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	           while (rs.next()) {
	                StudyPlan p = new StudyPlan();
	                p.setId(rs.getInt("id"));
	                p.setTitle(rs.getString("title"));
	                p.setDescription(rs.getString("description"));
	                p.setStartDate(rs.getTimestamp("start_date"));
	                p.setEndDate(rs.getTimestamp("end_date"));
	                p.setColor(rs.getString("color"));
	                list.add(p);
	                
	            }
	        }
	    } catch (SQLException e) { e.printStackTrace(); }
	    return list;
	}
}
