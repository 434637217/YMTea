package com.imcore.tupianfengzhuang.modl;

public class UserDetails {
	
	public long id;
	public long userId;
	public String name;
	public String gender;
	public String province;
	public String city;
	public String avatarUrl;
	public String jobTitle;
	public String userName;
	public String company;
	public long grade;
	public long age;
	private long totalPoint;
	public long getTotalPoint() {
		return totalPoint;
	}
	public void setTotalPoint(long totalPoint) {
		this.totalPoint = totalPoint;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public long getGrade() {
		return grade;
	}
	public void setGrade(long grade) {
		this.grade = grade;
	}
	public long getAge() {
		return age;
	}
	public void setAge(long age) {
		this.age = age;
	}
}
