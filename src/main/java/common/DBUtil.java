package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static final String URL = "jdbc:mariadb://localhost:3306/study_app_db";
    private static final String USER = "root";
    private static final String PASS = "mysql";

    /**
     * データベースへの接続を取得します。
     * @return Connectionオブジェクト
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            // JDBCドライバのロード（古いTomcatや環境によって必要な場合があります）
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBCドライバが見つかりません。");
        }
        
        // 接続を確立して返す
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
