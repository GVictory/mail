package com.rabbitmq.mail.service;

import com.rabbitmq.mail.model.MsgLog;

import java.util.Date;
import java.util.List;

/**
 * @description: MsgLogService
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
public interface MsgLogService {

    void updateStatus(String msgId, Integer status);

    MsgLog selectByMsgId(String msgId);

    List<MsgLog> selectTimeoutMsg();

    void updateTryCount(String msgId, Date tryTime);
}
