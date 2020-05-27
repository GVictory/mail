package com.rabbitmq.mail.model;

import com.rabbitmq.mail.util.JodaTimeUtil;
import com.rabbitmq.mail.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: MsgLog
 * @author: 郭木凯
 * @create_time 2020/5/27
 */

@Data
@Entity
@Table(name = "msg_log")
@NoArgsConstructor
public class MsgLog implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String msgId;

    private String msg;

    private String exchange;

    private String routingKey;

    private Integer status;

    private Integer tryCount;

    private Date nextTryTime;

    private Date createTime;

    private Date updateTime;

    public MsgLog(String msgId, Object msg, String exchange, String routingKey) {
        this.msgId = msgId;
        this.msg = JsonUtil.objToStr(msg);
        this.exchange = exchange;
        this.routingKey = routingKey;

        this.status = MsgLogStatus.DELIVERING;
        this.tryCount = 0;

        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.nextTryTime = (JodaTimeUtil.plusMinutes(date, 1));
    }
}
