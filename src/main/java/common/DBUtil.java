package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	// あなたのDBUtil.javaの上の部分をこのように書き換えます
	// 生のパスワードは消して、システムから読み込むようにします
	private static final String URL = System.getenv("DB_URL");
	private static final String USER = System.getenv("DB_USER");
	private static final String PASS = System.getenv("DB_PASS");

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
