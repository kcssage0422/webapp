package model_dto;

import java.io.Serializable;
import java.sql.Date;

public class Flashcard implements Serializable {
	private int cardId;
	private int userId;
	private int subjectId;
	private String question;
	private String answer;
	private Date nextReviewDate;
	private int correctCount;
	private  boolean is_public;

	public Flashcard() {
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getNextReviewDate() {
		return nextReviewDate;
	}

	public void setNextReviewDate(Date nextReviewDate) {
		this.nextReviewDate = nextReviewDate;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	public boolean getIs_public() {
		return is_public;
	}

	public void setIs_public(boolean is_public2) {
		this.is_public = is_public2;
	}
}