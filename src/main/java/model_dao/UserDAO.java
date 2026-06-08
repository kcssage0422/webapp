package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBUtil;
import model_dto.User;

public class UserDAO {
	public boolean registerUser(User user) {
	    // 💡 SQL文は、元々正常に動いていた時の記述（正しい列名）のままにしてください
	    String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)"; 
	    boolean success = false;

	    // 💡 1. 接続を取得する
	    try (java.sql.Connection conn = DBUtil.getConnection(); // ※お使いの接続管理クラス名にしてください
	         // 💡 2. 【超重要】ここで確実に Statement.RETURN_GENERATED_KEYS を指定します
	         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
	        
	        // 💡 3. 値をセット（お使いのDTOのゲッター名に合わせてください）
	        pstmt.setString(1, user.getUserName());
	        pstmt.setString(2, user.getPassword());
	        
	        // SQLを実行
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows > 0) {
	            success = true;
	            
	            // 💡 4. データベース側で生成された自動採番のID（主キー）を回収する
	            try (java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    // 自動生成されたID（1列目の値）を、引数で渡された user オブジェクトに書き戻す
	                    // ※ お使いのUser DTOのIDセットメソッド名（setId や setUserId など）に合わせてください
	                    user.setId(generatedKeys.getInt(1)); 
	                }
	            }
	        }
	    } catch (java.sql.SQLException e) {
	        e.printStackTrace();
	    }
	    return success;
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
