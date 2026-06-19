package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.DBUtil;
import model_dto.Flashcard;

public class FlashcardDAO {
	public boolean insert(Flashcard card) {
        String sql = "INSERT INTO flashcards (user_id, subject_id, question, answer, next_review_date, correct_count,is_public) "
                   + "VALUES (?, ?, ?, ?, CURRENT_DATE,0, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, card.getUserId());
            pstmt.setInt(2, card.getSubjectId());
            pstmt.setString(3, card.getQuestion());
            pstmt.setString(4, card.getAnswer());
            pstmt.setBoolean(5, card.getIs_public());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	public List<Flashcard> findCardsForReview(int userId) {
        List<Flashcard> list = new ArrayList<>();
        // 「今日以前」が復習予定日のものを取得
        String sql = "SELECT * FROM flashcards WHERE user_id = ? AND next_review_date <= CURRENT_DATE";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Flashcard card = new Flashcard();
                    card.setCardId(rs.getInt("card_id"));
                    card.setQuestion(rs.getString("question"));
                    card.setAnswer(rs.getString("answer"));
                    card.setCorrectCount(rs.getInt("correct_count"));
                    card.setSubjectId(rs.getInt("subject_id"));
                    // 必要に応じて他のフィールドもセット
                    list.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
	public void updateReviewResult(int cardId, boolean isCorrect) {
		String sql;
	    if (isCorrect) {
	        // 正解：復習日を3日後に更新（MySQLの例：INTERVAL 3 DAY）
	        sql = "UPDATE flashcards SET next_review_date = DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY) WHERE card_id = ?";
	    } else {
	        // 不正解：また明日（または今日）復習させる
	        sql = "UPDATE flashcards SET next_review_date = DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY) WHERE card_id = ?";
	    }

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, cardId);
	        pstmt.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public void insertStudyRecord(int userId, int subjectId, int minutes) {
	    // studyDateはSQLの CURRENT_DATE で今日の日付を自動入力
	    String sql = "INSERT INTO study_records (user_id, subject_id, study_date, actual_minutes) VALUES (?, ?, CURRENT_DATE, ?)";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	    	conn.setAutoCommit(false);
	        pstmt.setInt(1, userId);
	        pstmt.setInt(2, subjectId);
	        pstmt.setInt(3, minutes);
	        
	        pstmt.executeUpdate();
	        conn.commit();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public List<Flashcard> findMyCards(int currentUser) {
        List<Flashcard> list = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, currentUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Flashcard card = new Flashcard();
                    card.setCardId(rs.getInt("card_id"));
                    card.setQuestion(rs.getString("question"));
                    card.setAnswer(rs.getString("answer"));
                    card.setCorrectCount(rs.getInt("correct_count"));
                    card.setSubjectId(rs.getInt("subject_id"));
                    card.setIs_public(rs.getBoolean("is_public"));
                    // 必要に応じて他のフィールドもセット
                    list.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
	public List<Flashcard> findPublicCards(int userId) {
        List<Flashcard> list = new ArrayList<>();
        // 「今日以前」が復習予定日のものを取得
        String sql = "SELECT * FROM flashcards WHERE is_public = 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Flashcard card = new Flashcard();
                    card.setCardId(rs.getInt("card_id"));
                    card.setQuestion(rs.getString("question"));
                    card.setAnswer(rs.getString("answer"));
                    card.setCorrectCount(rs.getInt("correct_count"));
                    card.setSubjectId(rs.getInt("subject_id"));
                    // 必要に応じて他のフィールドもセット
                    list.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
	public Flashcard getCardByIdAndUserId(int cardId, int userId) {
	    Flashcard card = null;

	    String sql = "SELECT card_id, user_id, subject_id, question, answer, correct_count, is_public "
	               + "FROM flashcards "
	               + "WHERE card_id = ? AND user_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           
	        pstmt.setInt(1, cardId);
	        pstmt.setInt(2, userId);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            // 💡 改善ポイント1：1件のみの取得なので while ではなく if に変更します
	            if (rs.next()) {
	                card = new Flashcard();
	                card.setCardId(rs.getInt("card_id"));
	                card.setUserId(rs.getInt("user_id")); // 💡念のためuserIdもセットしておくと安全です
	                card.setSubjectId(rs.getInt("subject_id"));
	                card.setQuestion(rs.getString("question"));
	                card.setAnswer(rs.getString("answer"));
	                card.setCorrectCount(rs.getInt("correct_count"));
	                
	                // ★ 改善ポイント2：ここが漏れていました！
	                // DBの「is_public（0か1）」を Javaの boolean型（true/false）として取得し、Modelにセットします
	                // ※Model側のメソッド名が「setPublic」か「setIs_public」か、お使いの環境に合わせて微調整してください
	                card.setIs_public(rs.getBoolean("is_public")); 
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return card;
	}
	public boolean updateCard(Flashcard card) {
		// TODO 自動生成されたメソッド・スタブ
		boolean success = false;
	    
	    // WHERE句に card_id だけでなく user_id も指定することで鉄壁になります
	    String sql = "UPDATE flashcards "
	               + "SET subject_id = ?, question = ?, answer = ?, is_public = ? "
	               + "WHERE card_id = ? AND user_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, card.getSubjectId());
	        pstmt.setString(2, card.getQuestion());
	        pstmt.setString(3, card.getAnswer());
	        
	        pstmt.setBoolean(4, card.getIs_public()); 
	        
	        pstmt.setInt(5, card.getCardId());
	        pstmt.setInt(6, card.getUserId());

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            success = true;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return success;
	}
	public boolean deleteCard(int cardId, int userId) {
	    boolean success = false;
	    
	    // 💡 鉄壁ガード：card_id と user_id が両方一致するデータのみを削除対象にします
	    String sql = "DELETE FROM flashcards WHERE card_id = ? AND user_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        // プレースホルダーに値をセット
	        pstmt.setInt(1, cardId);
	        pstmt.setInt(2, userId);

	        // SQLを実行し、影響を受けた行数（削除された行数）を取得
	        int rowsAffected = pstmt.executeUpdate();
	        
	        // 1行以上削除されていれば成功とみなす
	        if (rowsAffected > 0) {
	            success = true;
	        }

	    } catch (SQLException e) {
	        // エラーが発生した場合はスタックトレースを出力
	        e.printStackTrace();
	    }
	    
	    return success;
	}
	public boolean updateCardsPublicStatusBySubject(int subjectId, boolean isPublic) {
	    boolean success = false;
	    // 💡 SQL: その科目のIDを持つカードの is_public をすべて書き換える
	    String sql = "UPDATE flashcards SET is_public = ? WHERE subject_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        // MySQLのTINYINTに対応（trueなら1、falseなら0）
	        pstmt.setBoolean(1, isPublic);
	        pstmt.setInt(2, subjectId);

	        pstmt.executeUpdate();
	        success = true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return success;
	}
	public List<Map<String, Object>> getPublicCardsWithSubjectName() {
	    List<Map<String, Object>> list = new ArrayList<>();
	    
	    // 💡 SQLで subjects テーブルと結合して科目名（s.name）を一緒に取得
	    String sql = "SELECT f.*, s.name AS subject_name FROM flashcards f "
	               + "JOIN subjects s ON f.subject_id = s.subject_id WHERE f.is_public = true";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            // ① いつも通り Flashcard オブジェクトを作る（クラスの変更は不要！）
	            Flashcard q = new Flashcard();
	            q.setCardId(rs.getInt("card_id"));
	            q.setSubjectId(rs.getInt("subject_id"));
	            q.setQuestion(rs.getString("question"));
	            q.setAnswer(rs.getString("answer"));
	            q.setIs_public(rs.getBoolean("is_public"));

	            // ② 一時的な「マップ（入れ物）」を作り、問題と科目名をセットにする
	            Map<String, Object> cardMap = new HashMap<>();
	            cardMap.put("cardData", q);                       // Flashcardオブジェクト
	            cardMap.put("subjectName", rs.getString("subject_name")); // 科目名（文字列）

	            list.add(cardMap);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	// 💡 公開問題ダウンロード用：ユーザーIDに関係なく、カードIDだけで1件取得するメソッド
	public Flashcard getCardById(int cardId) {
	    Flashcard card = null;

	    // 🔒 誰が作ったカードでも、カードIDが一致すれば取得できるようにWHERE句を card_id だけにします
	    String sql = "SELECT card_id, user_id, subject_id, question, answer, correct_count, is_public "
	               + "FROM flashcards "
	               + "WHERE card_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	           
	        pstmt.setInt(1, cardId);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                card = new Flashcard();
	                card.setCardId(rs.getInt("card_id"));
	                card.setUserId(rs.getInt("user_id")); 
	                card.setSubjectId(rs.getInt("subject_id"));
	                card.setQuestion(rs.getString("question"));
	                card.setAnswer(rs.getString("answer"));
	                card.setCorrectCount(rs.getInt("correct_count"));
	                card.setIs_public(rs.getBoolean("is_public")); 
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return card;
	}
	public List<Flashcard> getDueQuestionsBySubject(int userId, int subjectId) {
	    List<Flashcard> list = new ArrayList<>();
	    // SQL: ユーザーID かつ 科目ID かつ 今日が復習日の問題を抽出
	    String sql = "SELECT * FROM flashcards " +
	                 "WHERE user_id = ? AND subject_id = ? AND next_review_date <= CURRENT_DATE";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, userId);
	        pstmt.setInt(2, subjectId); // ここで科目IDを絞り込み
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Flashcard card = new Flashcard();
	                card.setCardId(rs.getInt("card_id"));
	                card.setQuestion(rs.getString("question"));
	                card.setAnswer(rs.getString("answer"));
	                // ... 他のフィールドセット
	                list.add(card);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	public List<Flashcard> getAllQuestionsBySubject(int userId, int subjectId) {
	    List<Flashcard> list = new ArrayList<>();
	    // 💡 条件から next_review_date を外すことで全件取得
	    String sql = "SELECT * FROM flashcards WHERE user_id = ? AND subject_id = ? ORDER BY RAND()";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, userId);
	        pstmt.setInt(2, subjectId);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Flashcard card = new Flashcard();
	                card.setCardId(rs.getInt("card_id"));
	                card.setQuestion(rs.getString("question"));
	                card.setAnswer(rs.getString("answer"));
	                list.add(card);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
