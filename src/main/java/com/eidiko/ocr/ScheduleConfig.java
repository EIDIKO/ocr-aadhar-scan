package com.eidiko.ocr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
public class ScheduleConfig {

	@Autowired
	private AadhaarRESTService aadhaarService;

    @Value("${file.path}")
    private String filePath;



    @Scheduled(fixedRate = 300000) 
    public void scheduleTask() {
    	LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        System.out.println("Scheduller Ran Last Time At: " + formattedDateTime);

        aadhaarService.extractInfo(filePath);
    }
}

