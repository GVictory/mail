package com.rabbitmq.mail.model;

/**
 * @description: MsgLogStatus
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
public interface MsgLogStatus {
    Integer DELIVERING = 0;// 消息投递中
    Integer DELIVER_SUCCESS = 1;// 投递成功
    Integer DELIVER_FAIL = 2;// 投递失败
    Integer CONSUMED_SUCCESS = 3;// 已消费
}
