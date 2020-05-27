package com.rabbitmq.mail.util;

import com.rabbitmq.mail.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * @description: MailUtil
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@Component
@Slf4j
public class MailUtil {

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private MailSender mailSender;

    public Boolean send(Mail mail){
        //构建邮件发送实体
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        //发送人
        simpleMailMessage.setFrom(from);
        //接收人
        simpleMailMessage.setTo(mail.getTo());
        //邮件标题
        simpleMailMessage.setSubject(mail.getTitle());
        //邮件内容
        simpleMailMessage.setText(mail.getContent());
        try {
            mailSender.send(simpleMailMessage);
            log.info("邮件发送成功");
            return true;
        }catch (MailException e){
            log.error("邮件发送失败, to: {}, title: {}", simpleMailMessage.getTo(), simpleMailMessage.getText(), e);
            return false;
        }
    }
}
