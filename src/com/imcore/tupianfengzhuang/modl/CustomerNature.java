package com.imcore.tupianfengzhuang.modl;

public class CustomerNature {
	public long id;
	public String userName;
	private long phoneNumber;
	public String salt;
	public long status;
	public String device;
	public long identity;
	public long newOrderNumber;
	public boolean online;
	public long pushMessage;
	public long getPushMessage() {
		return pushMessage;
	}
	public void setPushMessage(long pushMessage) {
		this.pushMessage = pushMessage;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public long getIdentity() {
		return identity;
	}
	public void setIdentity(long identity) {
		this.identity = identity;
	}
	public long getNewOrderNumber() {
		return newOrderNumber;
	}
	public void setNewOrderNumber(long newOrderNumber) {
		this.newOrderNumber = newOrderNumber;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
}
