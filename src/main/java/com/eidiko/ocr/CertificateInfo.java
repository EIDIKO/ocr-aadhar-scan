package com.eidiko.ocr;

public class CertificateInfo {
	private String certId;
	private String type;
	private String certName;
	private String whoCertified;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public String getWhoCertified() {
		return whoCertified;
	}
	public void setWhoCertified(String whoCertified) {
		this.whoCertified = whoCertified;
	}
	public String getCertDate() {
		return certDate;
	}
	public void setCertDate(String certDate) {
		this.certDate = certDate;
	}
	private String certDate;

}
