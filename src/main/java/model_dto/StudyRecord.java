package model_dto;

import java.sql.Date;
import java.sql.Timestamp;

public class StudyRecord {
	int id;
	int userId;
	int subjectId;
	Date studyDate;
	int actualminutes;
	Timestamp createdAt;
	
	public StudyRecord() {};
	public StudyRecord(int id, int userId, int subjectId, Date studyDate, int actualminutes, Timestamp createdAt) {
		this.id = id;
		this.userId = userId;
		this.subjectId = subjectId;
		this.studyDate = studyDate;
		this.actualminutes = actualminutes;
		this.createdAt = createdAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getStudyDate() {
		return studyDate;
	}
	public void setStudyDate(Date studyDate) {
		this.studyDate = studyDate;
	}
	public int getActualminutes() {
		return actualminutes;
	}
	public void setActualminutes(int actualminutes) {
		this.actualminutes = actualminutes;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
}
