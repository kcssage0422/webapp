package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBUtil;
import model_dto.User;

public class UserDAO {
	public boolean registerUser(User user) {
		if (isUsernameExists(user.getUserName())) {
			return false;
		}
		String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());

			int result = pstmt.executeUpdate();

			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isUsernameExists(String username) {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public User login(String username, String hashedpassword) {
		String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
		User user = null; // 見つからなかった時のために最初はnull
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, hashedpassword);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					user = new User();
					user.setId(rs.getInt("user_id"));
					user.setUserName(rs.getString("username"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}
