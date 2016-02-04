package com.spring.akn.entities.frmApiDoc;

import java.util.Date;

public class FrmUserUpdate {

	private String userId;
	private String username;
	private String gender;
	private Date dateOfBirth;
	private String phoneNumber;
	private String userImageUrl;
	private String universityId;
	private String departmentId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getUniversityId() {
		return universityId;
	}

	public void setUniversityId(String universityId) {
		this.universityId = universityId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	
	@Override
	public String toString() {
		return "FrmUserUpdate [userId=" + userId + ", username=" + username + ", gender=" + gender + ", dateOfBirth="
				+ dateOfBirth + ", phoneNumber=" + phoneNumber + ", userImageUrl=" + userImageUrl + ", universityId="
				+ universityId + ", departmentId=" + departmentId + "]";
	}
}
