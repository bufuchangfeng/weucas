package com.ucas.impl;

import com.ucas.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender javaMailSender;

    public boolean send(String sendTo, String subject, String content){
        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("924761163@qq.com");    //设置发信人，发信人需要和spring.mail.username配置的一样否则报错
            message.setTo(sendTo);				    //设置收信人
            message.setSubject(subject);	//设置主题
            message.setText(content,false);  	//第二个参数true表示使用HTML语言来编写邮件
            this.javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
