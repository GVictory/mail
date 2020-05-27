package com.rabbitmq.mail.model;

import lombok.Data;

/**
 * @description: Mail
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@Data
public class Mail {

    //接收人
    private String to;

    //邮件标题
    private String title;

    //邮件内容
    private String content;

    //消息id
    private String msgId;

}
