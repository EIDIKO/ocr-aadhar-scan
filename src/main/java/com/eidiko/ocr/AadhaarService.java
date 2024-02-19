package com.eidiko.ocr;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class AadhaarService {

	public Object extractAadhaarInfo(String filePath) throws IOException, TesseractException {

		File imageFile = new File(filePath);
		String ocrText = performOCR(imageFile);
		System.out.println("ocrText: " + ocrText);

		return parseOCRText(ocrText);
	}

	@SuppressWarnings("unused")
	private String performOCR1(File imageFile) throws IOException, TesseractException {
		ITesseract tesseract = new Tesseract();
		// Set the tessdata path if needed (where language data and configuration files
		// are stored)
		// tesseract.setDatapath("path/to/tessdata");

		// Perform OCR on the image file
		return tesseract.doOCR(imageFile);
	}

	private String performOCR(File imageFile) throws IOException {
		try (PDDocument document = PDDocument.load(imageFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);
		}
	}

	private Object parseOCRText(String ocrText) {

		String[] lines = ocrText.split("\\r?\\n");

		if (lines[1].contains("Certification")) {

			return parseOCRTextCert(lines);

		} else {

			return parseOCRTextAadhar(ocrText);
		}
	}

	private CertificateInfo parseOCRTextCert(String[] lines) {

		CertificateInfo cInfo = new CertificateInfo();
		cInfo.setType("CERTIFICATE");
		cInfo.setCertDate(lines[2]);
		cInfo.setCertName(lines[1]);
		cInfo.setWhoCertified(lines[0]);

		return cInfo;

	}

	private AadhaarInfo parseOCRTextAadhar(String ocrText) {
		AadhaarInfo aadhaarInfo = new AadhaarInfo();

		String enrolmentNoPattern = "(\\d{4}/\\d{5}/\\d{5})";
		String enrolmentNo = extractPattern(ocrText, enrolmentNoPattern);

		String namePattern = "\\b([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\b";
		String name = extractNamePattern(ocrText, namePattern);

		String mobilePattern = "Mobile:\\s*(\\d+)";
		String mobile = extractPattern(ocrText, mobilePattern);

		if (mobile.contains("Pattern not found")) {
			mobilePattern = "\\b\\d{10}\\b";
			mobile = extractPattern(ocrText, mobilePattern);
		}

		String pinPattern = "PIN Code:\\s*(\\d+)";
		String pin = extractPattern(ocrText, pinPattern);

		String poPattern = "Address:(.*?)VID :";
		String po = extractPattern(ocrText, poPattern);

		String disPattern = "District:\\s*([^,]+)";
		String dist = extractPattern(ocrText, disPattern);

		String statePattern = "State:\\s*([^,]+)";
		String state = extractPattern(ocrText, statePattern);

		String vtcPattern = "VTC:\\s*([^,]+)";
		String vtc = extractPattern(ocrText, vtcPattern);

		String aadhaarPattern = "(\\d{4} \\d{4} \\d{4})[\\s\\S]*?(?=VID :)";
		String aadhaarNumber = extractPattern(ocrText, aadhaarPattern);

		String vid = "VID : (\\d{4} \\d{4} \\d{4} \\d{4})";
		vid = extractPattern(ocrText, vid);

		System.out.println("Enrolment No.: " + enrolmentNo);
		System.out.println("Name: " + name);

		System.out.println("Mobile: " + mobile);
		System.out.println("Aadhaar Number: " + aadhaarNumber);
		aadhaarInfo.setType("AADHAR");

		aadhaarInfo.setEnrolmentNo(enrolmentNo);

		aadhaarInfo.setAadhaarNumber(aadhaarNumber);
		aadhaarInfo.setMobile(mobile);
		aadhaarInfo.setName(name);
		aadhaarInfo.setVid(vid);

		AddressInfo addInfo = new AddressInfo();

		addInfo.setMobile(mobile);
		addInfo.setDistrict(dist);
		addInfo.setPin(pin);
		addInfo.setpOffice(po);
		addInfo.setState(state);
		addInfo.setVtc(vtc);

		aadhaarInfo.setAddress(addInfo);

		return aadhaarInfo;
	}

	public static String extractPattern(String text, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text);
		if (m.find()) {
			try {
				return m.group(1).replaceAll("\\s", " ");
			} catch (Exception e) {
				return m.group();
			}
		}
		return "Pattern not found";
	}

	public static String extractNamePattern(String text, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text);
		if (m.find()) {
			StringBuilder fullName = new StringBuilder();
			for (int i = 1; i <= m.groupCount(); i++) {
				fullName.append(m.group(i)).append(" ");
			}
			return fullName.toString().trim();
		}
		return "Pattern not found";
	}

}
