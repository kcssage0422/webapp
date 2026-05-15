package model_dto;

import java.sql.Timestamp;

public class User {
	private int id;
	private String userName;
	private String password;
	private Timestamp create_date;
	public User(int id, String userName, String password, Timestamp create_date) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.create_date = create_date;
	}
	public User() {}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}
}
