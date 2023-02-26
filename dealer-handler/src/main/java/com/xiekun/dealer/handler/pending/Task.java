package com.xiekun.dealer.handler.pending;

import cn.hutool.core.collection.CollUtil;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.handler.HandlerHolder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送消息
 */
@Data
@Accessors(chain = true)
@Component
public class Task implements Runnable {
    @Autowired
    private HandlerHolder handlerHolder;

    private TaskInfo taskInfo;

    @Override
    public void run() {
        // 发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            Integer sendChannel = taskInfo.getSendChannel();
            handlerHolder.route(sendChannel).doHandler(taskInfo);
        }
    }
}
