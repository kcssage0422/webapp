package model_dto;

import java.io.Serializable;

public class Subject implements Serializable {
	private static final long serialVersionUID = 1L;

	private int subjectId;
	private int userId;
	private String name;
	private String colorCode;
	private Boolean is_public;
	// コンストラクタ
	public Subject() {
	}

	// 全フィールドをセットするコンストラクタ（任意）
	public Subject(int subjectId, int userId, String name, String colorCode) {
		this.subjectId = subjectId;
		this.userId = userId;
		this.name = name;
		this.colorCode = colorCode;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Boolean getIs_public() {
		return is_public;
	}

	public void setIs_public(Boolean is_public) {
		this.is_public = is_public;
	}
	
}