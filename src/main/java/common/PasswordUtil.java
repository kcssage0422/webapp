package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	/**
     * 文字列をSHA-256でハッシュ化し、16進数の文字列として返します。
     * @param password 生のパスワード
     * @return ハッシュ化された文字列
     */
    public static String hash(String password) {
        try {
            // 1. SHA-256アルゴリズムのインスタンスを取得
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // 2. パスワードをバイト配列に変換してハッシュ計算を実行
            byte[] hashBytes = md.digest(password.getBytes());
            
            // 3. バイト配列を16進数の文字列に変換
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                // %02x は「2桁の16進数」という意味です
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            // SHA-256はJavaの標準なので通常はここには来ませんが、型通りのエラー処理です
            throw new RuntimeException("ハッシュ化アルゴリズムが見つかりません", e);
        }
    }
}
