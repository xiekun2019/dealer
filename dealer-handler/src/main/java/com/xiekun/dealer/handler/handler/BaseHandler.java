package com.xiekun.dealer.handler.handler;

import com.xiekun.dealer.common.domain.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Slf4j
public abstract class BaseHandler implements Handler {
    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 标识渠道的 Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            log.info("send success!");
        } else {
            log.info("send failed!");
        }
    }

    /**
     * 统一处理的handler接口
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
