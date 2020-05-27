package com.rabbitmq.mail.controller;

import com.rabbitmq.mail.model.Mail;
import com.rabbitmq.mail.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: MailController
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private ProduceService produceService;

    @PostMapping("/send")
    public boolean sendMail(Mail mail){
        return produceService.send(mail);
    }
}
