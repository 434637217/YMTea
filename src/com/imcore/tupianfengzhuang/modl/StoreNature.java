package com.imcore.tupianfengzhuang.modl;

public class StoreNature {
	
	public long id;
	public String name;
	public String address;
	public String pictureUrl;
	public String phoneNumber;
	public double longitude;
	public double latitude;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	@Override
	public String toString() {
		return "StoreNature [id=" + id + ", name=" + name + ", longitude="
				+ longitude + ", latitude=" + latitude + "]";
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
