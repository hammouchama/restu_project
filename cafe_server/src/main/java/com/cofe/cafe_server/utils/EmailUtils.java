package com.cofe.cafe_server.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailUtils {

    private JavaMailSender javaMailSender;

    public void sendSimplMessage(String to, String subject , String text, List<String> list){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("hm01.oussama@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list !=null && list.size()>0){
            message.setCc(getCcArray(list));
        }
        javaMailSender.send(message);

    }
    private String[] getCcArray(List<String > cclist){
        String[] cc=new String[cclist.size()];
        for (int i=0;i<cclist.size();i++){
            cc[i]=cclist.get(i);
        }
        return cc;
    }
    public void forgotMail(String to ,String subject ,String password) throws MessagingException{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper =new MimeMessageHelper(message,true);
        messageHelper.setFrom("hm01.oussama@gmail.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Cafe Management " +
                "System</b><br><b>Email: </b> " +
                to + " <br><b>Password: </b> " + password +
                "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg,"text/html");
        javaMailSender.send(message);

    }
}
