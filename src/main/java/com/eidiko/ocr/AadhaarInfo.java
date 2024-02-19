package com.eidiko.ocr;

public class AadhaarInfo {

	private String type;
	private String aadhaarNumber;
	private String name;
	private AddressInfo address;
	private String mobile;
	private String enrolmentNo;
	private String vid;

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEnrolmentNo() {
		return enrolmentNo;
	}

	public void setEnrolmentNo(String enrolmentNo) {
		this.enrolmentNo = enrolmentNo;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Constructor, getters, and setters
}
