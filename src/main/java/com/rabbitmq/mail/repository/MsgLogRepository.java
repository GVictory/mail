package com.rabbitmq.mail.repository;

import com.rabbitmq.mail.model.MsgLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: MsgLogRepository
 * @author: 郭木凯
 * @create_time 2020/5/27
 */

@Repository
public interface MsgLogRepository extends CrudRepository<MsgLog, String> {
}
