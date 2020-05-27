package com.rabbitmq.mail.config;

import com.rabbitmq.mail.model.MsgLogStatus;
import com.rabbitmq.mail.service.MsgLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: RabbitConfig
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private MsgLogService msgLogService;

    //邮件队列
    public static final String MAIL_QUEUE_NAME = "mail.queue";

    //邮件交换机
    public static final String MAIL_EXCHANGE_NAME = "mail.exchange";

    //邮件路由
    public static final String MAIL_ROUTING_KEY_NAME = "mail.routing.key";

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        //实现消息发送到消息交换机的回调逻辑
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause)->{
            if (ack) {
                log.info("消息成功发送到Exchange");
                String msgId = correlationData.getId();
                msgLogService.updateStatus(msgId, MsgLogStatus.DELIVER_SUCCESS);
            } else {
                log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
            }
        });
        //触发setReturnCallback回调必须设置Mandatory=true，否则Exchange找不到Queue则直接丢弃消息，而不会触发回调
        rabbitTemplate.setMandatory(true);
        //消息从Exchange到Queue失败的回调方法
        rabbitTemplate.setReturnCallback((message, replyCode, replyText,exchange,routingKey)-> {
            log.info("消息从Exchange到Queue失败，exchange：{}，route：{}，replyCode：{}，replyText：{}，message：{}", exchange, routingKey, replyCode, replyText, message);
        });
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    //声明队列
    @Bean
    public Queue mailQueue() {
        return new Queue(MAIL_QUEUE_NAME, true);
    }

    //声明交换机
    @Bean
    public DirectExchange mailExchange() {
        return new DirectExchange(MAIL_EXCHANGE_NAME, true, false);
    }

    //将队列和交换机进行绑定
    @Bean
    public Binding mailBinding() {
        //将mail.queue绑定到mail.exchange，绑定的规则为mail.routing.key
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTING_KEY_NAME);
    }
}
