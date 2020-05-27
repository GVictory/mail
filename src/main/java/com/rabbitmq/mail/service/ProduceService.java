package com.rabbitmq.mail.service;

import com.rabbitmq.mail.model.Mail;

/**
 * @description: ProduceService
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
public interface ProduceService {
    boolean send(Mail mail);
}
