package model_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DBUtil;
import model_dto.Flashcard;

public class FlashcardDAO {
	public boolean insert(Flashcard card) {
        String sql = "INSERT INTO flashcards (user_id, subject_id, question, answer, next_review_date, correct_count) "
                   + "VALUES (?, ?, ?, ?, CURRENT_DATE, 0)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, card.getUserId());
            pstmt.setInt(2, card.getSubjectId());
            pstmt.setString(3, card.getQuestion());
            pstmt.setString(4, card.getAnswer());
            
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
}
