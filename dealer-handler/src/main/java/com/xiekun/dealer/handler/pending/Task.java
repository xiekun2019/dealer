package com.xiekun.dealer.handler.pending;

import cn.hutool.core.collection.CollUtil;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.handler.HandlerHolder;
import com.xiekun.dealer.handler.handler.deduplication.DeduplicationRuleService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 发送消息
 */
@Data
@Accessors(chain = true)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable {
    @Autowired
    private HandlerHolder handlerHolder;

    @Autowired
    private DeduplicationRuleService deduplicationRuleService;

    private TaskInfo taskInfo;

    @Override
    public void run() {
        // TODO 丢弃消息

        // TODO 屏蔽消息

        // 接受者为空直接返回
        if (CollUtil.isEmpty(taskInfo.getReceiver())) {
            return;
        }
        // TODO 消息去重
        deduplicationRuleService.duplication(taskInfo);

        // TODO 发送消息
        Integer sendChannel = taskInfo.getSendChannel();
        handlerHolder.route(sendChannel).doHandler(taskInfo);

    }
}
