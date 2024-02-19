package com.eidiko.ocr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.sourceforge.tess4j.TesseractException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;

import org.bson.Document;

@RestController
@RequestMapping("/api/aadhaar")
public class AadhaarController {

	@Autowired
	private AadhaarService aadhaarService;
	
	@Autowired
	private MongoDBService mongoDBService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/extract-info")
	public ResponseEntity<Object> extractAadhaarInfo(@RequestParam("filePath") String filePath)
			throws TesseractException, MessagingException {
		try {
			emailService.downloadAttachments();
			Object info = aadhaarService.extractAadhaarInfo(filePath);
			Document document = new Document();

			if (info instanceof AadhaarInfo) {
				AadhaarInfo aadhaarInfo = (AadhaarInfo) info;

				document.append("type", aadhaarInfo.getType()).append("aadhaarNumber", aadhaarInfo.getAadhaarNumber())
						.append("name", aadhaarInfo.getName()).append("vid", aadhaarInfo.getVid())
						.append("address",
								new Document("state", aadhaarInfo.getAddress().getState())
										.append("district", aadhaarInfo.getAddress().getDistrict())
										.append("pOffice", aadhaarInfo.getAddress().getpOffice())
										.append("pin", aadhaarInfo.getAddress().getPin())
										.append("mobile", aadhaarInfo.getAddress().getMobile())
										.append("vtc", aadhaarInfo.getAddress().getVtc()))
						.append("mobile", aadhaarInfo.getMobile()).append("insertedDate", getDate())
						.append("enrolmentNo", aadhaarInfo.getEnrolmentNo());

				mongoDBService.insertDocument("eidiko-documents", "aadhar-certificates", document);

				return ResponseEntity.ok(aadhaarInfo);
			} else if (info instanceof CertificateInfo) {
				CertificateInfo certificateInfo = (CertificateInfo) info;

				document.append("insertedDate", getDate()).append("type", certificateInfo.getType())
						.append("certName", certificateInfo.getCertName()).append("certId", certificateInfo.getCertId())
						.append("whoCertified", certificateInfo.getWhoCertified())
						.append("certDate", certificateInfo.getCertDate());
				mongoDBService.insertDocument("eidiko-documents", "aadhar-certificates", document);

				return ResponseEntity.ok(certificateInfo);
			} else {

				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();

			ExceptionInfo eInfo = new ExceptionInfo();
			eInfo.setType("EXCEPTION");
			eInfo.setMessage(e.getMessage());
			return ResponseEntity.ok(eInfo);
			// return ResponseEntity.status(500).body(null);
		}
	}

	public String getDate() {

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = now.format(formatter);
		return formattedDateTime;

	}
}
