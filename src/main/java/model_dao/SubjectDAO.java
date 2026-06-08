package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DBUtil;
import model_dto.Subject;

public class SubjectDAO {

	/**
	 * 特定のユーザーが登録した科目一覧を取得する
	 */
	public List<Subject> findByUserId(int userId) {
		List<Subject> list = new ArrayList<>();
		String sql = "SELECT * FROM subjects WHERE user_id = ? ORDER BY name ASC";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Subject subject = new Subject();
					subject.setSubjectId(rs.getInt("subject_id"));
					subject.setUserId(rs.getInt("user_id"));
					subject.setName(rs.getString("name"));
					subject.setColorCode(rs.getString("color_code"));
					subject.setIs_public(rs.getBoolean("is_public"));
					list.add(subject);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 新しい科目を登録する
	 */
	public boolean insert(Subject subject) {
		String sql = "INSERT INTO subjects (user_id, name, color_code) VALUES (?, ?, ?)";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, subject.getUserId());
			pstmt.setString(2, subject.getName());
			pstmt.setString(3, subject.getColorCode());

			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateSubject(int subjectId, String name, boolean isPublic) {
	    boolean success = false;
	    
	    // 💡 SQL: 指定されたIDの科目の名前(name)と公開フラグ(is_public)を更新する
	    String sql = "UPDATE subjects SET name = ?, is_public = ? WHERE subject_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        // 1番目の「?」に新しい科目名（文字列）をセット
	        pstmt.setString(1, name);
	        
	        // 2番目の「?」に公開フラグをセット（MySQL側がTINYINTでも、setBooleanで自動的に1か0に変換されます）
	        pstmt.setBoolean(2, isPublic);
	        
	        // 3番目の「?」に条件となる科目IDをセット
	        pstmt.setInt(3, subjectId);

	        // クエリを実行し、影響を受けた行数が1行以上あれば成功とみなす
	        int rowsUpdated = pstmt.executeUpdate();
	        if (rowsUpdated > 0) {
	            success = true;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return success;
	}

	public Subject getSubjectById(int subjectId) {
		// TODO 自動生成されたメソッド・スタブ
		Subject subject = null;
	    String sql = "SELECT subject_id, user_id, name, color_code, is_public FROM subjects WHERE subject_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, subjectId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                subject = new Subject();
	                subject.setSubjectId(rs.getInt("subject_id"));
	                subject.setUserId(rs.getInt("user_id"));
	                subject.setName(rs.getString("name"));
	                subject.setColorCode(rs.getString("color_code"));
	                
	                // 💡 MySQLのTINYINTをJavaのBooleanにセット（null対策のため、ここもモデルに合わせて連動）
	                subject.setIs_public(rs.getBoolean("is_public"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return subject;
	}
	public List<Subject> getPublicSubjects() {
	    List<Subject> list = new ArrayList<>();
	    // 💡 公開状態になっている科目をすべて取得するSQL
	    String sql = "SELECT * FROM subjects WHERE is_public = true";
	    
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {
	        while (rs.next()) {
	            Subject s = new Subject();
	            s.setSubjectId(rs.getInt("subject_id"));
	            s.setName(rs.getString("name"));
	            s.setIs_public(rs.getBoolean("is_public"));
	            list.add(s);
	        }
	    } catch (SQLException e) { e.printStackTrace(); }
	    return list;
	}
}
