package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model_dto.Subject;

import common.DBUtil;

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
}
