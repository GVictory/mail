package com.rabbitmq.mail.consume;

import com.rabbitmq.client.Channel;
import com.rabbitmq.mail.config.RabbitConfig;
import com.rabbitmq.mail.model.Mail;
import com.rabbitmq.mail.model.MsgLog;
import com.rabbitmq.mail.model.MsgLogStatus;
import com.rabbitmq.mail.service.MsgLogService;
import com.rabbitmq.mail.util.JsonUtil;
import com.rabbitmq.mail.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @description: ConsumerMailService
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@Component
@Slf4j
public class ConsumerMailService {

    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private MsgLogService msgLogService;

    //处理消息
    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        String str=new String(message.getBody());
        //将消息转换为对象
        Mail mail = JsonUtil.strToObj(str, Mail.class);
        log.info("收到消息: {}", mail.toString());

        //如果数据库已经存在且消费，则不处理（幂等性）
        String msgId = mail.getMsgId();
        MsgLog msgLog = msgLogService.selectByMsgId(msgId);
        if (null == msgLog || msgLog.getStatus().equals(MsgLogStatus.CONSUMED_SUCCESS)) {// 消费幂等性
            log.info("重复消费, msgId: {}", msgId);
            return;
        }

        //获取携带的属性
        MessageProperties properties = message.getMessageProperties();
        //tag为当前消息的编号，消费确认时使用
        long tag = properties.getDeliveryTag();
        boolean success = mailUtil.send(mail);
        if (success) {
            // 消费确认
            channel.basicAck(tag, false);
        } else {
            channel.basicNack(tag, false, true);
        }
    }
}
