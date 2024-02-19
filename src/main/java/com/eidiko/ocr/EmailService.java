package com.eidiko.ocr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm; // Add this import
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void downloadAttachments() throws MessagingException, IOException {
        Session session = Session.getDefaultInstance(System.getProperties());
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", "tondaputirapareddy22@gmail.com", "ggofylarhiyfiijf");

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
        SearchTerm searchTerm = new AndTerm(new SubjectTerm("AADHAR-SCAN"), unseenFlagTerm);

        Message[] messages = inbox.search(searchTerm);

        for (Message message : messages) {
        	
        	System.out.println("SUBJECT :"+message.getSubject());
        	
        	System.out.println("Received Date :"+message.getReceivedDate());
        	System.out.println("ACKNOWLEDGMENT :");
        	sendAcknowledgement(message.getFrom()[0].toString());
     
        	System.out.println("0");
        	message.setFlag(Flags.Flag.SEEN, true);
        	/* if (message.getContent() instanceof Multipart) {
        		 System.out.println("1");
                Multipart multipart = (Multipart) message.getContent();
                System.out.println("2");
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                        String fileName = bodyPart.getFileName();
                        File attachment = new File("C:\\Users\\tonda\\Desktop\\SAMPLE\\INPUT\\" + fileName);
                        ((MimeBodyPart) bodyPart).saveFile(attachment);
                        System.out.println("Attachment downloaded: " + attachment.getAbsolutePath());
                    }
                }
            }*/
        }

        inbox.close(false);
        store.close();
    }
    
    
    private void sendAcknowledgement(String recipient) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setSubject("Acknowledgement: AADHAR-SCAN Processed");
        mailMessage.setText("Dear User,\n\nThank you for your email. We are processing the attached document.\n\nBest Regards,\nTirapa Reddy Tondapu");

        javaMailSender.send(mailMessage);
    }
}
