package com.xiekun.dealer.handler.receiver.service;

import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.support.domain.MessageTemplate;

import java.util.List;

public interface ConsumeService {
    /**
     * 从 MQ 拉到消息进行消费，发送消息
     */
    void consume2Send(List<TaskInfo> taskInfoLists);


    /**
     * 从 MQ 拉到消息进行消费，撤回消息
     */
    void consume2recall(MessageTemplate messageTemplate);
}
