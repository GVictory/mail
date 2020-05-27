package com.rabbitmq.mail.service.impl;

import com.rabbitmq.mail.config.RabbitConfig;
import com.rabbitmq.mail.model.Mail;
import com.rabbitmq.mail.model.MsgLog;
import com.rabbitmq.mail.repository.MsgLogRepository;
import com.rabbitmq.mail.service.MsgLogService;
import com.rabbitmq.mail.service.ProduceService;
import com.rabbitmq.mail.util.MessageHelper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @description: ProduceServiceImpl
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@Service
public class ProduceServiceImpl implements ProduceService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MsgLogRepository msgLogRepository;
    @Override
    public boolean send(Mail mail) {
        //生成UUID作为消息的唯一标识
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        mail.setMsgId(uuid);

        //作为宕机时信息丢失的补偿，使用本地消息库进行补偿
        MsgLog msgLog = new MsgLog(uuid, mail, RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME);
        // 消息入库
        msgLogRepository.save(msgLog);

        //CorrelationData是一个数据对象，内部只有一个id，用来标识消息的唯一性
        CorrelationData correlationData = new CorrelationData(uuid);
        //将邮件发给rabbitMQ处理
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, MessageHelper.objToMsg(mail), correlationData);
        return true;
    }
}
