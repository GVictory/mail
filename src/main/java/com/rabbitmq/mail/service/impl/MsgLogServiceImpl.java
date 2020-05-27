package com.rabbitmq.mail.service.impl;

import com.rabbitmq.mail.model.MsgLog;
import com.rabbitmq.mail.repository.MsgLogRepository;
import com.rabbitmq.mail.service.MsgLogService;
import com.rabbitmq.mail.util.JodaTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @description: MsgLogServiceImpl
 * @author: 郭木凯
 * @create_time 2020/5/27
 */
@Service
public class MsgLogServiceImpl implements MsgLogService {

    @Autowired
    private MsgLogRepository msgLogRepository;
    @Override
    public void updateStatus(String msgId, Integer status) {
        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msgId);
        msgLog.setStatus(status);
        msgLog.setUpdateTime(new Date());
        msgLogRepository.save(msgLog);
    }

    @Override
    public MsgLog selectByMsgId(String msgId) {
        return msgLogRepository.findById(msgId).orElse(null);
    }

    @Override
    public List<MsgLog> selectTimeoutMsg() {
        //todo 此处需要处理超时消息
        return Collections.emptyList();
    }

    @Override
    public void updateTryCount(String msgId, Date tryTime) {
        Date nextTryTime = JodaTimeUtil.plusMinutes(tryTime, 1);

        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msgId);
        msgLog.setNextTryTime(nextTryTime);
        msgLogRepository.save(msgLog);
    }
}
